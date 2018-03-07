package com.nicholasdoglio.notes.ui.list

import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import com.nicholasdoglio.notes.data.model.note.Note
import com.nicholasdoglio.notes.data.repo.NoteRepository
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteListViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    val notesList =
        LivePagedListBuilder<Int, Note>(noteRepository.getAllNotes(), 10).build()

    fun deleteNote(note: Note) =
        noteRepository.deleteNote(note) //Implement with Swipe to delete

    fun checkForNotes() = noteRepository.getNumberOfNotes()
}