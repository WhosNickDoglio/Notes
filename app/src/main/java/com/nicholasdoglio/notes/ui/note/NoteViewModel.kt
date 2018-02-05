package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nicholasdoglio.notes.data.local.NoteDatabase
import com.nicholasdoglio.notes.data.model.note.Note
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteViewModel @Inject constructor(private val noteDatabase: NoteDatabase) : ViewModel() {

    private val titleSubject: BehaviorRelay<String> = BehaviorRelay.create()
    private val contentSubject: BehaviorRelay<String> = BehaviorRelay.create()
    private val idSubject: BehaviorRelay<Long> = BehaviorRelay.create()
    private val noteSubject: BehaviorRelay<Note> = BehaviorRelay.create()

    fun title(title: String) = titleSubject.accept(title)

    fun contents(content: String) = contentSubject.accept(content)

    fun id(id: Long) = idSubject.accept(id)

    fun note(note: Note) = noteSubject.accept(note)

    fun start(id: Long): Single<Note> = noteDatabase.noteDao().getNote(id)

    fun deleteNote(note: Note): Completable = Completable.fromAction {
        noteDatabase.noteDao().deleteNote(note)
    }

    fun saveNote(id: Long): Completable = Single.just(id > 0)
        .doOnSuccess {
            Timber.d(
                "Note Saved - ID: %s Title: %s, Content: %s ",
                id,
                titleSubject.value,
                contentSubject.value
            )
        }
        .map { saveOrUpdate(it) }
        .toCompletable()


    private fun createNote(): Note = Note(
        idSubject.value,
        titleSubject.value,
        contentSubject.value
    )

    private fun saveOrUpdate(doesNoteAlreadyExist: Boolean) {
        when (doesNoteAlreadyExist) {
            true -> noteDatabase.noteDao().updateNote(createNote())
            false -> noteDatabase.noteDao().saveNote(createNote())
        }
    }
}