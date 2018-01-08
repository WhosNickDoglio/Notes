package com.nicholasdoglio.notes.ui.list

import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import com.nicholasdoglio.notes.data.local.NoteDatabase
import com.nicholasdoglio.notes.data.model.note.Note
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