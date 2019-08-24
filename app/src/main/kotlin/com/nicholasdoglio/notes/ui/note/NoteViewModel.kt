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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nicholasdoglio.notes.data.model.Note
import com.nicholasdoglio.notes.data.repo.NoteRepository
import javax.inject.Inject
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import timber.log.Timber

class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    // TODO think about some warning if Note is empty from here?

    init {
        findNoteById()
        upsert()
        delete()
    }

    val inputNoteId: ConflatedBroadcastChannel<Long> = ConflatedBroadcastChannel()

    val triggerUpsert: ConflatedBroadcastChannel<Unit> = ConflatedBroadcastChannel()

    val triggerDelete: ConflatedBroadcastChannel<Unit> = ConflatedBroadcastChannel()

    val note: MutableLiveData<Note> = MutableLiveData()

    private fun findNoteById() {
        viewModelScope.launch {
            inputNoteId.asFlow()
                .flatMapConcat { noteRepository.findNoteById(it) }
                .collect {
                    note.value = it
                    Timber.d("LIVE DATA VALUE ${note.value}")
                    Timber.d("INPUT NOTE ID TRIGGERED")
                }
        }
    }

    private fun upsert() {
        viewModelScope.launch {
            triggerUpsert.asFlow()
                .collect {
                    val currentNote = note.value
                        Timber.d("LIVE DATA VALUE ${note.value}")
                        Timber.d(" UPSERT TRIGGERED")

                    if (currentNote != null) {
                        noteRepository.upsert(currentNote)
                    }
                }
        }
    }

    private fun delete() {
        viewModelScope.launch {
            triggerDelete.asFlow()
                .collect {
                    val currentNote = note.value
                        Timber.d("LIVE DATA VALUE ${note.value}")
                        Timber.d("DELETE NOTE TRIGGERED TRIGGERED")

                    if (currentNote != null) {
                        noteRepository.delete(currentNote)
                    }
                }
        }
    }
}

// TODO I think this can be simplified and moved here instead of in the Fragment
//        Observable.combineLatest(
//            noteTitle.textChanges().map { it.isNotEmpty() },
//            noteContent.textChanges().map { it.isNotEmpty() },
//            BiFunction<Boolean, Boolean, Boolean> { firstBool, secondBool -> firstBool && secondBool })
//            .distinctUntilChanged()
//            .autoDisposable(viewLifecycleOwner)
//            .subscribe { buttonsRelay.accept(it) }
//
