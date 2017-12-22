package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.NoteDatabase

/**
 * @author Nicholas Doglio
 */
class NoteViewModel(private val database: NoteDatabase) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
    }
}