package com.example.screenarrator.extensions

import java.util.*

inline val <reified T : Enum<T>> T.identifier
    get() = toString().toLowerCase(Locale.ROOT)