package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.Note
import com.nicholasdoglio.notes.data.NoteDatabase
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteViewModel @Inject constructor(
        private val database: NoteDatabase
) : ViewModel() {

    private var noteTitle: String = ""
    private var noteContent: String = ""
    private var updateNoteBool: Boolean = false
    private var compositeDisposable: CompositeDisposable

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun updateTitle(title: String) {
        noteTitle = title
    }

    fun updateContent(content: String) {
        noteContent = content
    }

    fun saveNote() {
//        compositeDisposable += database.noteDao()
//                .checkIfNoteAlreadyExists(
//                        createNote().title,
//                        createNote().contents)
//                .subscribeOn(Schedulers.io())
//                .subscribe { it ->
//                    when (it) {
//                        true -> updateNoteBool = true
//                        false -> updateNoteBool = false
//                    }
//                }

        if (updateNoteBool) {
            return database.noteDao().updateNote(createNote())
        } else {
            database.noteDao().saveNote(createNote())
        }

    }

    fun deleteNote() {
        //I need to check if this note is in the DB, if so delete from there,
        // if not just discard what's currently here
//        database.noteDao().deleteNote()
    }

    private fun createNote(): Note = Note(0, noteTitle, noteContent)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}