package com.nicholasdoglio.notes.ui.list

import androidx.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.model.Note
import com.nicholasdoglio.notes.data.repo.NoteRepository
import io.reactivex.Flowable
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteListViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    val notesList: Flowable<List<Note>> = noteRepository.observeNotes()

    val checkForNotes: Flowable<Int> = noteRepository.countOfNotes()
}
