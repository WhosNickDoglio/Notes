package com.nicholasdoglio.notes.ui.common

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.MainActivity
import com.nicholasdoglio.notes.ui.about.AboutFragment
import com.nicholasdoglio.notes.ui.about.libs.LibsFragment
import com.nicholasdoglio.notes.ui.list.NoteListFragment
import com.nicholasdoglio.notes.ui.note.NoteFragment
import com.nicholasdoglio.notes.util.UtilFunctions
import javax.inject.Inject

class NavigationController
@Inject
constructor(private val mainActivity: MainActivity) {
    private val containerId: Int = R.id.fragmentContainer
    private val fragmentManager: FragmentManager = mainActivity.supportFragmentManager


    //How do I test this class?

    //I want to change the transitions, find something that makes the toolbar look less weird
    fun openList() {
        UtilFunctions().hideSoftKeyboard(mainActivity.baseContext, mainActivity)
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

    fun openNoteShortcut(id: Long = 0) { //Maybe there's a better way to do this?
        val note = NoteFragment.create(id)
        openList()
        fragmentManager.beginTransaction()
                .replace(containerId, note)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .commit()
    }

    fun openAbout() {
        val about = AboutFragment()
        fragmentManager.beginTransaction()
                .replace(containerId, about)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .commit()
    }

    fun openLibs() {
        val lib = LibsFragment()
        fragmentManager.beginTransaction()
                .replace(containerId, lib)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .commit()
    }
}