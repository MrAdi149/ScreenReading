package com.example.screenarrator.tabs.actions

import android.app.Activity
import android.content.Intent
import com.example.screenarrator.R
import com.example.screenarrator.adapters.headerAdapterDelegate
import com.example.screenarrator.adapters.textAdapterDelegate
import com.example.screenarrator.adapters.textResourceAdapterDelegate
import com.example.screenarrator.adapters.trainingAdapterDelegate
import com.example.screenarrator.extensions.doSetAction
import com.example.screenarrator.extensions.requestReview
import com.example.screenarrator.extensions.showDialog
import com.example.screenarrator.helpers.Accessibility
import com.example.screenarrator.model.Action
import com.example.screenarrator.model.Header
import com.example.screenarrator.widgets.ListFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class ActionsFragment: ListFragment() {

    override val items = listOf(
        R.string.actions_description,
        Header(R.string.actions_header_navigation),
        Action.HEADINGS,
        Action.LINKS,
        Header(R.string.actions_header_text),
        Action.SELECT,
        Action.COPY,
        Action.PASTE
    )

    override val adapter = ListDelegationAdapter(
        textResourceAdapterDelegate(),
        headerAdapterDelegate(),
        trainingAdapterDelegate<Action> { action ->
            onActionClicked(action)
        }
    )

    private fun onActionClicked(action: Action) {
        context?.let { context ->
            if (!Accessibility.screenReader(context)) {
                context.showDialog(R.string.actions_talkback_disabled_title, R.string.actions_talkback_disabled_message)
                return
            }

            startActivity<ActionActivity>(REQUEST_CODE) {
                doSetAction(action)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            adapter.notifyDataSetChanged()
            activity?.requestReview()
        }
    }

    companion object {
        private val REQUEST_CODE = 1337
    }
}