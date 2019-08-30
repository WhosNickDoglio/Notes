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

package com.nicholasdoglio.notes.data.repo

import com.google.common.truth.Truth.assertThat
import com.nicholasdoglio.notes.data.model.Note
import com.nicholasdoglio.notes.shared.TestData
import com.nicholasdoglio.notes.shared.test
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class NoteRepositoryTest {

    private val repository = NoteRepository(FakeDao())

    @Test
    fun `given repository is empty when observing number of  notes then return zero`() =
        runBlockingTest {
            repository.countOfNotes.test {
                assertThat(expectItem()).isEqualTo(0)
                cancel()
            }
        }

    @Test
    fun `given a note is inserted when observing the number of notes then return one`() =
        runBlockingTest {
            repository.upsert(TestData.firstNote)

            repository.countOfNotes.test {
                assertThat(expectItem()).isEqualTo(1)
                cancel()
            }
        }

    @Test
    fun `given repository is empty when observing notes then return empty list`() =
        runBlockingTest {
            repository.observeNotes.test {
                assertThat(expectItem()).isEqualTo(emptyList<Note>())
                cancel()
            }
        }

    @Test
    fun `given a note is inserted when observing notes then return a list of one note`() =
        runBlockingTest {
            repository.upsert(TestData.firstNote)

            repository.observeNotes.test {
                assertThat(expectItem()).isEqualTo(listOf(TestData.firstNote))
                cancel()
            }
        }

    @Test
    fun `given note ID exists when findNoteById is called then return the correct note`() =
        runBlockingTest {
            TestData.someNotes.forEach {
                repository.upsert(it)
            }

            repository.findNoteById(TestData.firstNote.id).test {
                assertThat(expectItem()).isEqualTo(TestData.firstNote)
                expectComplete()
                cancel()
            }

            repository.findNoteById(TestData.secondNote.id).test {
                assertThat(expectItem()).isEqualTo(TestData.secondNote)
                expectComplete()
                cancel()
            }

            repository.findNoteById(TestData.thirdNote.id).test {
                assertThat(expectItem()).isEqualTo(TestData.thirdNote)
                expectComplete()
                cancel()
            }
        }

    @Test
    fun `given a note ID that doesn't exist in the repository when finding a note then return null`() =
        runBlockingTest {
            repository.findNoteById(TestData.thirdNote.id).test {
                assertThat(expectItem()).isEqualTo(null)
                expectComplete()
                cancel()
            }
        }

    @Test // TODO think about failure case?
    fun `given a note doesn't exist when upsert is triggered then insert it into DB`() =
        runBlockingTest {
            val note = Note(5, "Hello World", "Testing triggerUpsert success")
            repository.upsert(note)

            repository.observeNotes.test {
                assertThat(expectItem()).isEqualTo(listOf(note))
                cancel()
            }

            repository.countOfNotes.test {
                assertThat(expectItem()).isEqualTo(1)
                cancel()
            }

            repository.findNoteById(note.id).test {
                assertThat(expectItem()).isEqualTo(note)
                expectComplete()
                cancel()
            }
        }

    @Test // TODO think about failure case?
    fun `given a note exists in the DB when upsert is called then update the note in the DB`() =
        runBlockingTest {
            repository.upsert(TestData.firstNote)

            val newNote = Note(TestData.firstNote.id, "New Note", "New Note")

            repository.upsert(newNote)

            repository.findNoteById(TestData.firstNote.id).test {
                expectItem().apply {
                    assertThat(this).isEqualTo(newNote)
                    assertThat(this).isNotEqualTo(TestData.firstNote)
                }
                expectComplete()
                cancel()
            }
        }

    @Test // TODO think about failure case?
    fun `given a note exists when delete is triggered then remove the note from the DB`() =
        runBlockingTest {
            repository.upsert(TestData.firstNote)

            repository.countOfNotes.test {
                assertThat(expectItem()).isEqualTo(1)
                cancel()
            }

            repository.delete(TestData.firstNote)

            repository.countOfNotes.test {
                assertThat(expectItem()).isEqualTo(0)
                cancel()
            }
        }
}
