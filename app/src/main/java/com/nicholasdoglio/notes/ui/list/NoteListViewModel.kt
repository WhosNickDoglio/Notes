package com.nicholasdoglio.notes.ui.list

import android.arch.lifecycle.ViewModel

/**
 * @author Nicholas Doglio
 */
class NoteListViewModel : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        //clear out any subscriptions
    }
}