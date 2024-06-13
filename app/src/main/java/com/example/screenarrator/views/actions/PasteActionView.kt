package com.example.screenarrator.views.actions

import android.content.Context
import com.example.screenarrator.R
import androidx.core.widget.addTextChangedListener
import com.example.screenarrator.model.Action
import com.example.screenarrator.views.TrainingField

class PasteActionView(context: Context) : ActionView(
    context,
    Action.PASTE,
    R.layout.action_paste
) {

    private lateinit var trainingField: TrainingField

    init {
        trainingField.addTextChangedListener(beforeTextChanged = { _, _, _, after->
            if (after > 1){
                correct()
            }
        })
    }
}