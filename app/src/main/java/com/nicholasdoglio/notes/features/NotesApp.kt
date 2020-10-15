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

package com.nicholasdoglio.notes.features

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.github.zsoltk.compose.router.Router
import com.nicholasdoglio.notes.db.Note
import com.nicholasdoglio.notes.features.detail.DetailView
import com.nicholasdoglio.notes.features.discard.DiscardNoteView
import com.nicholasdoglio.notes.features.overview.OverviewView
import com.nicholasdoglio.notes.util.DispatcherProvider

@Composable
fun NotesApp(
    factory: ViewModelProvider.Factory,
    dispatcherProvider: DispatcherProvider
) {
    Stack(
        screen = Screen.Overview,
        factory = factory,
        dispatcherProvider = dispatcherProvider
    )
}

@Composable
fun Stack(
    screen: Screen,
    factory: ViewModelProvider.Factory,
    dispatcherProvider: DispatcherProvider
) {
    // TODO deep-linking
    Router(defaultRouting = screen) { stack ->
        when (val routing = stack.last()) {
            is Screen.Overview -> OverviewView(
                factory = factory,
                dispatcherProvider = dispatcherProvider,
                navigateToNote = { stack.push(Screen.Detail(it)) }
            )
            is Screen.Detail -> DetailView(
                id = routing.id,
                factory = factory,
                dispatcherProvider = dispatcherProvider,
                popBack = { stack.pop() }
            )
            is Screen.Discard -> DiscardNoteView(
                note = routing.note,
                factory = factory
            )
        }
    }
}

sealed class Screen {
    object Overview : Screen()
    data class Detail(val id: Long) : Screen()
    data class Discard(val note: Note) : Screen()
//    object DayNightToggle: Screen()
//    object About: Screen()
}
