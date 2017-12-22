package com.nicholasdoglio.notes.ui.list

import android.arch.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.NoteDatabase

/**
 * @author Nicholas Doglio
 */
class NoteListViewModel(private val database: NoteDatabase) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        //clear out any subscriptions
    }
}