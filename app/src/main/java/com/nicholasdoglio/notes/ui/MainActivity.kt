package com.nicholasdoglio.notes.ui

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.common.OnBackPressedListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Nicholas Doglio
 */
class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(
            findNavController(R.id.navHostFragment),
            AppBarConfiguration(findNavController(R.id.navHostFragment).graph)
        )
    }

    override fun onBackPressed() {
        val currentFragment = navHostFragment.childFragmentManager.fragments[0]
        val controller = findNavController(R.id.navHostFragment)

        if (currentFragment is OnBackPressedListener) {
            (currentFragment as OnBackPressedListener).doBack()
        } else if (!controller.popBackStack()) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
