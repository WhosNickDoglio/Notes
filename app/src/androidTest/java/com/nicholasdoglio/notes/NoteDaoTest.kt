/*
 * MIT License
 *
 * Copyright (c) 2019 Nicholas Doglio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.nicholasdoglio.notes

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholasdoglio.notes.data.local.NoteDatabase
import com.nicholasdoglio.notes.data.model.Note
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var noteDatabase: NoteDatabase
    private val firstTestNote: Note =
        Note(1, "first note title", "first note contents")
    private val secondTestNote: Note =
        Note(2, "second note title", "second note contents")

    @Before
    fun setUp() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
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
        val thirdTestNote =
            Note(3, "third note title", "third note contents")

        noteDatabase.noteDao().saveNote(thirdTestNote)

        val retrievedNote = noteDatabase.noteDao().note(3).blockingGet()

        assertEquals(retrievedNote, thirdTestNote)
    }

    @Test
    fun deleteNote() {
        noteDatabase.noteDao().deleteNote(firstTestNote)

        assert(noteDatabase.noteDao().countOfNotes().test().values()[0] == 1)
    }

    @Test
    fun updateNote() {
        val retrievedNote = noteDatabase.noteDao().note(1).blockingGet()

        noteDatabase.noteDao().updateNote(
            Note(
                1,
                "Updated note",
                "updated contents"
            )
        )

        val updatedNote = noteDatabase.noteDao().note(1).blockingGet()

        assertEquals(updatedNote.id, 1)
        assertEquals(updatedNote.title, "Updated note")
        assertEquals(updatedNote.contents, "updated contents")

        assertEquals(updatedNote.id, retrievedNote.id)
        assert(retrievedNote.title != updatedNote.title)
        assert(retrievedNote.contents != updatedNote.contents)
    }

    @Test
    fun getNumberOfItems() {
        assert(noteDatabase.noteDao().countOfNotes().test().values()[0] == 2)
    }

    @Test
    fun getNote() {
        val retrievedNote = noteDatabase.noteDao().note(1).blockingGet()
        assertEquals(firstTestNote, retrievedNote)

        noteDatabase.noteDao().note(1)
            .test()
            .assertComplete()
    }
}