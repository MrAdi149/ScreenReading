package com.example.screenarrator.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Region
import android.os.Build
import android.util.Log
import android.view.KeyEvent
import android.provider.Settings
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.hardware.display.DisplayManagerCompat
import com.example.screenarrator.MainActivity
import com.example.screenarrator.R
import com.example.screenarrator.extensions.getSpannable
import com.example.screenarrator.model.Constants
import com.example.screenarrator.model.Gesture
import java.io.Serializable

class ScreenReaderService: AccessibilityService() {

    private val TAG = "ScreenReaderService"
    private val GESTURE_TRAINING_CLASS_NAME = MainActivity::class.java.name

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")

        setPassThroughRegions()
        startGestureTraining()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.i(TAG, "onAccessibilityEvent: $event")

        if(event == null || event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            return
        }

        if(event.packageName == null || event.packageName.isEmpty()){
            return
        }

        if(event.packageName == this.packageName){
            return
        }

        if(event.packageName.contains("accessibility")){
            return
        }

        val serviceName = getString(R.string.service_label)
        if(event.text.contains(serviceName)){
            return
        }

        if(isGestureTraining()){
            return
        }

        kill()
    }

    override fun onInterrupt() {
        Log.i(TAG, "onInterrupt")
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        Log.d(TAG, "onKeyEvent: $event")
        return super.onKeyEvent(event)
    }

    override fun onServiceConnected() {
        Log.i(TAG, "Service Connected")
        super.onServiceConnected()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onGesture(gestureId: Int): Boolean{
        Log.i(TAG, "onGesture: $gestureId")

        Gesture.from(gestureId)?.let { gesture ->
            broadcast(Constants.SERVICE_GESTURE, gesture)
        }

        if(!isTouchingExploring()){
            kill()
            return false
        }
        return true
    }

    private fun setPassThroughRegions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            ContextCompat.getSystemService(this, WindowManager::class.java)?.let { windowManager ->
                val bounds = windowManager.currentWindowMetrics.bounds
                val region = Region(bounds.left, bounds.top, bounds.right, bounds.bottom)

                val displays = DisplayManagerCompat.getInstance(this).displays
                displays.forEach { display ->
                    Log.d(TAG, "Setting passthrough for display ${display.displayId} to: $region")
                    setTouchExplorationPassthroughRegion(display.displayId, region)
                    setGestureDetectionPassthroughRegion(display.displayId, region)
                }
            }
        }
    }

    private fun isGestureTraining(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getRunningTasks(1).firstOrNull()?.topActivity?.let { activity ->
            return activity.className == GESTURE_TRAINING_CLASS_NAME
        }
        return false
    }

    private fun startGestureTraining(){
        val gestures = Gesture.randomized()
        gestures.forEach{ gesture ->
            gesture.completed(this, false)
        }
    }

    private fun isTouchingExploring(): Boolean{
        var count = 0
        val manager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val services = manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for(service in services){
            val flags = service.capabilities
            val capability = AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION

            if(flags and capability == capability){
                count++
            }
        }
        return count >= 2
    }

    companion object {

        var instructions = true

        fun isEnabled(context: Context):Boolean{
            (context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).let { manager->
                val services = manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
                for(service in services){
                    if(service.resolveInfo.serviceInfo.name == ScreenReaderService::class.java.name){
                        return true
                    }
                }
            }
            return false
        }

        fun enable(context: Context, instructions: Boolean = false){
            this.instructions = instructions

            AlertDialog.Builder(context)
                .setTitle(context.getSpannable(R.string.service_enable_title))
                .setMessage(context.getSpannable(R.string.service_enable_message))
                .setPositiveButton(context.getSpannable(R.string.action_activate)){ _, _ ->

                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }
                .setNegativeButton(context.getSpannable(R.string.action_cancel)){ _, _ ->

                }
                .setCancelable(false)
                .show()
        }
    }

    private fun kill(){
        broadcast(Constants.SERVICE_KILLED, true)
        disableSelf()
    }

    private fun broadcast(key: String, value: Serializable){
        val intent = Intent(Constants.SERVICE_ACTION)
        intent.setPackage(packageName)
        intent.putExtra(key, value)
        sendBroadcast(intent)
    }
}