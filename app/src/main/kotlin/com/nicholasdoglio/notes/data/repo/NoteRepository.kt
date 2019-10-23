/*
 * MIT License
 *
 * Copyright (c) 2019 Nicholas Doglio
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

import com.nicholasdoglio.notes.data.local.NoteDao
import com.nicholasdoglio.notes.data.model.Note
import com.nicholasdoglio.notes.util.SchedulersProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val dao: NoteDao,
    private val schedulersProvider: SchedulersProvider
) : Repository<Note> {

    override val observeCountOfItems: Flowable<Int> = dao.observeNumOfNotes
        .observeOn(schedulersProvider.database)

    override val observeItems: Flowable<List<Note>> = dao.observeNotes
        .observeOn(schedulersProvider.database)

    override fun findItemById(id: Long): Maybe<Note> = dao.findNoteById(id)
        .observeOn(schedulersProvider.database)

    // TODO check if this actually works?
    override fun upsert(item: Note): Completable = dao.insertNote(item)
        .filter { it == FAILED_INSERT }
        .flatMapCompletable { dao.updateNote(item) }
        .observeOn(schedulersProvider.database)

    override fun delete(item: Note): Completable =
        dao.deleteNote(item).observeOn(schedulersProvider.database)

    private companion object {
        private const val FAILED_INSERT = -1L
    }
}
