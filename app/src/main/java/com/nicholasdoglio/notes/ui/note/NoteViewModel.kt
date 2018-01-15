package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.local.NoteDatabase
import com.nicholasdoglio.notes.data.model.note.Note
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteViewModel @Inject constructor(private val noteDatabase: NoteDatabase) : ViewModel() {

    private val titleChanged: BehaviorSubject<String> = BehaviorSubject.create()
    private val contentsChanged: BehaviorSubject<String> = BehaviorSubject.create()
    private var currentTitle: String = ""
    private var currentContent: String = ""
    private var currentId: Long = 0

    fun title(title: String) {
        titleChanged.onNext(title)
        currentTitle = title

    }

    fun contents(content: String) {
        contentsChanged.onNext(content)
        currentContent = content
    }

    fun id(id: Long) {
        currentId = id
    }

    fun start(id: Long): Single<Note> = noteDatabase.noteDao().getNote(id)

    fun deleteNote(note: Note) = noteDatabase.noteDao().deleteNote(note)

    private fun createNote(): Note = Note(currentId, currentTitle, currentContent)

    private fun checkIfNoteAlreadyExists(id: Long): Boolean = id > 0


    fun saveNote(id: Long): Completable {
        return Single.just(checkIfNoteAlreadyExists(id))
                .map { saveOrUpdate(it) }
                .toCompletable()
    }

    private fun saveOrUpdate(bool: Boolean) {
        if (bool) {
            noteDatabase.noteDao().updateNote(createNote())
        } else {
            noteDatabase.noteDao().saveNote(createNote())
        }
    }

    override fun onCleared() {
        super.onCleared()
        titleChanged.onComplete()
        contentsChanged.onComplete()
    }
}