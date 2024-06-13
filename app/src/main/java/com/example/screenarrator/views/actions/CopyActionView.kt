package com.example.screenarrator.views.actions

import android.content.ClipboardManager
import android.content.Context
import com.example.screenarrator.R
import com.example.screenarrator.model.Action
import com.example.screenarrator.views.TrainingField


class CopyActionView(context: Context) : ActionView(
    context,
    Action.COPY,
    R.layout.action_copy
) {

    private lateinit var trainingField: TrainingField

    init {
        val clipBoard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        clipBoard.addPrimaryClipChangedListener {
            clipBoard.primaryClip?.let { clip ->
                if(clip.itemCount > 0){
                    val text = clip.getItemAt(0).text

                    if (trainingField.text.toString().contains(text, false)){
                        correct()
                    } else {
                        incorrect(R.string.action_copy_incorrect)
                    }
                }
            }
        }
    }
}