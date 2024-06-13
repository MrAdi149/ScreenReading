package com.example.screenarrator.helpers

import android.os.Bundle
import com.example.screenarrator.extensions.identifier
import com.google.firebase.analytics.FirebaseAnalytics

class Events(private val firebaseAnalytics: FirebaseAnalytics) {

    enum class Property {
        screenreader
    }

    fun property(property: Property, value: String) {
        firebaseAnalytics.setUserProperty(property.identifier, value)
    }

    fun property(property: Property, value: Boolean) {
        property(property, if (value) "1" else "0")
    }

    enum class Category {
        action_completed,
        gesture_completed
    }

    fun log(category: Category, identifier: String, value: Int? = null) {
        print("Log event, category: $category, identifier: $identifier, value: $value")

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, identifier)

        if (value != null) {
            bundle.putInt(FirebaseAnalytics.Param.VALUE, value)
        }

        firebaseAnalytics.logEvent(category.identifier, bundle)
    }
}