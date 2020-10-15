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

package com.nicholasdoglio.notes.data

import com.nicholasdoglio.notes.db.NoteDatabase
import com.nicholasdoglio.notes.db.Note
import com.nicholasdoglio.notes.util.TestDispatchers
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

class ObserveNoteCountUseCaseTest {

    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        NoteDatabase.Schema.create(this)
    }

    private val queries =
        NoteDatabase(inMemorySqlDriver, Note.Adapter(TimestampColumnAdapter())).noteQueries

    private val observeNoteCountUseCase = ObserveNoteCountUseCase(queries, TestDispatchers())

    // TODO https://youtrack.jetbrains.com/issue/KT-41374
    // @Test
    // fun `given DB is empty when count is observed then return zero`() = runBlocking {
    //     observeNoteCountUseCase().test {
    //         assertThat(expectItem()).isEqualTo(0)
    //         expectNoEvents()
    //         expectComplete()
    //     }
    // }

    // TODO more flow based tests
}
