package com.example.screenarrator.extensions

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.screenarrator.R

fun RecyclerView.addItemDecoration(direction: Int = DividerItemDecoration.VERTICAL, resource: Int = R.drawable.divider) {
    ContextCompat.getDrawable(context, resource)?.let { drawable ->
        val decoration = DividerItemDecoration(context, direction)
        decoration.setDrawable(drawable)

        addItemDecoration(decoration)
    }
}