package com.nicholasdoglio.notes.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author Nicholas Doglio
 */
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
    }

    @After
    fun tearDown() {
        noteDatabase.close()
    }

    @Test
    fun insertData() {
        noteDatabase.noteDao().createNote(firstNote)

        val note = noteDatabase.noteDao().getNote(1).blockingGet()

        assertEquals(note.id, firstNote.id)
    }
}