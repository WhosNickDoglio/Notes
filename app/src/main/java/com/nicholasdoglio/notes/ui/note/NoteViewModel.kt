package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nicholasdoglio.notes.data.model.note.Note
import com.nicholasdoglio.notes.data.repo.NoteRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    private val titleSubject: BehaviorRelay<String> = BehaviorRelay.create()
    private val contentSubject: BehaviorRelay<String> = BehaviorRelay.create()
    private val idSubject: BehaviorRelay<Long> = BehaviorRelay.create()
    private val noteSubject: BehaviorRelay<Note> = BehaviorRelay.create()
    private lateinit var titleEmpty: Observable<Boolean>
    private lateinit var contentsEmpty: Observable<Boolean>


    fun title(title: String) = titleSubject.accept(title)

    fun contents(content: String) = contentSubject.accept(content)

    fun id(id: Long) = idSubject.accept(id)

    fun note(note: Note) = noteSubject.accept(note)

    fun currentTitle(): String = titleSubject.value

    fun currentContent(): String = contentSubject.value

    fun currentId(): Long = idSubject.value

    fun currentNote() = noteSubject

    fun start(id: Long): Single<Note> = noteRepository.getNote(id)

    fun isTitleEmpty(titleEmpty: Observable<Boolean>) {
        this.titleEmpty = titleEmpty
    }

    fun isContentsEmpty(contentsEmpty: Observable<Boolean>) {
        this.contentsEmpty = contentsEmpty
    }

    fun isNoteEmpty(): Observable<Boolean> = Observable.combineLatest(
        titleEmpty,
        contentsEmpty,
        BiFunction<Boolean, Boolean, Boolean> { firstBool, secondBool -> firstBool && secondBool })
        .distinctUntilChanged()


    fun deleteNote(note: Note): Completable = Completable.fromAction {
        noteRepository.deleteNote(note)
    }

    fun saveNote(id: Long): Completable = Single.just(id > 0)
        .map { saveOrUpdate(it) }
        .toCompletable()


    private fun createNote(): Note = Note(
        idSubject.value,
        titleSubject.value,
        contentSubject.value
    )

    private fun saveOrUpdate(doesNoteAlreadyExist: Boolean) {
        when (doesNoteAlreadyExist) {
            true -> noteRepository.updateNote(createNote())
            false -> noteRepository.saveNote(createNote())
        }
    }
}