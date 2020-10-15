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

import com.nicholasdoglio.notes.db.NoteQueries
import com.nicholasdoglio.notes.util.DispatcherProvider
import com.nicholasdoglio.notes.util.now
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

/**
 * If the given [id] is greater than -1 then it already exists in the database and will be updated,
 * otherwise it will be inserted as a new note.
 *
 * @param noteQueries
 * @param dispatcherProvider
 */
class UpsertNoteUseCase @Inject constructor(
    private val noteQueries: NoteQueries,
    private val dispatcherProvider: DispatcherProvider
) {
    suspend operator fun invoke(id: Long = -1L, title: String, content: String) =
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
}
