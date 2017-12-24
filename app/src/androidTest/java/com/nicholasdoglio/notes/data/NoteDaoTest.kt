package com.nicholasdoglio.notes.data

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var noteDatabase: NoteDatabase
    private val firstTestNote: Note = Note(1, "first note title", "first note contents")
    private val secondTestNote: Note = Note(2, "second note title", "second note contents")


    @Before
    fun setUp() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                NoteDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        noteDatabase.noteDao().createNote(firstTestNote)
        noteDatabase.noteDao().createNote(secondTestNote)
    }


    @After
    fun closeDatabase() {
        noteDatabase.close()
    }

    @Test
    fun insertNote() {
        val thirdTestNote = Note(3, "third note title", "third note contents")

        noteDatabase.noteDao().createNote(thirdTestNote)

        val retrievedNote = noteDatabase.noteDao().getNote(3)

        assertEquals(retrievedNote.noteId, thirdTestNote.noteId)
        assertEquals(retrievedNote.title, thirdTestNote.title)
        assertEquals(retrievedNote.contents, thirdTestNote.contents)
    }

    @Test
    fun getNote() {
        val retrievedNote = noteDatabase.noteDao().getNote(1)

        assertEquals(retrievedNote.noteId, firstTestNote.noteId)
        assertEquals(retrievedNote.title, firstTestNote.title)
        assertEquals(retrievedNote.contents, firstTestNote.contents)
    }

    @Test
    fun deleteNote() {
        noteDatabase.noteDao().deleteNote(firstTestNote)

        assertEquals(noteDatabase.noteDao().getNumberOfItems(), 1)
    }

    @Test
    fun getAllNotes() {
        //Look at AAC Github samples to figure out how to best test a PagedList
    }
}