package com.example.screenarrator.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ActionMenuView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import com.example.screenarrator.extensions.toSpannable
import com.example.screenarrator.extensions.toast
import com.example.screenarrator.helpers.Accessibility
import java.lang.reflect.Method

class AccessibilityToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.toolbarStyle
) : androidx.appcompat.widget.Toolbar(context, attrs, defStyleAttr) {

    init {
        // Listen to hierarchy changes to improve accessibility
        setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
            override fun onChildViewAdded(parent: View?, child: View?) {
                // Title view?
                if (child is TextView) {
                    // Mark title as heading
                    Accessibility.heading(child)

                    // Show full title on long press
                    child.setOnLongClickListener {
                        val title = context.toSpannable(child.text)
                        toast(context, title)
                        true
                    }
                }

                // Menu view?
                if (child is ActionMenuView) {
                    child.setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
                        @SuppressLint("RestrictedApi")
                        override fun onChildViewAdded(parent: View?, child: View?) {
                            // Mark menu items as button & set language
                            if (child is ActionMenuItemView) {
                                Accessibility.button(child)
                                Accessibility.language(child)
                            }
                        }

                        override fun onChildViewRemoved(parent: View?, child: View?) {
                            // Ignored
                        }
                    })
                }
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {
                // Ignored
            }
        })
    }
}