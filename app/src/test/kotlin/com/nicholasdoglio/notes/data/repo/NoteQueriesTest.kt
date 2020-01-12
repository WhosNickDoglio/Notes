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

import com.google.common.truth.Truth.assertThat
import com.nicholasdoglio.notes.Note
import com.nicholasdoglio.notes.NoteDatabase
import com.nicholasdoglio.notes.data.local.TimestampColumnAdapter
import com.nicholasdoglio.notes.shared.TestData
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.junit.After
import org.junit.Test

class NoteQueriesTest {

    private val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
        NoteDatabase.Schema.create(this)
    }

    private val queries =
        NoteDatabase(inMemorySqlDriver, Note.Adapter(TimestampColumnAdapter())).noteQueries

    @After
    fun tearDown() {
        queries.dropTable()
    }

    @Test
    fun `given DB is empty when all notes is called then return empty list`() {
        assertThat(queries.allNotes().executeAsList()).isEmpty()
    }

    @Test
    fun `given three notes exist in DB when all notes is called then return list of three`() {
        TestData.someNotes.forEach {
            queries.insertOrReplace(it.id, it.title, it.contents, it.timestamp)
        }

        assertThat(queries.allNotes().executeAsList()).isEqualTo(TestData.someNotes)
    }

    @Test
    fun `given DB is empty when count is called then return zero`() {
        assertThat(queries.count().executeAsOne())
            .isEqualTo(0)
    }

    @Test
    fun `given three notes exist in DB when count is called then return three`() {
        TestData.someNotes.forEach {
            queries.insertOrReplace(it.id, it.title, it.contents, it.timestamp)
        }

        assertThat(queries.count().executeAsOne()).isEqualTo(3)
    }

    @Test
    fun `given DB is empty when findNoteById is called then return null`() {
        assertThat(queries.findNoteById(0).executeAsOneOrNull())
            .isNull()
    }

    @Test
    fun `given note exists when find by id is called then return note`() {
        queries.insertOrReplace(
            TestData.firstNote.id,
            TestData.firstNote.title,
            TestData.firstNote.contents,
            TestData.firstNote.timestamp
        )

        assertThat(queries.findNoteById(TestData.firstNote.id).executeAsOneOrNull())
            .isEqualTo(TestData.firstNote)
    }

    @Test
    fun `given DB is empty when insertOrReplace is called then insert note`() {
        queries.insertOrReplace(
            TestData.firstNote.id,
            TestData.firstNote.title,
            TestData.firstNote.contents,
            TestData.firstNote.timestamp
        )

        assertThat(queries.count().executeAsOne())
            .isEqualTo(1)

        assertThat(queries.allNotes().executeAsList())
            .isEqualTo(listOf(TestData.firstNote))

        assertThat(queries.findNoteById(TestData.firstNote.id).executeAsOneOrNull())
            .isEqualTo(TestData.firstNote)
    }

    @Test
    fun `given note exists when insertOrReplace is called then update note`() {
        queries.insertOrReplace(
            TestData.firstNote.id,
            TestData.firstNote.title,
            TestData.firstNote.contents,
            TestData.firstNote.timestamp
        )

        assertThat(queries.findNoteById(TestData.firstNote.id).executeAsOneOrNull())
            .isEqualTo(TestData.firstNote)

        val newNote = Note.Impl(
            TestData.firstNote.id,
            TestData.firstNote.title,
            "Something something new",
            TestData.firstNote.timestamp
        )

        queries.insertOrReplace(newNote.id, newNote.title, newNote.contents, newNote.timestamp)

        assertThat(queries.findNoteById(TestData.firstNote.id).executeAsOneOrNull())
            .isEqualTo(newNote)
    }

    @Test
    fun `give note exists when deleteById is called then delete note`() {
        queries.insertOrReplace(
            TestData.firstNote.id,
            TestData.firstNote.title,
            TestData.firstNote.contents,
            TestData.firstNote.timestamp
        )

        queries.deleteById(TestData.firstNote.id)

        assertThat(queries.count().executeAsOne())
            .isEqualTo(0)

        assertThat(queries.allNotes().executeAsList()).isEmpty()

        assertThat(queries.findNoteById(TestData.firstNote.id).executeAsOneOrNull()).isEqualTo(null)
    }
}
