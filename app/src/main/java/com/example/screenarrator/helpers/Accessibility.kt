package com.example.screenarrator.helpers

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.os.Build
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityEventCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.allViews
import com.example.screenarrator.extensions.toSpannable

object Accessibility {


    fun accessibilityManager(context: Context?):AccessibilityManager?{
        if(context != null){
            val service = ContextCompat.getSystemService(context, AccessibilityManager::class.java)
            if(service is AccessibilityManager && service.isEnabled){
                return service
            }
        }
        return  null
    }

    fun interrupt(context: Context?){
        accessibilityManager(context)?.interrupt()
    }

    fun announce(context: Context?, message: String){
        accessibilityManager(context)?.let {
            accessibilityManager ->
                                    val event = AccessibilityEvent.obtain(AccessibilityEventCompat.TYPE_ANNOUNCEMENT)
                                    event.text.add(message)

                                    accessibilityManager.sendAccessibilityEvent(event)
        }
    }

    fun screenReader(context: Context?):Boolean{
        return accessibilityManager(context)?.getEnabledAccessibilityServiceList(
            AccessibilityServiceInfo.FEEDBACK_SPOKEN
        )?.isNotEmpty()?:false
    }

    fun touchExploration(context: Context?):Boolean{
        return accessibilityManager(context)?.isTouchExplorationEnabled?:false
    }

    fun focus(view: View): View{
        view.isFocusable = true
        view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
        return view
    }

    fun View.setAccessibilityFocus(): View{
        return focus(this)
    }

    fun accessibilityDelegate(view: View, callback: (host: View, info: AccessibilityNodeInfoCompat)->Unit){
        ViewCompat.setAccessibilityDelegate(
            view,
            object : AccessibilityDelegateCompat(){
                override fun onInitializeAccessibilityNodeInfo(
                    host: View,
                    info: AccessibilityNodeInfoCompat
                ) {
                    super.onInitializeAccessibilityNodeInfo(host, info)
                    callback(host, info)
                }
            }
        )
    }

    fun heading(view: View, isHeading: Boolean = true): View{
        ViewCompat.setAccessibilityHeading(view, isHeading)
        return view
    }

    fun View.setAsAccessibilityHeading(isHeading: Boolean = true): View{
        return heading(this, isHeading)
    }

    fun button(view: View, isButton: Boolean = true): View {
        accessibilityDelegate(view){ _, info ->
            info.className = if(isButton){
                Button::class.java.name
            }else{
                this::class.java.name
            }
        }
        return view
    }

    fun View.setAccessibilityButton(isButton: Boolean = true): View{
        return button(this, isButton)
    }

    fun state(view: View, state: CharSequence?):View{
        ViewCompat.setStateDescription(view, state)
        return view
    }

    fun View.setAccessibilityState(state: String):View{
        return state(this, state)
    }

    fun action(view: View, type: Int, description: CharSequence): View{
        accessibilityDelegate(view){ _, info->
            val action = AccessibilityNodeInfoCompat.AccessibilityActionCompat(type, description)
            info.addAction(action)
        }
        return view
    }

    fun View.addAccessibilityAction(type:Int, description: CharSequence): View{
        return action(this, type, description)
    }

    fun label(view: View, label: CharSequence): View{
        view.contentDescription = view.context.toSpannable(label)
        return view
    }

    fun View.setAccessibilityLabel(label: CharSequence): View {
        return label(this, label)
    }

    fun setTraversal(view: View, before: View? = null, after: View? = null) {
        ViewCompat.setAccessibilityDelegate(view, object : AccessibilityDelegateCompat() {
            override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfoCompat) {
                before?.let { before ->
                    info.setTraversalBefore(before)
                }
                after?.let { after ->
                    info.setTraversalAfter(after)
                }
                super.onInitializeAccessibilityNodeInfo(host, info)
            }
        })
    }

    fun setTraversalAfter(view: View, after: View) {
        setTraversal(view, after = after)
    }


    fun setTraversalOrder(vararg views: View) {
        if (views.size > 1) {
            for (i in 0..views.size-2) {
                setTraversalAfter(views[i], views[i+1])
            }
        }
    }

    fun languages(view: View) {
        if (view is ViewGroup) {
            view.allViews.forEach { child ->
                language(child)
            }
        } else {
            language(view)
        }
    }

    fun language(view: View) {
        val contentDescription = view.contentDescription
        if (contentDescription != null && contentDescription.isNotEmpty()) {
            view.contentDescription = view.context.toSpannable(contentDescription)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val tooltip = view.tooltipText
            if (tooltip != null && tooltip.isNotEmpty()) {
                view.tooltipText = view.context.toSpannable(tooltip)
            }
        }

        if (view is TextView) {
            val text = view.text
            if (text != null && text.isNotEmpty()) {
                view.text = view.context.toSpannable(text)
            }
        }
    }

}