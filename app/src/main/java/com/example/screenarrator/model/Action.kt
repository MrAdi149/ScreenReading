package com.example.screenarrator.model

import android.content.Context
import android.text.SpannableString
import com.example.screenarrator.extensions.identifier
import com.example.screenarrator.helpers.Preferences
import com.example.screenarrator.views.actions.*
import com.example.screenarrator.extensions.getSpannable
import java.io.Serializable

enum class Action: Training, Serializable {

    HEADINGS,
    LINKS,
    SELECT,
    COPY,
    PASTE;

    private fun spannable(context: Context, property: String): SpannableString {
        return context.getSpannable("action_${identifier}_${property}")
    }

    override fun title(context: Context): SpannableString {
        return spannable(context, "title")
    }

    fun view(context: Context): ActionView {
        return when (this) {
            HEADINGS -> HeadingActionView(context)
            LINKS -> LinkActionView(context)
            SELECT -> SelectionActionView(context)
            COPY -> CopyActionView(context)
            PASTE -> PasteActionView(context)
        }
    }

    override fun completed(context: Context): Boolean {
        return Preferences.isActionCompleted(this)
    }
    override fun completed(context: Context, completed: Boolean) {
        Preferences.isActionCompleted(this, true)
    }
}