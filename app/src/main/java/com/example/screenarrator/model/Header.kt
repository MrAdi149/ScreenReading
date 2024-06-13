package com.example.screenarrator.model

import android.content.Context
import android.text.SpannableString
import com.example.screenarrator.extensions.getSpannable
import java.io.Serializable

data class Header(
    val textId: Int
): Item, Serializable {

    override fun title(context: Context): SpannableString {
        return context.getSpannable(textId)
    }
}
