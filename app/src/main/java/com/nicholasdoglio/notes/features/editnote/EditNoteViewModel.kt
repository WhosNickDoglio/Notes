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

package com.nicholasdoglio.notes.features.editnote

import androidx.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.NoteRepository
import com.nicholasdoglio.notes.util.DispatcherProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

// https://www.reddit.com/r/androiddev/comments/976m70/a_functional_approach_to_mvvm_which_lets_you/
// https://quickbirdstudios.com/blog/app-architecture-our-functional-mvvm-approach-with-rx/
// https://www.slideshare.net/QuickBirdStudios/mvvm-with-kotlin-making-ios-and-android-apps-as-similar-as-possible

class EditNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable)
    }

    // TODO I shouldn't cancel this since it does all the DB transactions?
    private val scope = CoroutineScope(dispatcherProvider.main + exceptionHandler)

    private val idChannel = ConflatedBroadcastChannel<Long>()

    val inputInsert = ConflatedBroadcastChannel<Unit>()

    val inputDelete = ConflatedBroadcastChannel<Unit>()

    val inputNoteId = ConflatedBroadcastChannel<Long>()

    val titleChannel = ConflatedBroadcastChannel<String>()

    val contentChannel = ConflatedBroadcastChannel<String>()

    val title = titleChannel.asFlow()
        .flowOn(dispatcherProvider.background)

    val contents = contentChannel.asFlow()
        .flowOn(dispatcherProvider.background)

    val isEmpty: Flow<Boolean> = combine(
        title,
        contents,
        transform = { title, contents -> title.isEmpty() && contents.isEmpty() })

    private val note: Flow<UiNote> = combine(
        idChannel.asFlow(),
        titleChannel.asFlow(),
        contentChannel.asFlow(),
        transform = { id, title, contents -> UiNote(id = id, title = title, contents = contents) })
        .flowOn(dispatcherProvider.background)

    private val input = inputNoteId.asFlow()
        .flowOn(dispatcherProvider.background)
        .map { repository.findItemById(it) }
        // TODO I don't like this
        .onEach { idChannel.send(it?.id ?: -1L) }
        .onEach { titleChannel.send(it?.title.orEmpty()) }
        .onEach { contentChannel.send(it?.contents.orEmpty()) }

    private val insert = inputInsert.asFlow()
        .flowOn(dispatcherProvider.background)
        .flatMapConcat { note }
        .map { repository.upsert(it.id, it.title.orEmpty(), it.contents.orEmpty()) }

    private val delete = inputDelete.asFlow()
        .flowOn(dispatcherProvider.background)
        .flatMapConcat { note }
        .map { repository.deleteById(it.id) }

    init {
        input.launchIn(scope)
        insert.launchIn(scope)
        delete.launchIn(scope)
    }
}

private data class UiNote(
    val id: Long = -1L,
    val title: String?,
    val contents: String?
)
