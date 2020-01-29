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

import com.nicholasdoglio.notes.Note
import com.nicholasdoglio.notes.NoteDatabase
import com.nicholasdoglio.notes.data.note.TimestampColumnAdapter
import com.nicholasdoglio.notes.data.note.NoteRepository
import com.nicholasdoglio.notes.shared.TestData
import com.nicholasdoglio.notes.shared.TestSchedulers
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test

class EditNoteViewModelTest {

    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        NoteDatabase.Schema.create(this)
    }

    private val queries =
        NoteDatabase(inMemorySqlDriver, Note.Adapter(TimestampColumnAdapter())).noteQueries

    private val repository: NoteRepository =
        NoteRepository(
            queries,
            TestSchedulers()
        )

    private val viewModel = EditNoteViewModel(repository, TestSchedulers())

    @Before
    fun setUp() {
        viewModel.input.subscribe()
        viewModel.insert.subscribe()
        viewModel.delete.subscribe()
    }

    @Test
    fun `given note ID exist when inputted then return note title`() {
        repository.insert(TestData.firstNote).subscribe()

        val titleObserver = TestObserver<String>()

        viewModel.title.subscribe(titleObserver)
        viewModel.inputId(1)

        titleObserver.assertValue(TestData.firstNote.title)
    }

    @Test
    fun `given note ID exist when inputted then return note content`() {
        repository.insert(TestData.firstNote).subscribe()

        val contentsObserver = TestObserver<String>()

        viewModel.contents.subscribe(contentsObserver)

        viewModel.inputId(1)

        contentsObserver.assertValue(TestData.firstNote.contents)
    }

    @Test
    fun `given note ID exist when inputted then return no values for title`() {
        val titleObserver = TestObserver<String>()

        viewModel.title.subscribe(titleObserver)

        viewModel.inputId(1)

        titleObserver.assertNoValues()
    }

    @Test
    fun `given note ID exist when inputted then return no values for contents`() {
        val contentsObserver = TestObserver<String>()

        viewModel.contents.subscribe(contentsObserver)

        viewModel.inputId(1)

        contentsObserver.assertNoValues()
    }

    @Test
    fun `given a note is inserted when inserted is called`() {
        val title = "This is my note"
        viewModel.inputTitle(title)
        val contents = "This is my note contents"
        viewModel.inputContents(contents)

        viewModel.triggerInsert()

        repository.observeCountOfItems
            .test()
            .assertValue { it > 0 }
    }
    //
    // @Test
    // fun `given note deleted when delete triggered then verify delete called`() {
    //
    // }
}
