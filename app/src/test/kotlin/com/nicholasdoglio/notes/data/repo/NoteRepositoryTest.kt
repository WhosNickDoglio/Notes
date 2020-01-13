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

package com.nicholasdoglio.notes.data.repo

import com.nicholasdoglio.notes.Note
import com.nicholasdoglio.notes.NoteDatabase
import com.nicholasdoglio.notes.data.local.TimestampColumnAdapter
import com.nicholasdoglio.notes.shared.TestSchedulers
import com.nicholasdoglio.notes.shared.TestData
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDateTime

class NoteRepositoryTest {

    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        NoteDatabase.Schema.create(this)
    }

    private val queries =
        NoteDatabase(inMemorySqlDriver, Note.Adapter(TimestampColumnAdapter())).noteQueries
    private lateinit var repository: Repository<Note>

    @Before
    fun setUp() {
        repository = NoteRepository(queries, TestSchedulers())
    }

    @Test
    fun `given repository is empty when observing number of  notes then return zero`() {

        repository.observeCountOfItems
            .test()
            .assertValue(0)
    }

    @Test
    fun `given a note is inserted when observing the number of notes then return one`() {
        repository.upsert(TestData.firstNote)
            .andThen(repository.observeCountOfItems)
            .test()
            .assertValue(1)
    }

    @Test
    fun `given repository is empty when observing notes then return empty list`() {
        repository.observeItems
            .test()
            .assertValue(emptyList())
    }

    // @Test
    // fun `given a note is inserted when observing notes then return a list of one note`() {
    //     repository.upsert(TestData.firstNote)
    //         .andThen(repository.observeItems)
    //         .test()
    //         .assertValue(listOf(TestData.firstNote))
    // }

    // @Test
    // fun `given note ID exists when findNoteById is called then return the correct note`() {
    //     TestData.someNotes.forEach {
    //         repository.upsert(it).test()
    //     }
    //
    //     repository.findItemById(TestData.firstNote.id).test().assertValue(TestData.firstNote)
    //     repository.findItemById(TestData.secondNote.id).test().assertValue(TestData.secondNote)
    //     repository.findItemById(TestData.thirdNote.id).test().assertValue(TestData.thirdNote)
    // }

    // given a note ID that doesn't exist in the repository
    // when finding a note then return null
    @Test
    fun `TODO Fix name`() {
        repository.findItemById(TestData.thirdNote.id).test()
            .assertNoValues()
    }

    @Test // TODO think about failure case?
    fun `given a note doesn't exist when upsert is triggered then insert it into DB`() {
        val note =
            Note.Impl(10, "Hello World", "Testing triggerUpsert success", LocalDateTime.now())
        repository.upsert(note)
            .andThen(repository.observeItems)
            .test()
            .assertValue(listOf(note))

        repository.observeCountOfItems
            .test()
            .assertValue(1)

        repository.findItemById(note.id).test()
            .assertValue(note)
    }

    // @Test // TODO think about failure case?
    // fun `given a note exists in the DB when upsert is called then update the note in the DB`() {
    //     val newNote = Note.Impl(TestData.firstNote.id, "New Note", "New Note", LocalDateTime.now())
    //
    //     repository.upsert(TestData.firstNote)
    //         .andThen(repository.upsert(newNote))
    //         .andThen(repository.findItemById(TestData.firstNote.id))
    //         .test()
    //         .assertValue(newNote)
    // }

    @Test // TODO think about failure case?
    fun `given a note exists when delete is triggered then remove the note from the DB`() {
        repository.upsert(TestData.firstNote)
            .andThen(repository.observeCountOfItems)
            .test()
            .assertValue(1)

        repository.delete(TestData.firstNote)
            .andThen(repository.observeCountOfItems)
            .test()
            .assertValue(0)
    }
}
