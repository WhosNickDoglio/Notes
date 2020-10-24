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

package com.nicholasdoglio.notes.features.discard

import androidx.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.DeleteNoteByIdUseCase
import com.nicholasdoglio.notes.data.UpsertNoteUseCase
import com.nicholasdoglio.notes.di.AppCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class DiscardViewModel @Inject constructor(
        private val deleteNoteByIdUseCase: DeleteNoteByIdUseCase,
        private val upsertNoteUseCase: UpsertNoteUseCase,
        @AppCoroutineScope private val scope: CoroutineScope,
) : ViewModel(), Discard {

    private val _isFinished = MutableStateFlow(false)
    override val isFinished: Flow<Boolean> = _isFinished
        .onEach { Timber.i("Is Dialog finished: $it") }

    override fun input(input: DiscardInput) {
        Timber.i("Current Discard Input: $input")
        when (input) {
            is DiscardInput.Save -> {
                scope.launch {
                    upsertNoteUseCase(
                        input.id,
                        input.title,
                        input.content
                    )
                }
                _isFinished.value = true
            }
            is DiscardInput.Delete -> {
                scope.launch { deleteNoteByIdUseCase(input.id) }
                _isFinished.value = true
            }
        }
    }
}

interface Discard {

    val isFinished: Flow<Boolean>

    fun input(input: DiscardInput)
}
