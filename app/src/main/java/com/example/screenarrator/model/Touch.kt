package com.example.screenarrator.model

data class Touch (
    val x: Int,
    val y: Int,
    val taps: Int = 1,
    val longPress: Boolean = false
)