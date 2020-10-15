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

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Dimension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModelProvider
import com.nicholasdoglio.notes.util.DispatcherProvider

/**
 *
 *
 *
 * @param id The initial [com.nicholasdoglio.notes.db.Note] identifier,
 * @param factory a [ViewModelProvider.Factory] that is used to create our [androidx.lifecycle.ViewModel].
 * @param dispatcherProvider a container for Coroutine [kotlinx.coroutines.Dispatchers] to launch async work.
 * @param popBack a function that is called to navigate out of this screen.
 */
@Composable
fun DetailView(
    id: Long,
    factory: ViewModelProvider.Factory,
    dispatcherProvider: DispatcherProvider,
    popBack: () -> Unit
) {
    val viewModel = viewModel<DetailViewModel>(factory = factory)

    val state = viewModel.state.collectAsState(
        initial = DetailState.Idle,
        context = dispatcherProvider.main
    )

    val currentState = state.value

    if (currentState is DetailState.Content) {
        if (currentState.isFinished) {
            popBack()
        }

        // No Note exists yet
        if (currentState.id != -id) viewModel.input(DetailInput.FirstLoad(id))

        NoteView(currentState, viewModel)
    }
}

@Composable
private fun NoteView(
    state: DetailState.Content,
    viewModel: DetailViewModel
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (title, content, save, discard) = createRefs()

        TextField(
            value = state.title,
            onValueChange = { viewModel.input(DetailInput.TitleChange(it)) },
            label = {},
            modifier = Modifier
                .padding(PADDING.dp)
                .constrainAs(title) {
                    width = Dimension.fillToConstraints
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                }
        )

        TextField(
            value = state.contents,
            onValueChange = { viewModel.input(DetailInput.ContentChange(it)) },
            modifier = Modifier
                // TODO figure out scrollable
                // .scrollable(orientation = Orientation.Vertical, rememberScrollableController())
                .background(Color.White)
                .padding(PADDING.dp)
                .constrainAs(content) {
                    width = Dimension.fillToConstraints

                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            label = {},
        )

        Button(
            onClick = { viewModel.input(DetailInput.Save) },
            modifier = Modifier
                .padding(PADDING.dp)
                .constrainAs(save) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Text("SAVE")
        }

        Button(
            onClick = { viewModel.input(DetailInput.Delete) },
            modifier = Modifier
                .padding(PADDING.dp)
                .constrainAs(discard) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Text("DISCARD")
        }
    }
}

private const val PADDING = 8
