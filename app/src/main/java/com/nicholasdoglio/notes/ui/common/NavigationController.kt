package com.nicholasdoglio.notes.ui.common

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import com.nicholasdoglio.notes.MainActivity
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.list.NoteListFragment
import com.nicholasdoglio.notes.ui.note.NoteFragment
import javax.inject.Inject

class NavigationController
@Inject
constructor(mainActivity: MainActivity) {
    private val containerId: Int
    private val fragmentManager: FragmentManager

    init {
        this.containerId = R.id.fragmentContainer
        this.fragmentManager = mainActivity.supportFragmentManager
    }

    //ALL THE TRANSITIONS

    fun openList() {
        val notesList = NoteListFragment()
        fragmentManager.beginTransaction()
                .replace(containerId, notesList)
                .commit()
    }

    fun openNote(id: Long = 0) {
        val note = NoteFragment()
        fragmentManager.beginTransaction()
                .replace(containerId, note)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()

        //Called when you a note is clicked
    }
}

