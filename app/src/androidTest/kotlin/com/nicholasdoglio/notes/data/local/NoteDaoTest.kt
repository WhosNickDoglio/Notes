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
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

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
    fun give_db_is_empty_when_observing_db_then_return_zero() = runBlockingTest {
        noteDao.observeNumOfNotes.test {
            assertThat(expectItem()).isEqualTo(0)

            cancel()
        }
    }

    @Test
    fun given_note_when_insert_note_is_called_then_save_note() = runBlockingTest {
        val note = Note(1, "Hello", "World")

        noteDao.insertNote(note)

        noteDao.findNoteById(note.id).test {
            assertThat(note).isEqualTo(expectItem())

            cancel()
        }
    }

    @Test
    fun given_note_exists_when_delete_note_is_called_then_delete_note() = runBlockingTest {
        val first = Note(1, "First Note", "test")
        val second = Note(2, "Second Note", "test")

        noteDao.insertNote(first)
        noteDao.insertNote(second)

        noteDao.observeNumOfNotes.test {
            assertThat(expectItem()).isEqualTo(2)
            cancel()
        }

        noteDao.deleteNote(second)

        noteDao.findNoteById(first.id).test {
            assertThat(first).isEqualTo(expectItem())

            cancel()
        }

        noteDao.observeNumOfNotes.test {
            assertThat(expectItem()).isEqualTo(1)
            cancel()
        }
    }

    @Test
    fun given_note_exists_when_update_is_called_then_update_note() = runBlockingTest {
        val originalNote = Note(1, "Hello", "World!")

        noteDao.insertNote(originalNote)

        noteDao.findNoteById(originalNote.id).test {
            assertThat(originalNote).isEqualTo(expectItem())
            cancel()
        }

        val updatedNote = Note(1, "Hello", "Earth!")

        noteDao.updateNote(updatedNote)

        noteDao.findNoteById(updatedNote.id).test {
            assertThat(updatedNote).isEqualTo(expectItem())
            cancel()
        }
    }

    @Test
    fun given_notes_when_observing_number_of_notes_then_return_correct_number() = runBlockingTest {
        val first = Note(1, "First Note", "test")
        val second = Note(2, "Second Note", "test")
        val third = Note(3, "Third Note", "test")

        noteDao.insertNote(first)
        noteDao.observeNumOfNotes.test {
            assertThat(expectItem()).isEqualTo(1)
            cancel()
        }

        noteDao.insertNote(second)
        noteDao.observeNumOfNotes.test {
            assertThat(expectItem()).isEqualTo(2)
            cancel()
        }

        noteDao.insertNote(third)
        noteDao.observeNumOfNotes.test {
            assertThat(expectItem()).isEqualTo(3)
            cancel()
        }

        noteDao.deleteNote(third)
        noteDao.observeNumOfNotes.test {
            assertThat(expectItem()).isEqualTo(2)
            cancel()
        }
    }

    @Test
    fun given_note_id_that_exists_when_find_note_called_then_return_note() = runBlockingTest {
        val first = Note(1, "First Note", "test")
        val second = Note(2, "Second Note", "test")
        val third = Note(3, "Third Note", "test")

        noteDao.insertNote(first)
        noteDao.insertNote(second)
        noteDao.insertNote(third)

        noteDao.findNoteById(first.id).test {
            assertThat(first).isEqualTo(expectItem())
            cancel()
        }
        noteDao.findNoteById(second.id).test {
            assertThat(second).isEqualTo(expectItem())
            cancel()
        }
        noteDao.findNoteById(third.id).test {
            assertThat(third).isEqualTo(expectItem())
            cancel()
        }
    }

    @Test
    fun given_note_id_that_does_not_exist_when_find_note_called_then_return_null() =
        runBlockingTest {
            noteDao.findNoteById(10).test {
                assertThat(expectItem()).isEqualTo(null)
                cancel()
            }
        }
}
