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

import androidx.compose.foundation.Icon
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.nicholasdoglio.notes.ui.EmptyState
import com.nicholasdoglio.notes.ui.NoteList
import com.nicholasdoglio.notes.util.DispatcherProvider
import com.nicholasdoglio.notes.util.NEW_NOTE_ID

@Composable
fun OverviewView(
    viewModel: Overview,
    dispatcherProvider: DispatcherProvider,
    navigateToNote: (id: Long) -> Unit
) {
    val state by viewModel.state.collectAsState(
        initial = null,
        context = dispatcherProvider.main
    )

    Scaffold(
        bottomBar = {
            BottomAppBar(cutoutShape = CircleShape, backgroundColor = Color.White) {
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToNote(NEW_NOTE_ID) },
                shape = CircleShape,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(asset = Icons.Filled.Edit)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bodyContent = {
            if (state?.isEmpty == true) {
                EmptyState(
                    title = "No Notes",
                    message = "Click the create button to add your first note!"
                )
            } else {
                NoteList(
                    notes = state?.data.orEmpty(),
                    onItemClicked = { navigateToNote(it) }
                )
            }
        }
    )
}
