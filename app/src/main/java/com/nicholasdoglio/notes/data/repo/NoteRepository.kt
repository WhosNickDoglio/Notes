package com.nicholasdoglio.notes.data.repo

import com.nicholasdoglio.notes.data.local.NoteDao
import com.nicholasdoglio.notes.data.model.Note
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor(private val noteDao: NoteDao) {

    fun saveNote(note: Note): Completable = Completable.fromAction { noteDao.saveNote(note) }

    fun deleteNote(note: Note): Completable = Completable.fromAction { noteDao.deleteNote(note) }

    fun updateNote(note: Note): Completable = Completable.fromAction { noteDao.updateNote(note) }

    fun countOfNotes(): Flowable<Int> = noteDao.countOfNotes()

    fun note(id: Long): Maybe<Note> = noteDao.note(id)

    fun observeNotes(): Flowable<List<Note>> = noteDao.observeNotes()
}
