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

package com.nicholasdoglio.notes.features.editnote

import com.google.common.truth.Truth.assertThat
import com.nicholasdoglio.notes.Note
import com.nicholasdoglio.notes.NoteDatabase
import com.nicholasdoglio.notes.data.NoteRepository
import com.nicholasdoglio.notes.data.TimestampColumnAdapter
import com.nicholasdoglio.notes.shared.TestData
import com.nicholasdoglio.notes.shared.TestDispatchers
import com.nicholasdoglio.notes.shared.test
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import org.junit.Test

class EditNoteViewModelTest {

    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        NoteDatabase.Schema.create(this)
    }

    private val queries =
        NoteDatabase(inMemorySqlDriver, Note.Adapter(TimestampColumnAdapter())).noteQueries

    private val dispatcher = TestDispatchers()

    private val repository: NoteRepository =
        NoteRepository(
            queries,
            dispatcher
        )

    private val viewModel = EditNoteViewModel(repository, dispatcher)

    @Test
    fun `given note ID exist when inputted then return note title`() {
        runBlocking {
            repository.upsert(
                title = TestData.firstNote.title!!,
                content = TestData.firstNote.contents!!
            )

            viewModel.inputNoteId.offer(1)

            viewModel.title.test {
                assertThat(expectItem()).isEqualTo(TestData.firstNote.title)
                cancel()
            }
        }
    }

    @Test
    fun `given note ID exist when inputted then return note content`() {
        runBlocking {
            repository.upsert(
                title = TestData.firstNote.title!!,
                content = TestData.firstNote.contents!!
            )

            viewModel.inputNoteId.offer(1)

            viewModel.contents.test {
                assertThat(expectItem()).isEqualTo(TestData.firstNote.contents)
                cancel()
            }
        }
    }

    @Test
    fun `given note ID exist when inputted then return no values for title`() {
        runBlocking {
            viewModel.inputNoteId.offer(1)

            viewModel.title.test {
                assertThat(expectItem()).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun `given note ID exist when inputted then return no values for contents`() {
        runBlocking {
            viewModel.inputNoteId.offer(1)

            viewModel.contents.test {
                assertThat(expectItem()).isEmpty()
                cancel()
            }
        }
    }

    @Test
    fun `given a note is inserted when inserted is called then a note should exist in DB`() {
        viewModel.inputNoteId.offer(-1L)
        val title = "This is my note"
        viewModel.titleChannel.offer(title)
        val contents = "This is my note contents"
        viewModel.contentChannel.offer(contents)

        viewModel.inputInsert.offer(Unit)

        runBlocking {
            repository.observeNumOfNotes.test {
                assertThat(expectItem()).isGreaterThan(0)
                cancel()
            }
        }
    }

    @Test
    fun `given note deleted when delete triggered then note shouldn't exist in DB`() {
    }
}
