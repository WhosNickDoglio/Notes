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
import com.nicholasdoglio.notes.data.DeleteNoteByIdUseCase
import com.nicholasdoglio.notes.data.FindNoteByIdUseCase
import com.nicholasdoglio.notes.data.UpsertNoteUseCase
import com.nicholasdoglio.notes.di.AppCoroutineScope
import com.nicholasdoglio.notes.util.NEW_NOTE_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DetailViewModel @Inject constructor(
        private val upsertNoteUseCase: UpsertNoteUseCase,
        private val findNoteByIdUseCase: FindNoteByIdUseCase,
        private val deleteNoteByIdUseCase: DeleteNoteByIdUseCase,
        @AppCoroutineScope private val scope: CoroutineScope
) : ViewModel(), Detail {
    private val mutableStateFlow: MutableStateFlow<DetailState?> = MutableStateFlow(null)

    override val state: Flow<DetailState> = mutableStateFlow.filterNotNull()
        .onEach { Timber.i("Current State: $it") }

    override fun input(input: DetailInput) {
        Timber.i("Current Input: $input")

        val previousState = mutableStateFlow.value

        when (input) {
            DetailInput.Save -> {
                scope.launch {
                    if (previousState != null) {
                        upsertNoteUseCase(
                            previousState.id,
                            previousState.title,
                            previousState.contents
                        )
                    }
                }
                mutableStateFlow.value = DetailState(isFinished = true)
            }
            DetailInput.Delete -> {
                scope.launch {
                    if (previousState != null) {
                        deleteNoteByIdUseCase(previousState.id)
                    }
                }
                mutableStateFlow.value = DetailState(isFinished = true)
            }
            is DetailInput.FirstLoad -> {
                scope.launch {
                    val note = findNoteByIdUseCase(input.id)

                    mutableStateFlow.value = DetailState(
                        id = note?.id ?: NEW_NOTE_ID,
                        title = note?.title.orEmpty(),
                        contents = note?.contents.orEmpty(), isFinished = false
                    )
                }
            }
            is DetailInput.TitleChange -> {
                mutableStateFlow.value = previousState?.copy(title = input.title)
            }
            is DetailInput.ContentChange -> {
                mutableStateFlow.value = previousState?.copy(contents = input.content)
            }
        }
    }
}

interface Detail {
    val state: Flow<DetailState>

    fun input(input: DetailInput)
}
