package com.example.screenarrator.model

import android.content.Context
import android.graphics.Path
import android.text.SpannableString
import com.example.screenarrator.R
import com.example.screenarrator.extensions.getSpannable

enum class Direction(var fingers: Int = 1) {
    UP,
    TOP_RIGHT,
    RIGHT,
    BOTTOM_RIGHT,
    DOWN,
    BOTTOM_LEFT,
    LEFT,
    TOP_LEFT,
    UNKNOWN;

    fun title(context: Context): SpannableString{
        return context.getSpannable(title)
    }

    val title: Int
        get() {
            return when (this){
                UP -> R.string.direction_up
                TOP_RIGHT -> R.string.direction_top_right
                RIGHT -> R.string.direction_right
                BOTTOM_RIGHT -> R.string.direction_bottom_right
                DOWN -> R.string.direction_down
                BOTTOM_LEFT -> R.string.direction_bottom_left
                LEFT -> R.string.direction_left
                TOP_LEFT -> R.string.direction_top_left
                UNKNOWN -> R.string.direction_unknown
            }
        }

    override fun toString(): String {
        return "${super.toString()}($fingers)"
    }

    companion object{
        fun feedback(context: Context, directions: Array<Direction>): String{
            return directions.joinToString(separator = ", ") { direction ->
                direction.title(context)
            }
        }
    }
}