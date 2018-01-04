package com.nicholasdoglio.notes.ui.list

import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import com.nicholasdoglio.notes.data.Note
import com.nicholasdoglio.notes.data.NoteDatabase
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteListViewModel @Inject constructor(
        database: NoteDatabase
) : ViewModel() {

    val notesList = LivePagedListBuilder<Int, Note>(
            database.noteDao()
                    .getAllNotes(),
            10).build()
}