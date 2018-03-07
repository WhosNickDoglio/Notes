package com.nicholasdoglio.notes.data.repo

import android.arch.paging.DataSource
import com.nicholasdoglio.notes.data.local.NoteDatabase
import com.nicholasdoglio.notes.data.model.note.Note
import io.reactivex.Single
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDatabase: NoteDatabase) :
    Repository {

    override fun saveNote(note: Note) = noteDatabase.noteDao().saveNote(note)

    override fun deleteNote(note: Note) = noteDatabase.noteDao().deleteNote(note)

    override fun updateNote(note: Note) = noteDatabase.noteDao().updateNote(note)

    override fun getNumberOfNotes(): Single<Int> = noteDatabase.noteDao().getNumberOfNotes()

    override fun getNote(id: Long): Single<Note> = noteDatabase.noteDao().getNote(id)

    override fun getAllNotes(): DataSource.Factory<Int, Note> = noteDatabase.noteDao().getAllNotes()
}