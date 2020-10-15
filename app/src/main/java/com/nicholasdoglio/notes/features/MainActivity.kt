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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Providers
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.ViewModelProvider
import com.github.zsoltk.compose.backpress.AmbientBackPressHandler
import com.github.zsoltk.compose.backpress.BackPressHandler
import com.nicholasdoglio.notes.di.injector
import com.nicholasdoglio.notes.util.DispatcherProvider
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private val backPressHandler = BackPressHandler()

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            Providers(AmbientBackPressHandler provides backPressHandler) {
                MaterialTheme {
                    NotesApp(
                        factory = factory,
                        dispatcherProvider = dispatcherProvider
                    )
                }
            }
        }
    }

    override fun onBackPressed() {
        if (!backPressHandler.handle()) {
            super.onBackPressed()
        }
    }
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
//                    navigateToNote = { stack.push(Screen.Detail(it)) }
                    navigateToNote = { stack.push(Screen.Discard(Note(0, "", "", LocalDateTime.now()))) }
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

// @Composable
// fun NotesApp() {
// }

sealed class Screen {
    object Overview : Screen()
    data class Detail(val id: Long) : Screen()
    data class Discard(val note: Note) : Screen()
//    object DayNightToggle: Screen()
//    object About: Screen()
}
