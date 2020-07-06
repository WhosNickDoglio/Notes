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

import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.compose.launchInComposition
import androidx.lifecycle.ViewModelProvider
import androidx.ui.foundation.TextField
import androidx.ui.foundation.VerticalScroller
import androidx.ui.input.TextFieldValue
import androidx.ui.layout.Column
import androidx.ui.viewmodel.viewModel
import com.doglio.shared.util.DispatcherProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect

@Composable
fun DetailView(
    id: Long,
    factory: ViewModelProvider.Factory,
    dispatcherProvider: DispatcherProvider,
    popBack: () -> Unit
) {
    val input = MutableStateFlow<DetailInput>(DetailInput.FirstLoad(id))
    val viewModel = viewModel<DetailViewModel>(factory = factory)

    launchInComposition { input.collect { viewModel.input(it) } }

    val state = viewModel.state.collectAsState(
        initial = DetailState(),
        context = dispatcherProvider.main
    )

    if (state.value.isFinished) {
        popBack()
    }
    Column {
        TextField(
            value = TextFieldValue("This is my title"),
            onValueChange = { input.value = DetailInput.TitleChange(it.text) }
        )

        VerticalScroller {
            TextField(
                value = TextFieldValue("This is my content"),
                onValueChange = { input.value = DetailInput.ContentChange(it.text) }
            )
        }
    }
}
