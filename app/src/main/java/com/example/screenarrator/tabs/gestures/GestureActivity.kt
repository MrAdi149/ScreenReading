package com.example.screenarrator.tabs.gestures

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.SpannableString
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.screenarrator.R
import com.example.screenarrator.extensions.*
import com.example.screenarrator.helpers.Accessibility
import com.example.screenarrator.helpers.Events
import com.example.screenarrator.model.Constants
import com.example.screenarrator.model.Gesture
import com.example.screenarrator.services.ScreenReaderService
import com.example.screenarrator.views.gestures.GestureView
import com.example.screenarrator.views.gestures.GestureViewCallback
import com.example.screenarrator.widgets.ToolbarActivity
import java.util.*
import kotlin.concurrent.schedule

class GestureActivity: ToolbarActivity(), GestureViewCallback {

    private val TAG = "GestureActivity"
    private lateinit var titleTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var gestureImageView: ImageView
    private lateinit var feedbackTextView: TextView
    private lateinit var container: FrameLayout

    private val gestures: ArrayList<Gesture> by lazy {
        intent.getGestures() ?: arrayListOf()
    }

    private val gesture: Gesture by lazy {
        gestures.firstOrNull() ?: intent.getGesture() ?: Gesture.ONE_FINGER_TOUCH
    }

    private val instructions: Boolean by lazy {
        intent.getInstructions()
    }

    private lateinit var gestureView: GestureView

    private var dialog: AlertDialog? = null
    private var errorLimit = 5
    private var errorCount = 0
    private var finished = false

    private val isPracticing: Boolean
        get() = gestures.isNotEmpty()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // Received kill event
            intent?.getBooleanExtra(Constants.SERVICE_KILLED, false)?.let { killed ->
                Log.d(TAG, "ScreenReaderService has been killed")
            }

            // Received gesture event
            (intent?.getSerializableExtra(Constants.SERVICE_GESTURE) as? Gesture)?.let { gesture ->
                if (dialog != null) {
                    dialog?.onAccessibilityGesture(gesture) // Pass gesture to AlertDialog if shown.
                } else {
                    gestureView.onAccessibilityGesture(gesture) // Pass gesture to GestureView.
                }
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_gesture

    override fun getToolbarTitle(): SpannableString {
        return SpannableString("")
    }

    override fun onViewCreated() {
        super.onViewCreated()

        // Initialize container
        container = findViewById(R.id.container)
        titleTextView = findViewById(R.id.titleTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        gestureImageView = findViewById(R.id.gestureImageView)
        feedbackTextView = findViewById(R.id.feedbackTextView)

        // Setup GestureView
        gestureView = gesture.view(this)
        gestureView.callback = this
        container.addView(gestureView)
        Accessibility.setTraversalOrder(gestureView)

        // Setup other views
        titleTextView.text = gesture.title(this)
        descriptionTextView.text = gesture.description(this)
        gestureImageView.setImageResource(gesture.image(this))

        // Setup instructions
        if (instructions) {
            Accessibility.label(container, String.format("%s. %s", titleTextView.text, descriptionTextView.text))
        } else {
            descriptionTextView.setVisible(false)
            gestureImageView.setVisible(false)
            Accessibility.label(container, titleTextView.text)
        }

        // Listen to events from ScreenReaderService
        val filter = IntentFilter()
        filter.addAction(Constants.SERVICE_ACTION)
        registerReceiver(receiver, filter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (instructions) {
            menuInflater.inflate(R.menu.explanation, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_explanation) {
            showDialog(title = gesture.title(this), message = gesture.explanation(this))
            return true
        }
        return false
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        dialog?.dismiss()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        if (Accessibility.screenReader(this)) {
            if (ScreenReaderService.isEnabled(this)) {
                Timer().schedule(500) {
                    runOnUiThread {
                        Accessibility.focus(gestureView)
                    }
                }
            } else {
                showError(R.string.service_disabled_error) {
                    finish()
                }
            }
        }
    }

    override fun correct(gesture: Gesture) {
        finished = true
        feedbackTextView.visibility = View.GONE

        events.log(Events.Category.gesture_completed, gesture.identifier, errorCount)
        gesture.completed(baseContext, true)
        setResult(RESULT_OK)

        toast(R.string.gesture_completed_message) {
            if (isPracticing) {
                next()
            }
            finish()
        }
    }

    private fun next() {
        gestures.removeAt(0)

        startActivity<GestureActivity> {
            setGestures(gestures)
            setInstructions(instructions)
        }
    }

    override fun incorrect(gesture: Gesture, feedback: SpannableString) {
        if (finished) {
            return
        }

        val feedback = if (instructions) {
            feedback
        } else {
            getSpannable(R.string.gestures_feedback_incorrect)
        }

        // Show feedback
        feedbackTextView.animate()
            .alpha(0.0f)
            .setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animator: Animator) {
                    if (finished) {
                        return
                    }
                    feedbackTextView.visibility = View.VISIBLE
                    feedbackTextView.text = feedback
                    feedbackTextView.animate().alpha(1.0f).setDuration(250)
                }
            })

        // Check if error count exceeds limit
        errorCount++
        if (errorCount < errorLimit) {
            return
        }

        // Show option dialog
        val incorrectMessage = getSpannable(R.string.gestures_feedback_incorrect_amount, errorCount)

        val actionMessage = if (Accessibility.screenReader(this)) {
            if (isPracticing) {
                getSpannable(R.string.gestures_feedback_wrong_action_talkback_practice)
            } else {
                getSpannable(R.string.gestures_feedback_wrong_action_talkback)
            }
        } else {
            if (isPracticing) {
                getSpannable(R.string.gestures_feedback_wrong_action_practice)
            } else {
                getSpannable(R.string.gestures_feedback_wrong_action)
            }
        }

        val message = TextUtils.concat(incorrectMessage, actionMessage)

        val builder = AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(getSpannable(R.string.action_continue)) { _, _ ->
                errorLimit *= 2
            }
            .setNegativeButton(getSpannable(R.string.action_stop)) { _, _ ->
                finish()
            }
            .setCancelable(false)
            .setOnDismissListener {
                dialog = null
            }

        if (isPracticing) {
            builder.setNeutralButton(getSpannable(R.string.action_skip)) { _, _ ->
                next()
                finish()
            }
        }

        if (dialog == null) {
            dialog = builder.show()
        }
    }
}