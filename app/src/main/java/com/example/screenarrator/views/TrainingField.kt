package com.example.screenarrator.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class TrainingField(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatEditText(context, attrs, defStyle) {

    init {
        showSoftInputOnFocus = false
        setTextIsSelectable(true)
    }

    interface OnSelectionChangedListener{
        fun onSelectionChanged(start: Int, end: Int)
    }

    var callback : OnSelectionChangedListener? = null

    override fun onSelectionChanged(start: Int, end: Int) {
        super.onSelectionChanged(start, end)

        callback?.onSelectionChanged(start, end)
    }
}