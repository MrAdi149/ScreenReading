package com.example.screenarrator.model

import android.content.Context

interface Training: Item {
    fun completed(context: Context): Boolean
    fun completed(context: Context, completed: Boolean)
}