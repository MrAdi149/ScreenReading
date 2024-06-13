package com.example.screenarrator.views.actions

import android.content.Context
import com.example.screenarrator.R
import com.example.screenarrator.model.Action
import com.example.screenarrator.views.TrainingField

class SelectionActionView(context: Context) : ActionView(
    context,
    Action.SELECT,
    R.layout.action_selection
), TrainingField.OnSelectionChangedListener {

    private lateinit var trainingField: TrainingField
    init {
        trainingField.callback = this
    }

    override fun onSelectionChanged(start: Int, end: Int) {
        if ((end - start) > 1){
            correct()
        }
    }
}