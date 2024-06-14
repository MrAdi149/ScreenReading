package com.example.screenarrator.tabs.actions

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.text.SpannableString
import android.widget.ScrollView
import com.example.screenarrator.R
import com.example.screenarrator.extensions.doGetAction
import com.example.screenarrator.extensions.identifier
import com.example.screenarrator.extensions.showDialog
import com.example.screenarrator.helpers.Accessibility
import com.example.screenarrator.model.Action
import com.example.screenarrator.helpers.Events
import com.example.screenarrator.views.actions.ActionViewCallback
import com.example.screenarrator.widgets.ToolbarActivity

class ActionActivity: ToolbarActivity(), ActionViewCallback {

    private val startTime = System.currentTimeMillis()
    private lateinit var scrollView: ScrollView

    private val action: Action by lazy {
        intent.doGetAction() ?: Action.SELECT
    }

    override fun getLayoutId() = R.layout.activity_action

    override fun getToolbarTitle() = action.title(this)

    override fun onViewCreated() {
        super.onViewCreated()

        val view = action.view(this)
        view.callback = this
        scrollView.addView(view)

        // Listen to accessibility state changes
        Accessibility.accessibilityManager(this)?.let { manager ->
            manager.addAccessibilityStateChangeListener {
                if (manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN).isEmpty()) {
                    showDialog(R.string.actions_talkback_disabled_title, R.string.actions_talkback_disabled_message) {
                        finish()
                    }
                }
            }
        }
    }

    override fun correct(action: Action) {
        val elapsedTime = (System.currentTimeMillis() - startTime).toInt() / 1000
        events.log(Events.Category.action_completed, action.identifier, elapsedTime)

        action.completed(this, true)
        setResult(RESULT_OK)

        toast(R.string.action_completed_message) {
            finish()
        }
    }

    override fun incorrect(action: Action, feedback: SpannableString) {
        toast(feedback)
    }

    override fun startActivity(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            // Ignore view actions
            return
        }
        super.startActivity(intent)
    }
}