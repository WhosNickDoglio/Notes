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

package com.nicholasdoglio.notes.features.detail

import androidx.lifecycle.ViewModel
import com.doglio.shared.data.DeleteNoteByIdUseCase
import com.doglio.shared.data.FindNoteByIdUseCase
import com.doglio.shared.data.UpsertNoteUseCase
import com.doglio.shared.util.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    dispatcherProvider: DispatcherProvider,
    private val upsertNoteUseCase: UpsertNoteUseCase,
    private val findNoteByIdUseCase: FindNoteByIdUseCase,
    private val deleteNoteByIdUseCase: DeleteNoteByIdUseCase
) : ViewModel() {
    private val scope = CoroutineScope(dispatcherProvider.main)

    private val _state = MutableStateFlow(DetailState())

    val state: Flow<DetailState> = _state

    fun input(input: DetailInput) {
        when (input) {
            DetailInput.Save -> {
                val note = _state.value.note
                scope.launch {
                    upsertNoteUseCase(
                        note?.id
                            ?: 0L,
                        note?.title.orEmpty(), note?.contents.orEmpty()
                    )
                }
                _state.value = DetailState(
                    note = null,
                    isFinished = true
                )
            }
            DetailInput.Delete -> {
                val id = _state.value.note?.id ?: -1L
                scope.launch { deleteNoteByIdUseCase(id) }
                _state.value = DetailState(
                    note = null,
                    isFinished = true
                )
            }
            is DetailInput.FirstLoad -> {
                scope.launch {
                    val note = findNoteByIdUseCase(input.id)

                    _state.value = DetailState(note)
                }
            }
            is DetailInput.TitleChange -> {
                val note = _state.value.note

                _state.value = DetailState(note?.copy(title = input.title))
            }
            is DetailInput.ContentChange -> {
                val note = _state.value.note

                _state.value = DetailState(note?.copy(contents = input.content))
            }
        }
    }

    override fun onCleared() {
        scope.coroutineContext.cancel()
        super.onCleared()
    }
}
