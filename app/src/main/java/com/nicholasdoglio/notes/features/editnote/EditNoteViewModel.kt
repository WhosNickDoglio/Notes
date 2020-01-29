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
import com.jakewharton.rxrelay2.PublishRelay
import com.nicholasdoglio.notes.Note
import com.nicholasdoglio.notes.data.note.NoteRepository
import com.nicholasdoglio.notes.util.SchedulersProvider
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.withLatestFrom
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

// https://www.reddit.com/r/androiddev/comments/976m70/a_functional_approach_to_mvvm_which_lets_you/
// https://quickbirdstudios.com/blog/app-architecture-our-functional-mvvm-approach-with-rx/
// https://www.slideshare.net/QuickBirdStudios/mvvm-with-kotlin-making-ios-and-android-apps-as-similar-as-possible

class EditNoteViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val schedulersProvider: SchedulersProvider
) : ViewModel() {

    private val inputInsert = PublishRelay.create<Unit>()

    private val inputDelete = PublishRelay.create<Unit>()

    private val inputNoteId = PublishRelay.create<Long>()

    private val titleRelay = PublishRelay.create<String>()

    private val contentsRelay = PublishRelay.create<String>()

    private val note: Observable<Note> = Observables.combineLatest(
        titleRelay,
        contentsRelay
    ) { title, contents ->
        UiNote(
            title = title,
            contents = contents
        )
    }

    fun inputId(id: Long) {
        inputNoteId.accept(id)
    }

    fun inputTitle(title: String) {
        titleRelay.accept(title)
    }

    fun inputContents(contents: String) {
        contentsRelay.accept(contents)
    }

    fun triggerInsert() {
        inputInsert.accept(Unit)
    }

    fun triggerDelete() {
        inputDelete.accept(Unit)
    }

    val title = titleRelay.hide().firstElement()

    val contents = contentsRelay.hide().firstElement()

    val input = inputNoteId
        .subscribeOn(schedulersProvider.background)
        .flatMapMaybe { repository.findItemById(it) }
        .map {
            titleRelay.accept(it.title.orEmpty())
            contentsRelay.accept(it.contents.orEmpty())
        }

    val insert = inputInsert
        .subscribeOn(schedulersProvider.background)
        .withLatestFrom(note)
        .flatMapCompletable { repository.insert(it.second) }

    val delete = inputDelete
        .subscribeOn(schedulersProvider.background)
        .withLatestFrom(note)
        .flatMapCompletable { repository.delete(it.second) }
}

private data class UiNote(
    override val id: Long = 0,
    override val title: String?,
    override val contents: String?,
    override val timestamp: LocalDateTime = LocalDateTime.now()
) : Note
