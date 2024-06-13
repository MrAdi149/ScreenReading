package com.example.screenarrator.views.actions

import android.content.Context
import android.util.AttributeSet
import android.view.accessibility.AccessibilityEvent
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.ViewCompat

class HeadingTextView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int =0
) : AppCompatTextView(context, attrs, defStyle){


    init {
        ViewCompat.setAccessibilityHeading(this, true)
    }

    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent?): Boolean {
        event?.className = javaClass.name
        return super.dispatchPopulateAccessibilityEvent(event)
    }
}