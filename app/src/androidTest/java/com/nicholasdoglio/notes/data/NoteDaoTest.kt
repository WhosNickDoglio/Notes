package com.nicholasdoglio.notes.data

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.nicholasdoglio.notes.data.local.NoteDatabase
import com.nicholasdoglio.notes.data.model.note.Note
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var noteDatabase: NoteDatabase
    private val firstTestNote: Note = Note(1, "first note title", "first note contents")
    private val secondTestNote: Note = Note(2, "second note title", "second note contents")


    @Before
    fun setUp() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()

        noteDatabase.noteDao().saveNote(firstTestNote)
        noteDatabase.noteDao().saveNote(secondTestNote)
    }


    @After
    fun closeDatabase() {
        noteDatabase.close()
    }

    @Test
    fun saveNote() {
        val thirdTestNote = Note(3, "third note title", "third note contents")

        noteDatabase.noteDao().saveNote(thirdTestNote)

        val retrievedNote = noteDatabase.noteDao().getNote(3).blockingGet()

        assertEquals(retrievedNote, thirdTestNote)
    }

    @Test
    fun deleteNote() {
        noteDatabase.noteDao().deleteNote(firstTestNote)

        assert(noteDatabase.noteDao().getNumberOfItems().blockingGet() == 1)
    }

    @Test
    fun updateNote() {
        val retrievedNote = noteDatabase.noteDao().getNote(1).blockingGet()

        noteDatabase.noteDao().updateNote(Note(1, "Updated note", "updated contents"))

        val updatedNote = noteDatabase.noteDao().getNote(1).blockingGet()


        assertEquals(updatedNote.id, 1)
        assertEquals(updatedNote.title, "Updated note")
        assertEquals(updatedNote.contents, "updated contents")


        assertEquals(updatedNote.id, retrievedNote.id)
        assert(retrievedNote.title != updatedNote.title)
        assert(retrievedNote.contents != updatedNote.contents)
    }

    @Test
    fun getNumberOfItems() {
        assert(noteDatabase.noteDao().getNumberOfItems().blockingGet() == 2)
    }


    @Test
    fun getNote() {
        val retrievedNote = noteDatabase.noteDao().getNote(1).blockingGet()
        assertEquals(firstTestNote, retrievedNote)

        noteDatabase.noteDao().getNote(1)
            .test()
            .assertComplete()
    }

    @Test
    fun getAllNotes() {
        //Look at AAC Github samples to figure out how to best test a PagedList
    }
}