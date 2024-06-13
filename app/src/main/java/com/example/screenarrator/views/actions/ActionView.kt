package com.example.screenarrator.views.actions

import android.content.Context
import android.text.SpannableString
import android.view.ViewGroup
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.widget.LinearLayout
import com.example.screenarrator.extensions.getSpannable
import com.example.screenarrator.helpers.Accessibility
import com.example.screenarrator.model.Action

interface ActionViewCallback{
    fun correct(action: Action)
    fun incorrect(action: Action, feedback: SpannableString)
}

abstract class ActionView(context: Context, private val action: Action, layoutId: Int): LinearLayout(context) {


    var callback: ActionViewCallback? = null // Define the callback property

    init {
        val view = inflate(context, layoutId, this)
        view.accessibilityDelegate = FocusDelegate()
        Accessibility.languages(view)
    }

    open fun onFocused(host: ViewGroup?, child: View?, className: CharSequence){

    }

    open fun onFocusChanged(elements: List<String>){

    }

    open fun correct(){
        callback?.correct(action)
    }

    open fun incorrect(feedback : Int){
        callback?.incorrect(action, context.getSpannable(feedback))
    }

    private inner class FocusDelegate: View.AccessibilityDelegate(){
        private val elements = arrayListOf<String>()

        override fun onRequestSendAccessibilityEvent(
            host: ViewGroup,
            child: View,
            event: AccessibilityEvent
        ): Boolean {
            if(event != null && event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED){
                event.className?.let { onFocused(host, child, it) }
                elements.add(event.className.toString())
                onFocusChanged(elements)
            }
            return super.onRequestSendAccessibilityEvent(host, child, event)
        }
    }
}