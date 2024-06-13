package com.example.screenarrator.extensions

import android.view.MotionEvent

fun MotionEvent.isStart(): Boolean {
    return actionMasked == MotionEvent.ACTION_DOWN || actionMasked == MotionEvent.ACTION_HOVER_ENTER
}

fun MotionEvent.isEnd(): Boolean {
    return actionMasked == MotionEvent.ACTION_UP || actionMasked == MotionEvent.ACTION_HOVER_EXIT
}