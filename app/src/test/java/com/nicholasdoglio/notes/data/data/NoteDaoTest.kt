package com.nicholasdoglio.notes.data.data

import android.arch.persistence.room.Room
import com.nicholasdoglio.notes.data.Note
import com.nicholasdoglio.notes.data.NoteDatabase
import junit.framework.Assert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * @author Nicholas Doglio
 */
@RunWith(RobolectricTestRunner::class)
class NoteDaoTest {

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
    }

    @After
    fun tearDown() {
        noteDatabase.close()
    }

    @Test
    fun insertData() {
        noteDatabase.noteDao().createNote(firstNote)

        val note = noteDatabase.noteDao().getNote(1).blockingGet()

        Assert.assertEquals(note.id, firstNote.id)
    }
}