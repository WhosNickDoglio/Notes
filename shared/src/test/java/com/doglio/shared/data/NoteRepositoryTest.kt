/*
 * MIT License
 *
 * Copyright (c) 2020 Nicholas Doglio
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

package com.doglio.shared.data

import com.google.common.truth.Truth.assertThat
import com.nicholasdoglio.notes.Note
import com.nicholasdoglio.notes.NoteDatabase
import com.nicholasdoglio.notes.shared.TestData
import com.nicholasdoglio.notes.shared.TestDispatchers
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.time.LocalDateTime

class NoteRepositoryTest {

    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        NoteDatabase.Schema.create(this)
    }

    private val queries =
        NoteDatabase(inMemorySqlDriver, Note.Adapter(TimestampColumnAdapter())).noteQueries
    private val repository: UpsertNoteUseCase =
        UpsertNoteUseCase(queries, TestDispatchers())

    @Test
    fun `given repository is empty when observing number of  notes then return zero`() =
        runBlocking {
            repository.observeNumOfNotes.test {
                assertThat(expectItem()).isEqualTo(0)
                cancel()
            }
        }

    @Test
    fun `given a note is inserted when observing the number of notes then return one`() =
        runBlocking {
            repository.upsert(
                -1L,
                TestData.firstNote.title!!,
                TestData.firstNote.contents!!
            )

            repository.observeNumOfNotes.test {
                assertThat(expectItem()).isEqualTo(1)
                cancel()
            }
        }

    @Test
    fun `given repository is empty when observing notes then return empty list`() = runBlocking {
        repository.observeNotes.test {
            assertThat(expectItem()).isEmpty()
            cancel()
        }
    }

    @Test
    fun `given a note is inserted when observing notes then return a list of one note`() =
        runBlocking {
            repository.upsert(
                -1L,
                TestData.firstNote.title!!,
                TestData.firstNote.contents!!
            )

            repository.observeNotes.test {
                assertThat(expectItem()).isNotEmpty()
                cancel()
            }
        }

    @Test
    fun `given note ID exists when findNoteById is called then return the correct note`() {
        runBlocking {
            queries.transaction {
                TestData.someNotes.forEach {
                    runBlocking {
                        repository.upsert(it.id, it.title!!, it.contents!!)
                    }
                }
            }

            repository.findItemById(TestData.firstNote.id)?.compareNote(TestData.firstNote)

            repository.findItemById(TestData.secondNote.id)
                ?.compareNote(TestData.secondNote)

            repository.findItemById(TestData.thirdNote.id)
                ?.compareNote(TestData.thirdNote)
        }
    }

    @Test
    fun `given a note doesn't exist when find note is triggered then return null`() = runBlocking {
        assertThat(repository.findItemById(TestData.thirdNote.id)).isEqualTo(null)
    }

    @Test
    fun `given a note doesn't exist when upsert is triggered then insert it into DB`() {
        val note =
            Note.Impl(10, "Hello World", "Testing triggerUpsert success", LocalDateTime.now())

        runBlocking {
            repository.upsert(-1L, note.title!!, note.contents!!)
            repository.observeNotes
                .test {
                    assertThat(expectItem()).isNotEmpty()
                    cancel()
                }
            repository.observeNumOfNotes
                .test {
                    assertThat(expectItem()).isEqualTo(1)
                    cancel()
                }

            repository.findItemById(note.id)?.compareNote(note)
        }
    }

    @Test
    fun `given a note exists in the DB when upsert is called then update the note in the DB`() {
        val newNote = Note.Impl(TestData.firstNote.id, "New Note", "New Note", LocalDateTime.now())

        runBlocking {
            repository.upsert(
                TestData.firstNote.id,
                TestData.firstNote.title!!,
                TestData.firstNote.contents!!
            )

            repository.upsert(newNote.id, newNote.title!!, newNote.contents!!)

            repository.findItemById(TestData.firstNote.id)?.compareNote(newNote)
        }
    }

    @Test
    fun `given a note exists when delete is triggered then remove the note from the DB`() =
        runBlocking {
            repository.upsert(
                -1L,
                TestData.firstNote.title!!,
                TestData.firstNote.contents!!
            )

            repository.observeNumOfNotes
                .test {
                    assertThat(expectItem()).isEqualTo(1)
                    cancel()
                }

            repository.deleteById(TestData.firstNote.id)

            repository.observeNumOfNotes
                .test {
                    assertThat(expectItem()).isEqualTo(0)
                    cancel()
                }
        }
}
