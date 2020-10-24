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

import com.google.common.truth.Truth.assertThat
import com.nicholasdoglio.notes.db.NoteDatabase
import com.nicholasdoglio.notes.db.Note
import com.nicholasdoglio.notes.util.NEW_NOTE_ID
import com.nicholasdoglio.notes.util.TestDispatchers
import com.nicholasdoglio.notes.util.compareNote
import com.nicholasdoglio.notes.util.firstTestNote
import com.nicholasdoglio.notes.util.now
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.junit.Test

class UpsertNoteUseCaseTest {
    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        NoteDatabase.Schema.create(this)
    }

    private val queries =
        NoteDatabase(inMemorySqlDriver, Note.Adapter(TimestampColumnAdapter())).noteQueries

    private val upsertNoteUseCase = UpsertNoteUseCase(queries, TestDispatchers())

    @Test
    fun `given no note exists in DB when upsert is called then insert note`() {
        assertThat(queries.count().executeAsOne()).isEqualTo(0)

        runBlocking {
            upsertNoteUseCase(
                NEW_NOTE_ID,
                firstTestNote.title.orEmpty(),
                firstTestNote.contents.orEmpty()
            )
        }

        assertThat(queries.count().executeAsOne()).isEqualTo(1)

        val foundNote = queries.findNoteById(firstTestNote.id).executeAsOne()

        firstTestNote.compareNote(foundNote)
    }

    @Test
    fun `given a note exists in DB when upsert is called then update note`() {
        queries.insert(firstTestNote.title, firstTestNote.contents, LocalDateTime.now())

        assertThat(queries.count().executeAsOne()).isEqualTo(1)

        val insertedNote = queries.findNoteById(firstTestNote.id).executeAsOne()

        insertedNote.compareNote(firstTestNote)

        runBlocking {
            upsertNoteUseCase(
                firstTestNote.id,
                "New Note title",
                "New note content"
            )
        }

        val foundNote = queries.findNoteById(firstTestNote.id).executeAsOne()

        foundNote.run {
            assertThat(this.id).isEqualTo(firstTestNote.id)
            assertThat(this.title).isNotEqualTo(firstTestNote.title)
            assertThat(this.contents).isNotEqualTo(firstTestNote.contents)
        }
    }
}
