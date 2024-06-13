package com.example.screenarrator.extensions

inline fun <reified T> className(): String {
    return T::class.java.name
}