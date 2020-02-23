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

import com.nicholasdoglio.notes.Note
import com.nicholasdoglio.notes.NoteQueries
import com.nicholasdoglio.notes.util.DispatcherProvider
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

/**
 * A Repository to manage [Note] in the app database.
 */
class NoteRepository @Inject constructor(
    private val noteQueries: NoteQueries,
    private val dispatcherProvider: DispatcherProvider
) {

    /**
     * A [Flow] that returns the number of [Notes][Note] in the database each time it updates.
     */
    val observeNumOfNotes: Flow<Long> =
        noteQueries.count().asFlow().mapToOne(dispatcherProvider.database)

    /**
     * A [Flow] that returns a list of [Notes][Note] each time the database updates.
     */
    val observeNotes: Flow<List<Note>> =
        noteQueries.allNotes().asFlow().mapToList(dispatcherProvider.database)

    /**
     * Returns a [Note] for the given ID or null if a corresponding [Note] doesn't exist in the database.
     *
     * @param id: the ID of the note you're trying to find.
     */
    suspend fun findItemById(id: Long): Note? = withContext(dispatcherProvider.database) {
        noteQueries.findNoteById(id).executeAsOneOrNull()
    }

    /**
     * If the given [id] is greater than -1 then it already exists in the database and will be updated,
     * otherwise it will be inserted as a new note.
     *
     * @param id: the ID of the note you're trying to update, defaults to -1 if none given.
     * @param title: the title for the given note.
     * @param content: the content for the given note.
     */
    suspend fun upsert(id: Long = -1L, title: String, content: String) =
        withContext(dispatcherProvider.database) {
            if (id > -1L) {
                noteQueries.updateById(
                    title = title,
                    contents = content,
                    timestamp = LocalDateTime.now(),
                    id = id
                )
            } else {
                noteQueries.insert(
                    title = title,
                    contents = content,
                    timestamp = LocalDateTime.now()
                )
            }
        }

    /**
     * Deletes the [Note] from the database corresponding to the given [id].
     *
     * @param id: the ID of the given note you'd like to delete.
     */
    suspend fun deleteById(id: Long) = withContext(dispatcherProvider.database) {
        noteQueries.deleteById(id)
    }
}
