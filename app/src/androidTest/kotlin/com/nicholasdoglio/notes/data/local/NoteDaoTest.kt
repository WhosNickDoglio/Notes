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
import com.google.common.truth.Truth.assertThat
import com.nicholasdoglio.notes.data.model.Note
import com.nicholasdoglio.notes.waitForValue
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    @Rule @JvmField
    val instantRule = InstantTaskExecutorRule()

    private lateinit var noteDatabase: NoteDatabase

    private lateinit var noteDao: NoteDao

    @Before
    fun setUp() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()

        noteDao = noteDatabase.noteDao()
    }

    @After
    fun closeDatabase() {
        noteDatabase.close()
    }

    @Test
    fun empty_database_successful() {

        val count = noteDao.countOfNotes.waitForValue

        assertThat(count)
            .isEqualTo(0)
    }

    @Test
    fun save_note_successful() = runBlockingTest {

        val note = Note(1, "Hello", "World")

        noteDao.insertNote(note)

        val retrievedNote = noteDao.note(note.id).waitForValue

        assertThat(note).isEqualTo(retrievedNote)
    }

    @Test
    fun delete_note_successful() = runBlockingTest {
        val first = Note(1, "First Note", "test")
        val second = Note(2, "Second Note", "test")

        noteDao.insertNote(first)
        noteDao.insertNote(second)

        val count = noteDao.countOfNotes.waitForValue

        assertThat(count).isEqualTo(2)

        noteDao.deleteNote(second)

        val retrievedNote = noteDao.note(first.id).waitForValue

        assertThat(first).isEqualTo(retrievedNote)

        val secondCount = noteDao.countOfNotes.waitForValue

        assertThat(secondCount).isEqualTo(1)
    }

    @Test
    fun update_note_successful() = runBlockingTest {
        val originalNote = Note(1, "Hello", "World!")

        noteDao.insertNote(originalNote)

        val retrievedOriginalNote = noteDao.note(originalNote.id).waitForValue

        assertThat(originalNote).isEqualTo(retrievedOriginalNote)

        val updatedNote = Note(1, "Hello", "Earth!")

        noteDao.updateNote(updatedNote)

        val retrievedUpdatedNote = noteDao.note(updatedNote.id).waitForValue

        assertThat(updatedNote).isEqualTo(retrievedUpdatedNote)

        assertThat(originalNote.id).isEqualTo(retrievedUpdatedNote.id)
    }

    @Test
    fun number_of_notes_successful() = runBlockingTest {
        val first = Note(1, "First Note", "test")
        val second = Note(2, "Second Note", "test")
        val third = Note(3, "Third Note", "test")

        noteDao.insertNote(first)
        val firstCount = noteDao.countOfNotes.waitForValue

        noteDao.insertNote(second)
        val secondCount = noteDao.countOfNotes.waitForValue

        noteDao.insertNote(third)
        val thirdCount = noteDao.countOfNotes.waitForValue

        noteDao.deleteNote(third)
        val fourthCount = noteDao.countOfNotes.waitForValue

        assertThat(firstCount).isEqualTo(1)
        assertThat(secondCount).isEqualTo(2)
        assertThat(thirdCount).isEqualTo(3)
        assertThat(fourthCount).isEqualTo(2)
    }

    @Test
    fun return_note_successful() = runBlockingTest {
        val first = Note(1, "First Note", "test")
        val second = Note(2, "Second Note", "test")
        val third = Note(3, "Third Note", "test")

        noteDao.insertNote(first)
        noteDao.insertNote(second)
        noteDao.insertNote(third)

        val firstRetrieved = noteDao.note(first.id).waitForValue
        val secondRetrieved = noteDao.note(second.id).waitForValue
        val thirdRetrieved = noteDao.note(third.id).waitForValue

        assertThat(first).isEqualTo(firstRetrieved)
        assertThat(second).isEqualTo(secondRetrieved)
        assertThat(third).isEqualTo(thirdRetrieved)
    }
}
