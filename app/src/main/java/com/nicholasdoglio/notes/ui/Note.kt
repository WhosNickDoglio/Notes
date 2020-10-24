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

package com.nicholasdoglio.notes.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Dimension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nicholasdoglio.notes.features.detail.DetailState

@Composable
fun Note(
    state: DetailState,
    onTitleChange: (title: String) -> Unit,
    onContentsChange: (contents: String) -> Unit,
    onNoteSaved: () -> Unit,
    onNoteDeleted: () -> Unit,
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (title, content, save, discard) = createRefs()

        TextField(
            value = state.title,
            onValueChange = { onTitleChange(it) },
            label = { Text("Title") },
            backgroundColor = Color.Transparent,
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
            onValueChange = { value -> onContentsChange(value) },
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
            label = { Text("Content") },
            backgroundColor = Color.Transparent,
        )

        Button(
            onClick = { onNoteSaved() },
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
            onClick = { onNoteDeleted() },
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
