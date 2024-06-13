package com.example.screenarrator.tabs.more

import androidx.core.app.ShareCompat
import com.example.screenarrator.R
import com.example.screenarrator.adapters.headerAdapterDelegate
import com.example.screenarrator.adapters.textResourceAdapterDelegate
import com.example.screenarrator.adapters.itemAdapterDelegate
import com.example.screenarrator.extensions.openWebsite
import com.example.screenarrator.model.Header
import com.example.screenarrator.model.Topic
import com.example.screenarrator.widgets.ListFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class MoreFragment : ListFragment(){

    override val adapter = ListDelegationAdapter(
        textResourceAdapterDelegate(),
        headerAdapterDelegate(),
        itemAdapterDelegate<Topic>{ topic ->
            onTopicClicked(topic)
        }
    )

    override val items = listOf(
        R.string.more_description,
        Header(R.string.more_actions),
        Topic.RATING,
        Topic.SHARE,
        Topic.WEBSITE,
        Topic.SOURCE,
        Header(R.string.more_partners),
        Topic.STICHING_APPT,
        Topic.ABRA,
        Topic.SIDN_FONDS
    )

    private fun onTopicClicked(topic: Topic) {
        activity?.let { activity ->
            if (topic == Topic.SHARE) {
                ShareCompat.IntentBuilder.from(activity)
                    .setType("text/plain")
                    .setChooserTitle(topic.title(activity))
                    .setText(topic.url(activity))
                    .startChooser()
            } else {
                activity.openWebsite(topic.url)
            }
        }
    }
}