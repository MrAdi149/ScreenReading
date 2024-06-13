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
        setOnHierarchyChangeListener(object : OnHierarchyChangeListener {
            @SuppressLint("RestrictedApi")
            override fun onChildViewAdded(parent: View?, child: View?) {
                if (child is TextView) {
                    Accessibility.heading(child)

                    child.setOnLongClickListener {
                        val title = context.toSpannable(child.text)
                        toast(context, title)
                        true
                    }
                }

                if (child is ActionMenuView) {
                    val method: Method? = try {
                        ActionMenuView::class.java.getDeclaredMethod("getChildAt", Int::class.javaPrimitiveType)
                    } catch (e: NoSuchMethodException) {
                        null
                    }

                    method?.isAccessible = true

                    // Iterate over ActionMenuView children using reflection
                    for (i in 0 until child.childCount) {
                        val actionMenuItemView = method?.invoke(child, i) as? ActionMenuItemView
                        actionMenuItemView?.let {
                            Accessibility.button(it)
                            Accessibility.language(it)
                        }
                    }
                }
            }

            override fun onChildViewRemoved(parent: View?, child: View?) {
                // Not implemented
            }
        })
    }
}
