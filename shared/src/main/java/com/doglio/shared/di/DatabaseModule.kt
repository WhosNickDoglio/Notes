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

package com.doglio.shared.di

import android.app.Application
import com.doglio.shared.NoteDatabase
import com.nicholasdoglio.shared.db.Note
import com.nicholasdoglio.shared.db.NoteQueries
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
object DatabaseModule {
    private const val NOTES_DB = "notes_db"

    @Provides
    fun driver(app: Application): AndroidSqliteDriver =
        AndroidSqliteDriver(NoteDatabase.Schema, app, NOTES_DB)

    @Provides
    @Singleton
    fun database(
        driver: AndroidSqliteDriver,
        adapter: ColumnAdapter<LocalDateTime, String>
    ): NoteDatabase = NoteDatabase(driver, Note.Adapter(timestampAdapter = adapter))

    @Provides
    fun noteQueries(database: NoteDatabase): NoteQueries = database.noteQueries
}
