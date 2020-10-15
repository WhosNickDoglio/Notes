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

import androidx.compose.foundation.Text
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModelProvider
import com.nicholasdoglio.notes.db.Note

@Composable
fun DiscardNoteView(
    note: Note,
    factory: ViewModelProvider.Factory
) {
    val viewModel = viewModel<DiscardViewModel>(factory = factory)

    var showDialog by remember { mutableStateOf(true) }

    val dismissDialog = { showDialog = false }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            text = { Text(text = "Would you like to discard or save this note?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.input(DiscardInput.Save(note))
                        dismissDialog()
                    }
                ) { Text("SAVE") }
            },
            dismissButton = {
                Button(
                    onClick = {
                        viewModel.input(DiscardInput.Delete(note.id))
                        dismissDialog()
                    }
                ) { Text("DISCARD") }
            }
        )
    }
}
