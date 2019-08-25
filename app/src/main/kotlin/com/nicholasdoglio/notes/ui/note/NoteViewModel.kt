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

package com.nicholasdoglio.notes.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.nicholasdoglio.notes.data.model.Note
import com.nicholasdoglio.notes.data.repo.NoteRepository
import javax.inject.Inject
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch

class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            findNoteById()
            upsert()
            delete()
        }
    }

    val triggerUpsert: ConflatedBroadcastChannel<Unit> = ConflatedBroadcastChannel()

    val triggerDelete: ConflatedBroadcastChannel<Unit> = ConflatedBroadcastChannel()

    val note: MutableLiveData<Note> = MutableLiveData()

    val inputNoteId: ConflatedBroadcastChannel<Long> = ConflatedBroadcastChannel()

    // TODO think about some warning if Note is empty from here?
    val isNoteEmpty: LiveData<Boolean> =
        note.map { it.title.isBlank() && it.contents.isBlank() }.distinctUntilChanged()

    private suspend fun findNoteById() {
        inputNoteId.asFlow()
            .flatMapConcat { noteRepository.findNoteById(it) }
            .collect { note.value = it }
    }

    private suspend fun upsert() {
        triggerUpsert.asFlow()
            .collect {
                val currentNote = note.value

                if (currentNote != null) {
                    noteRepository.upsert(currentNote)
                }
            }
    }

    private suspend fun delete() {
        triggerDelete.asFlow()
            .collect {
                val currentNote = note.value

                // TODO check if ID is above -1 here?
                if (currentNote != null) {
                    noteRepository.delete(currentNote)
                }
            }
    }
}
