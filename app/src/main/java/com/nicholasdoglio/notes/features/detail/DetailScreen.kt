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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.nicholasdoglio.notes.ui.Note
import com.nicholasdoglio.notes.util.DispatcherProvider

/**
 * A screen that displays the entirety of a [com.nicholasdoglio.notes.db.Note] and allows for it to
 * be edited or deleted.
 *
 * @param id The initial [com.nicholasdoglio.notes.db.Note] identifier.
 * @param viewModel A [androidx.lifecycle.ViewModel] that adheres to [Detail] and supplies state..
 * @param dispatcherProvider a container for Coroutine [kotlinx.coroutines.Dispatchers] to launch async work.
 * @param popBack a function that is called to navigate out of this screen.
 */
@Composable
fun DetailScreen(
    id: Long,
    viewModel: Detail,
    dispatcherProvider: DispatcherProvider,
    popBack: () -> Unit,
    displayDiscardDialog: (id: Long, title: String, content: String) -> Unit
) {
    val state by viewModel.state.collectAsState(
        initial = null,
        context = dispatcherProvider.main
    )

    if (state?.id != id) viewModel.input(DetailInput.FirstLoad(id))

    if (state?.isFinished == true) popBack()

    state?.let { detailState ->
        Note(
            state = detailState,
            onTitleChange = { viewModel.input(DetailInput.TitleChange(it)) },
            onContentsChange = { viewModel.input(DetailInput.ContentChange(it)) },
            onNoteSaved = { viewModel.input(DetailInput.Save) },
            onNoteDeleted = { viewModel.input(DetailInput.Delete) }
        )
    }
}
