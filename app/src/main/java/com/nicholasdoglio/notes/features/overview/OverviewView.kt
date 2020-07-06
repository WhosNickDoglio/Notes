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

package com.nicholasdoglio.notes.features.overview

import androidx.compose.Composable
import androidx.compose.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.ui.core.Modifier
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.foundation.clickable
import androidx.ui.foundation.lazy.LazyColumnItems
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.graphics.Color
import androidx.ui.layout.Column
import androidx.ui.material.BottomAppBar
import androidx.ui.material.FloatingActionButton
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Scaffold
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Edit
import androidx.ui.viewmodel.viewModel
import com.doglio.shared.util.DispatcherProvider
import com.nicholasdoglio.shared.db.Note

@Composable
fun OverviewView(
    factory: ViewModelProvider.Factory,
    dispatcherProvider: DispatcherProvider,
    navigateToNote: (id: Long) -> Unit
) {
    val viewModel = viewModel<OverviewViewModel>(factory = factory)
    val state = viewModel.state.collectAsState(
        initial = OverviewState(),
        context = dispatcherProvider.main
    )

    Scaffold(
        bottomBar = {
            BottomAppBar(cutoutShape = CircleShape, backgroundColor = Color.White) {}
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToNote(0) },
                shape = CircleShape,
                backgroundColor = MaterialTheme.colors.primary
            ) { Icon(asset = Icons.Filled.Edit) }
        },
        floatingActionButtonPosition = Scaffold.FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bodyContent = {
            if (state.value.isEmpty) {
                emptyView()
            } else {
                LazyColumnItems(items = state.value.data) { note ->
                    NoteListItemView(
                        note = note,
                        onClick = {
                            navigateToNote(it)
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun emptyView() {
    // TODO make this look not terrible
    Column {
        Text("No Notes")
        Text("Click the create button to add your first note!")
    }
}

// TODO make this look not terrible
@Composable
fun NoteListItemView(note: Note, onClick: (id: Long) -> Unit) {
    Column(modifier = Modifier.clickable(onClick = { onClick(note.id) })) {
        Text(note.title.orEmpty())
        Text(note.contents.orEmpty())
    }
}
