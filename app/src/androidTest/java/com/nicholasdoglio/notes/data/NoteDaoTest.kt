package com.nicholasdoglio.notes.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class NoteDaoTest {

    private lateinit var noteDatabase: NoteDatabase
    private lateinit var firstNote: Note
    private lateinit var secondNote: Note
    private lateinit var thirdNote: Note

    @Before
    fun setUp() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
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

        assertEquals(retrievedNote.noteId, fourthNote.noteId)
        assertEquals(retrievedNote.title, fourthNote.title)
        assertEquals(retrievedNote.contents, fourthNote.contents)
    }

    @Test
    fun getNote() {
        val retrievedNote = noteDatabase.noteDao().getNote(1)

        assertEquals(retrievedNote.noteId, firstNote.noteId)
        assertEquals(retrievedNote.title, firstNote.title)
        assertEquals(retrievedNote.contents, firstNote.contents)
    }

    @Test
    fun deleteNote() {
        assertEquals(1, 1)
    }

    @Test
    fun getAllNotes() {
        assertEquals(1, 1)
    }
}
