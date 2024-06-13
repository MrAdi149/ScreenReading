package com.example.screenarrator.tabs.talkback

import com.example.screenarrator.R
import com.example.screenarrator.widgets.ListFragment
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.example.screenarrator.adapters.headerAdapterDelegate
import com.example.screenarrator.adapters.textResourceAdapterDelegate
import com.example.screenarrator.model.Header

class TalkBackFragment : ListFragment(){

    override var decoration: Boolean = false

    override val adapter = ListDelegationAdapter(
        headerAdapterDelegate(),
        textResourceAdapterDelegate(),
    )

    override val items = listOf(
        R.string.talkback_description,
        Header(R.string.talkback_section_1),
        R.string.talkback_section_1_paragraph_1,
        R.string.talkback_section_1_paragraph_2,
        R.string.talkback_section_1_paragraph_3,
        Header(R.string.talkback_section_2),
        R.string.talkback_section_2_paragraph_1,
        R.string.talkback_section_2_paragraph_2,
        R.string.talkback_section_2_paragraph_3,
        Header(R.string.talkback_section_3),
        R.string.talkback_section_3_paragraph_1,
        R.string.talkback_section_3_paragraph_2,
        R.string.talkback_section_3_paragraph_3,
        Header(R.string.talkback_section_4),
        R.string.talkback_section_4_paragraph_1,
        R.string.talkback_section_4_paragraph_2,
        Header(R.string.talkback_section_5),
        R.string.talkback_section_5_paragraph_1,
        R.string.talkback_section_5_paragraph_2,
        R.string.talkback_section_5_paragraph_3,
        R.string.talkback_section_5_paragraph_4,
        Header(R.string.talkback_section_6),
        R.string.talkback_section_6_paragraph_1,
        R.string.talkback_section_6_paragraph_2,
        R.string.talkback_section_6_paragraph_3,
    )
}