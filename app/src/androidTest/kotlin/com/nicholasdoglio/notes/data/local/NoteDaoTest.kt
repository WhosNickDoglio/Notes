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

package com.nicholasdoglio.notes.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nicholasdoglio.notes.data.model.Note
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {
    // TODO try and clean these up with chaining and confirm they are all working correctly

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private lateinit var noteDatabase: NoteDatabase

    private val noteDao: NoteDao by lazy {
        noteDatabase.noteDao
    }

    @Before
    fun setUp() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() {
        noteDatabase.close()
    }

    @Test
    fun give_db_is_empty_when_observing_db_then_return_zero() {
        noteDao
            .observeNumOfNotes
            .test()
            .assertValue(0)
    }

    @Test
    fun given_note_when_insert_note_is_called_then_save_note() {
        val note = Note(1, "Hello", "World")

        noteDao.insertNote(note)
            .test()

        noteDao.findNoteById(note.id)
            .test()
            .assertValue(note)
    }

    @Test
    fun given_note_exists_when_delete_note_is_called_then_delete_note() {
        val first = Note(1, "First Note", "test")
        val second = Note(2, "Second Note", "test")

        noteDao.insertNote(first).test()
        noteDao.insertNote(second).test()

        noteDao.observeNumOfNotes
            .test()
            .assertValue(2)

        noteDao.deleteNote(second).test()

        noteDao.findNoteById(first.id).test().assertValue(first)

        noteDao.observeNumOfNotes.test().assertValue(1)
    }

    @Test
    fun given_note_exists_when_update_is_called_then_update_note() {
        val originalNote = Note(1, "Hello", "World!")

        noteDao.insertNote(originalNote).test()

        noteDao.findNoteById(originalNote.id).test().assertValue(originalNote)

        val updatedNote = Note(1, "Hello", "Earth!")

        noteDao.updateNote(updatedNote).test()

        noteDao.findNoteById(updatedNote.id).test().assertValue(updatedNote)
    }

    @Test
    fun given_notes_when_observing_number_of_notes_then_return_correct_number() {
        val first = Note(1, "First Note", "test")
        val second = Note(2, "Second Note", "test")
        val third = Note(3, "Third Note", "test")

        noteDao.insertNote(first).test()
        noteDao.observeNumOfNotes.test().assertValue(1)

        noteDao.insertNote(second).test()
        noteDao.observeNumOfNotes.test().assertValue(2)

        noteDao.insertNote(third).test()
        noteDao.observeNumOfNotes.test().assertValue(3)

        noteDao.deleteNote(third).test()
        noteDao.observeNumOfNotes.test().assertValue(2)
    }

    @Test
    fun given_note_id_that_exists_when_find_note_called_then_return_note() {
        val first = Note(1, "First Note", "test")
        val second = Note(2, "Second Note", "test")
        val third = Note(3, "Third Note", "test")

        noteDao.insertNote(first).test()
        noteDao.insertNote(second).test()
        noteDao.insertNote(third).test()

        noteDao.findNoteById(first.id).test().assertValue(first)
        noteDao.findNoteById(second.id).test().assertValue(second)
        noteDao.findNoteById(third.id).test().assertValue(third)
    }

    @Test
    fun given_note_id_that_does_not_exist_when_find_note_called_then_return_null() {
        // TODO look into this?
        noteDao.findNoteById(10).test()
            .assertNoValues()
    }
}
