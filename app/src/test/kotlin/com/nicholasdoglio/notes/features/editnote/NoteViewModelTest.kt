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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nicholasdoglio.notes.Note
import com.nicholasdoglio.notes.NoteDatabase
import com.nicholasdoglio.notes.data.local.TimestampColumnAdapter
import com.nicholasdoglio.notes.data.repo.NoteRepository
import com.nicholasdoglio.notes.data.repo.Repository
import com.nicholasdoglio.notes.shared.TestSchedulers
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        NoteDatabase.Schema.create(this)
    }

    private val queries =
        NoteDatabase(inMemorySqlDriver, Note.Adapter(TimestampColumnAdapter())).noteQueries

    private val repository: Repository<Note> = NoteRepository(queries, TestSchedulers())
    private lateinit var viewModel: NoteViewModel

    @Before
    fun setUp() {
        viewModel = NoteViewModel(repository)
    }

    @Test
    fun `given note ID exists when find note is called then return a note`() {
    }

    @Test
    fun `given note ID doesn't exist in DB when find note is called then return null`() {
    }

    @Test
    fun `given note upserted when upserted triggered then verify upserted called`() {
    }

    @Test
    fun `given note deleted when delete triggered then verify delete called`() {
    }
}
