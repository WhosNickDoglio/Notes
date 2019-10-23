/*
 * MIT License
 *
 * Copyright (c) 2019 Nicholas Doglio
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

package com.nicholasdoglio.notes.ui.note

import androidx.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.model.Note
import com.nicholasdoglio.notes.data.repo.Repository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

// https://www.reddit.com/r/androiddev/comments/976m70/a_functional_approach_to_mvvm_which_lets_you/
// https://quickbirdstudios.com/blog/app-architecture-our-functional-mvvm-approach-with-rx/
// https://www.slideshare.net/QuickBirdStudios/mvvm-with-kotlin-making-ios-and-android-apps-as-similar-as-possible

class NoteViewModel @Inject constructor(private val repository: Repository<Note>) : ViewModel() {

    // INPUT
    val triggerUpsert = PublishSubject.create<Unit>()

    val triggerDelete = PublishSubject.create<Unit>()

    val inputNoteId = PublishSubject.create<Long>()

    // OUTPUT
    private val _note: PublishSubject<Note> = PublishSubject.create()

    val note: Observable<Note> = _note.hide()

    val input = inputNoteId
        .flatMapMaybe { repository.findItemById(it) }

    val upsert = triggerUpsert
        .flatMap { _note }
        .flatMapCompletable { repository.upsert(it) }

    val delete = triggerDelete.flatMap { _note }
        // TODO check if ID is above -1 here?
        .flatMapCompletable { repository.delete(it) }
}
