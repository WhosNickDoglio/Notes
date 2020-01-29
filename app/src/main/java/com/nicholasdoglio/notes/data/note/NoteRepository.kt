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

package com.nicholasdoglio.notes.data.note

import com.nicholasdoglio.notes.Note
import com.nicholasdoglio.notes.NoteQueries
import com.nicholasdoglio.notes.util.SchedulersProvider
import com.squareup.sqldelight.runtime.rx.asObservable
import com.squareup.sqldelight.runtime.rx.mapToList
import com.squareup.sqldelight.runtime.rx.mapToOne
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteQueries: NoteQueries,
    private val schedulersProvider: SchedulersProvider
) {

    val observeCountOfItems: Flowable<Long> =
        noteQueries.count().asObservable(schedulersProvider.database).mapToOne()
            .toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(schedulersProvider.database)

    val observeItems: Flowable<List<Note>> =
        noteQueries.allNotes().asObservable(schedulersProvider.database).mapToList()
            .toFlowable(BackpressureStrategy.LATEST)
            .subscribeOn(schedulersProvider.database)

    fun findItemById(id: Long): Maybe<Note> =
        Maybe.create { emitter: MaybeEmitter<Note> ->
            val note = noteQueries.findNoteById(id).executeAsOneOrNull()
            if (note != null) {
                emitter.onSuccess(note)
            }
        }
            .subscribeOn(schedulersProvider.database)

    fun insert(item: Note): Completable =
        Completable.fromAction {
            noteQueries.insertOrReplace(
                id = item.id,
                title = item.title,
                contents = item.contents,
                timestamp = LocalDateTime.now()
            )
        }
            .subscribeOn(schedulersProvider.database)

    fun delete(item: Note): Completable =
        Completable.fromAction { noteQueries.deleteById(item.id) }
            .subscribeOn(schedulersProvider.database)
}
