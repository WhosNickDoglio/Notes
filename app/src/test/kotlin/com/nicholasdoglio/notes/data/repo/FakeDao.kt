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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nicholasdoglio.notes.data.local.NoteDao
import com.nicholasdoglio.notes.data.model.Note

class FakeDao : NoteDao {

    private val notes: MutableList<Note> = mutableListOf()
    private val _notes: MutableLiveData<List<Note>> = MutableLiveData(notes)
    private val _count: MutableLiveData<Int> = MutableLiveData(notes.size)

    override val observeNotes: LiveData<List<Note>> = _notes
    override val countOfNotes: LiveData<Int> = _count

    override fun note(id: Long): LiveData<Note> {
        val currentNote = notes.find { it.id == id }

        return MutableLiveData(currentNote)
    }

    override suspend fun insertNote(note: Note): Long {
        return if (notes.add(note)) {
            _count.postValue(notes.size)
            _notes.postValue(notes)
            1
        } else {
            0
        }
    }

    // TODO do this better
    override suspend fun updateNote(note: Note): Int {
        val oldNote = notes.find { it.id == note.id }

        val index = notes.indexOf(oldNote)

        // update
        notes.removeAt(index)
        notes.add(index, note)

        return 0
    }

    override suspend fun deleteNote(note: Note): Int {
        return if (notes.remove(note)) {
            _count.postValue(notes.size)
            _notes.postValue(notes)
            0
        } else {
            _count.postValue(notes.size)
            _notes.postValue(notes)
            1
        }
    }
}
