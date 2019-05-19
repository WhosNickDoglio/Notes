package com.nicholasdoglio.notes.ui.note

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nicholasdoglio.notes.data.model.Note
import com.nicholasdoglio.notes.data.repo.NoteRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    private val titleRelay: BehaviorRelay<String> = BehaviorRelay.create()
    private val contentsRelay: BehaviorRelay<String> = BehaviorRelay.create()
    private val noteRelay: BehaviorRelay<Note> = BehaviorRelay.create()

    fun title(title: String) = titleRelay.accept(title)

    fun contents(content: String) = contentsRelay.accept(content)

    fun note(note: Note) = noteRelay.accept(note)

    fun start(id: Long): Maybe<Note> = noteRepository.note(id)

    fun deleteNote(): Completable =
        noteRepository.deleteNote(noteRelay.value ?: throw IllegalStateException("Note doesn't exist!"))

    fun saveNote(): Completable =
        Single.just(Note(noteRelay.value?.id ?: 0, titleRelay.value ?: "", contentsRelay.value ?: ""))
            .flatMapCompletable { if (it.id > 0) noteRepository.updateNote(it) else noteRepository.saveNote(it) }
}
