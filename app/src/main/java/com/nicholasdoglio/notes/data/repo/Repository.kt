package com.nicholasdoglio.notes.data.repo

import android.arch.paging.DataSource
import com.nicholasdoglio.notes.data.model.note.Note
import io.reactivex.Single

interface Repository {

    fun saveNote(note: Note)

    fun deleteNote(note: Note)

    fun updateNote(note: Note)

    fun getNumberOfNotes(): Single<Int>

    fun getNote(id: Long): Single<Note>

    fun getAllNotes(): DataSource.Factory<Int, Note>

}