package com.example.screenarrator

import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.screenarrator.helpers.Accessibility
import com.example.screenarrator.helpers.Events
import com.example.screenarrator.widgets.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity :  BaseActivity(){
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onViewCreated() {
        super.onViewCreated()

        val hostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        val navController = hostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.tab_talkback,
                R.id.tab_gestures,
                R.id.tab_actions,
                R.id.tab_more
            )
        )

        toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            val titleId = when(destination.id){
                R.id.tab_talkback -> R.string.talkback_title
                R.id.tab_gestures -> R.string.gestures_title
                R.id.tab_actions -> R.string.actions_title
                R.id.tab_more -> R.string.more_title
                else -> R.string.more_title
            }
            setTitle(titleId)
        }
        events.property(Events.Property.screenreader, Accessibility.screenReader(this))
    }

}