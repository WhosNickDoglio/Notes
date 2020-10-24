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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicholasdoglio.notes.db.Note

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
