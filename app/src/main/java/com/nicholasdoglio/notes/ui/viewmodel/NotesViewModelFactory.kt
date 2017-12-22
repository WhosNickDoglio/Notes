package com.nicholasdoglio.notes.ui.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.nicholasdoglio.notes.data.NoteDatabase
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NotesViewModelFactory(@Inject val noteDatabase: NoteDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}