package com.nicholasdoglio.notes.ui.common

import android.support.v4.app.FragmentTransaction
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.MainActivity
import com.nicholasdoglio.notes.ui.about.AboutFragment
import com.nicholasdoglio.notes.ui.about.libs.LibsFragment
import com.nicholasdoglio.notes.ui.list.NoteListFragment
import com.nicholasdoglio.notes.ui.note.NoteFragment
import com.nicholasdoglio.notes.util.Const
import com.nicholasdoglio.notes.util.hideKeyboard
import com.nicholasdoglio.notes.util.showFragment
import javax.inject.Inject

class NavigationController
@Inject
constructor(private val mainActivity: MainActivity) : Navigation { //How do I test this class?
    private val containerId: Int = R.id.fragmentContainer

    override fun openList() {
        mainActivity.hideKeyboard()
        mainActivity.showFragment(
            NoteListFragment(),
            Const.noteListFragmentId,
            Const.noteListFragmentId,
            FragmentTransaction.TRANSIT_FRAGMENT_CLOSE,
            containerId,
            false
        )
    }

    override fun openNote(id: Long) {
        mainActivity.showFragment(
            NoteFragment.create(id),
            Const.noteFragmentId,
            Const.noteFragmentId,
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
            containerId
        )
    }

    override fun openAbout() {
        mainActivity.showFragment(
            AboutFragment(),
            Const.aboutFragmentId,
            Const.aboutFragmentId,
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
            containerId
        )
    }

    override fun openLibs() {
        mainActivity.showFragment(
            LibsFragment(),
            Const.libsFragmentId,
            Const.libsFragmentId,
            FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
            containerId
        )
    }
}