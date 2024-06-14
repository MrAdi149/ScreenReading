package com.example.screenarrator.widgets

import android.text.SpannableString
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.example.screenarrator.R

abstract class ToolbarActivity : BaseActivity() {

    var toolbar: Toolbar? = null

    abstract fun getToolbarTitle(): SpannableString?

    override fun onViewCreated() {
        super.onViewCreated()

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeActionContentDescription(R.string.action_back)

        title = getToolbarTitle()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackSelected()
        }

        return super.onOptionsItemSelected(item)
    }

    open fun onBackSelected(){

    }
}