package com.nicholasdoglio.notes.data

import android.arch.persistence.room.Room
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class NoteDaoTestRobo {

    private lateinit var noteDatabase: NoteDatabase
    private lateinit var firstNote: Note
    private lateinit var secondNote: Note
    private lateinit var thirdNote: Note

    @Before
    fun setUp() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
                RuntimeEnvironment.application.applicationContext,
                NoteDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        firstNote = Note(1, "first note title", "first note contents")
        secondNote = Note(2, "second note title", "second note contents")
        thirdNote = Note(3, "third note title", "third note contents")

        noteDatabase.noteDao().createNote(firstNote)
        noteDatabase.noteDao().createNote(secondNote)
        noteDatabase.noteDao().createNote(thirdNote)
    }

    @After
    fun tearDown() {
        noteDatabase.close()
    }

    @Test
    fun insertNote() {
        val fourthNote = Note(4, "fourth note title", "fourth note contents")

        noteDatabase.noteDao().createNote(fourthNote)

        val retrievedNote = noteDatabase.noteDao().getNote(4)

        Assert.assertEquals(retrievedNote.noteId, fourthNote.noteId)
        Assert.assertEquals(retrievedNote.title, fourthNote.title)
        Assert.assertEquals(retrievedNote.contents, fourthNote.contents)
    }

    @Test
    fun getNote() {
        val retrievedNote = noteDatabase.noteDao().getNote(1)

        Assert.assertEquals(retrievedNote.noteId, firstNote.noteId)
        Assert.assertEquals(retrievedNote.title, firstNote.title)
        Assert.assertEquals(retrievedNote.contents, firstNote.contents)
    }

    @Test
    fun deleteNote() {
        noteDatabase.noteDao().deleteNote(firstNote)
    }


    @Test
    fun deleteAllNotes() {
        noteDatabase.noteDao().deleteAllNotes()
    }

    @Test
    fun getAllNotes() {
        val allNotes = noteDatabase.noteDao().getAllNotes()
    }
}
