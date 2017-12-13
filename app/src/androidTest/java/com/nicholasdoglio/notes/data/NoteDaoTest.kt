package com.nicholasdoglio.notes.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class NoteDaoTest {

    private lateinit var noteDatabase: NoteDatabase
    private lateinit var testNote: Note

    @Before
    fun setUp() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                NoteDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        testNote = Note(1, "test note title", "test note contents")
    }

    @After
    fun tearDown() {
        noteDatabase.close()
    }

    @Test
    fun insertNote() {
        noteDatabase.noteDao().createNote(testNote)

        val retrievedNote = noteDatabase.noteDao().getNote(1)

        Assert.assertEquals(retrievedNote.noteId, testNote.noteId)
        Assert.assertEquals(retrievedNote.title, testNote.title)
        Assert.assertEquals(retrievedNote.contents, testNote.contents)
    }

    @Test
    fun getNote() {
        val retrievedNote = noteDatabase.noteDao().getNote(1)

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
