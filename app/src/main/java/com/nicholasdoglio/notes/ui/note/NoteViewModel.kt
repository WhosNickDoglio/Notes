package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.local.NoteDatabase
import com.nicholasdoglio.notes.data.note.Note
import io.reactivex.Single
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

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    //I think I should be able to remove this and chain everything in Note Fragment

    fun updateTitle(title: String) {
        noteTitle = title
    }

    fun updateContent(content: String) {
        noteContent = content
    }

    fun start(id: Long): Single<Note> =
            database.noteDao().getNote(id)

    fun saveNote(): Boolean {
        if (noteHasContents()) {
            database.noteDao().saveNote(createNote())
            return true
        } else {
            return false
        }

//        compositeDisposable += database.noteDao()
//                .checkIfNoteAlreadyExists(
//                        noteTitle,
//                        noteContent)
//                .subscribeOn(Schedulers.io())
//                .subscribe { it ->
//                    when (it) {
//                        true ->  database.noteDao().updateNote(createNote())
//                        false -> database.noteDao().saveNote(createNote())
//                    }
//                }

//        if (updateNoteBool) {
//            return database.noteDao().updateNote(createNote())
//        } else {
//            return database.noteDao().saveNote(createNote())
//        }

    }

    fun getNote(id: Long) = database.noteDao().getNote(id)

    fun deleteNote(note: Note) {
        //I need to check if this note is in the DB, if so delete from there,
        // if not just discard what's currently here
        database.noteDao().deleteNote(note)
    }

    private fun createNote(): Note = Note(0, noteTitle, noteContent)

    private fun noteHasContents(): Boolean {
        //can I convert this to a when statement?
        if (noteTitle == "" && noteContent !== "") {
            //I need a title error!
            return false
        } else if (noteContent == "" && noteTitle !== "") {
            //I need contents error
            return false
        } else return !(noteTitle == "" && noteContent == "")
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}