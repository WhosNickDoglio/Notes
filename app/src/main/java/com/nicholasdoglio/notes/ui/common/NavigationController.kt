package com.nicholasdoglio.notes.ui.common

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.nicholasdoglio.notes.MainActivity
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.about.AboutFragment
import com.nicholasdoglio.notes.ui.list.NoteListFragment
import com.nicholasdoglio.notes.ui.note.NoteFragment
import javax.inject.Inject

class NavigationController
@Inject
constructor(mainActivity: MainActivity) {
    private val containerId: Int = R.id.fragmentContainer
    private val fragmentManager: FragmentManager = mainActivity.supportFragmentManager


    //I want to change the transitions, find something that makes the toolbar look less weird
    fun openList() {
        //Transitions work great on Emulator but are awful on actual devices  ¯\_(ツ)_/¯
        val notesList = NoteListFragment()
        fragmentManager.beginTransaction()
                .replace(containerId, notesList)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .setReorderingAllowed(true)
                .commit()
    }

    fun openNote(id: Long = 0) {
        val note = NoteFragment.create(id)
        fragmentManager.beginTransaction()
                .replace(containerId, note)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .commit()
    }

    fun openAbout() {
        val about = AboutFragment()
        about.show(fragmentManager, "AboutFrag")
    }
}