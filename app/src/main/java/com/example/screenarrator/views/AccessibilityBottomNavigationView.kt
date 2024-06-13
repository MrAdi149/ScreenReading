package com.example.screenarrator.views

import android.content.Context
import android.util.AttributeSet
import com.example.screenarrator.helpers.Accessibility

class AccessibilityBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.toolbarStyle
) : com.google.android.material.bottomnavigation.BottomNavigationView(context, attrs, defStyleAttr) {

    init {
        // Set accessibility language
        Accessibility.languages(this)
    }
}