package com.example.screenarrator.views.actions

import android.content.Context
import com.example.screenarrator.R
import com.example.screenarrator.extensions.className
import com.example.screenarrator.model.Action

abstract class FocusActionView(
    context: Context,
    action: Action,
    layoutId: Int
) : ActionView(context, action, layoutId) {

    private val COUNT = 3
    private val CLASS_NAME = getClassName()

    override fun onFocusChanged(elements: List<String>) {
        if (elements.size  >= COUNT){
            if (elements.takeLast(COUNT).all { className ->

                    className == CLASS_NAME

                }){
                correct()
            }
        }
    }

    abstract fun getClassName(): String
}


class HeadingActionView(context: Context): FocusActionView(context, Action.HEADINGS, R.layout.action_headings){
    override fun getClassName() = className<HeadingTextView>()
}

class LinkActionView(context: Context): FocusActionView(context, Action.LINKS, R.layout.action_links){
    override fun getClassName() = className<LinkTextView>()
}