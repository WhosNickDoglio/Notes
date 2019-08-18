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
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf

class FakeDao : NoteDao {

    private val notes: MutableList<Note> = mutableListOf()
    private val _notes: ConflatedBroadcastChannel<List<Note>> = ConflatedBroadcastChannel(notes)
    private val _count: ConflatedBroadcastChannel<Int> = ConflatedBroadcastChannel(notes.size)

    override val observeNotes: Flow<List<Note>> = _notes.asFlow()
    override val countOfNotes: Flow<Int> = _count.asFlow()

    override fun note(id: Long): Flow<Note?> {
        val note = notes.find { it.id == id }

        return flowOf(note)
    }

    override suspend fun insertNote(note: Note): Long {
        if (notes.add(note)) {
            _count.offer(notes.size)
            _notes.offer(notes)
            return notes.indexOf(note).toLong()
        } else {
            return -1L
        }
    }

    override suspend fun updateNote(note: Note) {
        val oldNote = notes.find { it.id == note.id }

        val index = notes.indexOf(oldNote)

        notes.removeAt(index)
        notes.add(index, note)
    }

    override suspend fun deleteNote(note: Note) {
        if (notes.remove(note)) {
            _count.offer(notes.size)
            _notes.offer(notes)
        }
    }
}
