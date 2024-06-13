package com.example.screenarrator.model

import android.content.Context
import android.text.SpannableString

interface Item {
    fun title(context: Context): SpannableString
}