package com.example.screenarrator.views.gestures

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import com.example.screenarrator.R
import com.example.screenarrator.extensions.isEnd
import com.example.screenarrator.extensions.isStart
import com.example.screenarrator.model.Direction
import com.example.screenarrator.model.Gesture
import com.example.screenarrator.services.ScreenReaderService

class SwipeGestureView(context: Context, gesture: Gesture): GestureView(gesture, context) {

    private val TAG = "SwipeGestureView"
    var swiped = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)

        if(event != null){
            gestureDetector.onTouchEvent(event)

            if (event.isStart()){
                swiped = false
            }else if(event.isEnd()){
                if(!swiped){
                    incorrect(R.string.gestures_feedback_swipe_larger)
                }
            }
        }
        return true
    }

    override fun onAccessibilityGesture(gesture: Gesture) {
        when{
            this.gesture == gesture -> {
                correct()
            }
            gesture.directions.isNotEmpty() -> {
                onSwipe(gesture.directions)
            }else -> {
                incorrect(R.string.gestures_feedback_swipe)
            }
        }
    }

    fun onSwipe(directions: Array<Direction>){
        swiped = true

        val fingers = directions.map{it.fingers}.average().toInt()
        Log.d(TAG,"onSwipe: ${directions.joinToString { it.toString() }}, fingers: $fingers")

        when{
            fingers != gesture.fingers -> {
                incorrect(R.string.gestures_feedback_fingers, gesture.fingers, fingers)
            }
            directions.contentEquals(gesture.directions) -> {
                correct()
            }else->{
                incorrect(R.string.gestures_feedback_swipe_directions, Direction.feedback(context, directions))
            }
        }
    }

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener(){
        private val THRESHOLD = 15
        private var path = arrayListOf<Direction>()

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            Log.d(TAG, "onScroll, distanceX: $distanceX, distanceY: $distanceY")

            var direction = Direction.UNKNOWN
            when{
                distanceX > THRESHOLD -> {
                    direction = Direction.LEFT
                }
                distanceX < -THRESHOLD -> {
                    direction = Direction.RIGHT
                }
                distanceY > THRESHOLD -> {
                    direction = Direction.UP
                }
                distanceY < -THRESHOLD -> {
                    direction = Direction.DOWN
                }
            }

            if(direction != Direction.UNKNOWN){
                direction.fingers = e2.pointerCount ?: 1
                if(ScreenReaderService.isEnabled(context)){
                    direction.fingers++
                }
                if(path.isEmpty()){
                    path.add(direction)
                    Log.d(TAG, "Direction: $direction, fingers: ${direction.fingers}")
                }else{
                    path.lastOrNull()?.let { lastDirection ->
                        if(direction != lastDirection){
                            path.add(direction)
                            Log.d(TAG, "Direction: $direction, fingers: ${direction.fingers}")
                        }
                    }
                }
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            Log.d(TAG, "onFling, velocityX: $velocityX, velocityY: $velocityY")

            if(path.isNotEmpty()){
                onSwipe(path.toTypedArray())
            }
            path.clear()
            return super.onFling(e1, e2, velocityX, velocityY)
        }
    })
}