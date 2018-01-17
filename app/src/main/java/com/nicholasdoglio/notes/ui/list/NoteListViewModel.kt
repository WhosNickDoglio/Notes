package com.nicholasdoglio.notes.ui.list

import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import com.nicholasdoglio.notes.data.local.NoteDatabase
import com.nicholasdoglio.notes.data.model.note.Note
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteListViewModel @Inject constructor(private val noteDatabase: NoteDatabase) : ViewModel() {

    val notesList =
        LivePagedListBuilder<Int, Note>(noteDatabase.noteDao().getAllNotes(), 10).build()

    fun deleteNote(note: Note) =
        noteDatabase.noteDao().deleteNote(note) //Implement with Swipe to delete

    fun checkForNotes() = noteDatabase.noteDao().getNumberOfItems()
}