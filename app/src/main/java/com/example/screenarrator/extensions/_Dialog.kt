package com.example.screenarrator.extensions


import androidx.appcompat.app.AlertDialog
import com.example.screenarrator.model.Gesture

fun AlertDialog.onAccessibilityGesture(gesture: Gesture) {
    when (gesture) {
        Gesture.ONE_FINGER_SWIPE_LEFT -> AlertDialog.BUTTON_NEGATIVE
        Gesture.ONE_FINGER_SWIPE_RIGHT -> AlertDialog.BUTTON_POSITIVE
        Gesture.ONE_FINGER_SWIPE_DOWN -> AlertDialog.BUTTON_NEUTRAL
        else -> null
    }?.let { button ->
        getButton(button)?.performClick()
    }
}