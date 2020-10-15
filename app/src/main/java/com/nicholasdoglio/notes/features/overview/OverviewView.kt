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
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Divider
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModelProvider
import com.nicholasdoglio.notes.db.Note
import com.nicholasdoglio.notes.util.DispatcherProvider

@Composable
fun OverviewView(
    factory: ViewModelProvider.Factory,
    dispatcherProvider: DispatcherProvider,
    navigateToNote: (id: Long) -> Unit
) {
    // TODO this is stateful, do I want it to be?
    val viewModel = viewModel<OverviewViewModel>(factory = factory)
    val state = viewModel.state.collectAsState(
        initial = OverviewState(),
        context = dispatcherProvider.main
    )

    Scaffold(
        bottomBar = { BottomAppBar(cutoutShape = CircleShape, backgroundColor = Color.White) {} },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToNote(-1L) },
                shape = CircleShape,
                backgroundColor = MaterialTheme.colors.primary
            ) { Icon(asset = Icons.Filled.Edit) }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bodyContent = {
            if (state.value.isEmpty) {
                EmptyView()
            } else {
                LazyColumnFor(
                    items = state.value.data,
                    itemContent = { note ->
                        NoteListItemView(
                            note = note,
                            onClick = { navigateToNote(it) }
                        )
                    }
                )
            }
        }
    )
}

@Composable
fun EmptyView() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (header, message) = createRefs()

        Text(
            "No Notes",
            style = TextStyle(fontWeight = FontWeight.W700),
            modifier = Modifier
                .constrainAs(header) { centerTo(parent) },
        )
        Text(
            "Click the create button to add your first note!",
            modifier = Modifier.constrainAs(message) {
                centerHorizontallyTo(parent)
                top.linkTo(header.bottom)
            }
        )
    }
}

@Composable
fun NoteListItemView(
    note: Note,
    onClick: (id: Long) -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .absolutePadding(
                left = PADDING_EIGHT.dp,
                top = PADDING_EIGHT.dp,
                right = PADDING_EIGHT.dp,
            )
            .clickable(onClick = { onClick(note.id) })
    ) {

        Text(
            note.title.orEmpty(),
            style = TextStyle(
                fontWeight = FontWeight.W700,
                fontSize = TITLE_FONT_SIZE.sp,
            ),
            maxLines = 1,
            modifier = Modifier
                .absolutePadding(
                    left = PADDING_EIGHT.dp,
                    right = PADDING_EIGHT.dp,
                ),
        )

        Text(
            note.contents.orEmpty(),
            style = TextStyle(
                color = Color.Gray,
                fontSize = CONTENT_FONT_SIZE.sp,
            ),
            maxLines = 1,
            modifier = Modifier
                .absolutePadding(
                    left = PADDING_EIGHT.dp,
                    top = PADDING_FOUR.dp,
                    right = PADDING_EIGHT.dp,
                ),
        )

        Divider()
    }
}

private const val TITLE_FONT_SIZE = 16
private const val CONTENT_FONT_SIZE = 12
private const val PADDING_EIGHT = 8
private const val PADDING_FOUR = 4
