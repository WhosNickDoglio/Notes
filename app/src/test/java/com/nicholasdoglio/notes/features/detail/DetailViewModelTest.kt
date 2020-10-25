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

package com.nicholasdoglio.notes.features.detail

import com.nicholasdoglio.notes.db.NoteDatabase
import com.nicholasdoglio.notes.data.DeleteNoteByIdUseCase
import com.nicholasdoglio.notes.data.FindNoteByIdUseCase
import com.nicholasdoglio.notes.data.TimestampColumnAdapter
import com.nicholasdoglio.notes.data.UpsertNoteUseCase
import com.nicholasdoglio.notes.db.Note
import com.nicholasdoglio.notes.util.TestDispatchers
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.CoroutineScope

class DetailViewModelTest {
    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        NoteDatabase.Schema.create(this)
    }

    private val queries =
            NoteDatabase(inMemorySqlDriver, Note.Adapter(TimestampColumnAdapter())).noteQueries

    private val dispatchers = TestDispatchers()

    private val upsertNoteUseCase = UpsertNoteUseCase(queries, dispatchers)
    private val findNoteByIdUseCase = FindNoteByIdUseCase(queries, dispatchers)
    private val deleteNoteByIdUseCase = DeleteNoteByIdUseCase(queries, dispatchers)

    private val viewModel = DetailViewModel(
            upsertNoteUseCase,
            findNoteByIdUseCase,
            deleteNoteByIdUseCase,
            CoroutineScope(dispatchers.main)
    )
}
