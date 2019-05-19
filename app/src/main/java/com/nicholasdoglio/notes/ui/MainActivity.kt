package com.nicholasdoglio.notes.ui

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.common.OnBackPressedListener
import com.nicholasdoglio.notes.ui.list.NoteListFragmentDirections
import com.nicholasdoglio.notes.ui.tile.NOTES_TILE_SHORTCUT
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

        val navController = findNavController(R.id.navHostFragment)
        toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))

        if (NOTES_TILE_SHORTCUT == intent.action) {
            navController.navigate(NoteListFragmentDirections.openNote())
        }

        intent.extras?.let {
            if (it.containsKey(NOTES_TILE_SHORTCUT)) {
                navController.navigate(NoteListFragmentDirections.openNote())
            }
        }
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
