package com.example.screenarrator.views.gestures

import android.content.Context
import android.view.MotionEvent
import com.example.screenarrator.R
import com.example.screenarrator.extensions.isStart
import com.example.screenarrator.model.Gesture

class TouchGestureView(
    context: Context,
    gesture: Gesture
):GestureView(gesture,context){

    private var touched = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        if(!touched && event?.isStart() == true){
            touched = true
            correct()
        }
        return true
    }
    override fun onAccessibilityGesture(gesture: Gesture) {
        if(gesture == gesture){
            correct()
        }else{
            incorrect(R.string.gestures_feedback_touch)
        }
    }

}