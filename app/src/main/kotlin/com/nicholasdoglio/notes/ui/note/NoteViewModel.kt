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
import com.nicholasdoglio.notes.data.repo.Repository
import com.nicholasdoglio.notes.util.asFlow
import javax.inject.Inject
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class NoteViewModel @Inject constructor(private val repository: Repository<Note>) : ViewModel() {

    init {
        findNoteById()
        upsert()
        delete()
    }

    val note: MutableLiveData<Note> = MutableLiveData()

    // TODO think about some warning if Note is empty from here?
    val isNoteEmpty: LiveData<Boolean> =
        note.map { it.title.isBlank() && it.contents.isBlank() }.distinctUntilChanged()

    val triggerUpsert = ConflatedBroadcastChannel<Unit?>()

    val triggerDelete = ConflatedBroadcastChannel<Unit?>()

    val inputNoteId: ConflatedBroadcastChannel<Long?> = ConflatedBroadcastChannel()

    private fun findNoteById() {
        viewModelScope.launch {
            inputNoteId.asFlow()
                .onEach { Timber.i("Input triggered") }
                .filterNotNull()
                .flatMapConcat { repository.findItemById(it) }
                .collect {
                    note.value = it
                    Timber.i(it.toString())
                }
        }
    }

    private fun upsert() {
        viewModelScope.launch {
            triggerUpsert.asFlow()
                .onEach { Timber.i("Upsert triggered") }
                .flatMapConcat { note.asFlow() }
                .filterNotNull()
                .collect {
                    Timber.i(it.toString())
                    repository.upsert(it)
                }
        }
    }

    private fun delete() {
        viewModelScope.launch {
            triggerDelete.asFlow()
                .onEach { Timber.i("Delete triggered") }
                .flatMapConcat { note.asFlow() }
                .filterNotNull()
                .collect {
                    Timber.i(it.toString())
                    // TODO check if ID is above -1 here?
                    repository.delete(it)
                }
        }
    }
}
