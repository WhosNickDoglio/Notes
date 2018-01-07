package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.note.Note
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.ui.viewmodel.NotesViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_note.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: NotesViewModelFactory

    @Inject
    lateinit var navigationController: NavigationController

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var compositeDisposable: CompositeDisposable

    private var currentNote: Note? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
        noteViewModel = ViewModelProviders.of(this,
                viewModelFactory).get(NoteViewModel::class.java)

        if (openOldNote()) {
            compositeDisposable += noteViewModel.start(arguments!!.getLong(argNoteid))
                    .subscribeOn(Schedulers.io())
                    .map { currentNote = it }
                    .subscribe()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (openOldNote()) {
            compositeDisposable += Single.just(currentNote)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .map { handleNote(it) }
                    .subscribe()
        }

        compositeDisposable += noteTitle.textChanges()
                .subscribe { noteViewModel.updateTitle(it.toString()) }

        compositeDisposable += noteContent.textChanges()
                .subscribe { noteViewModel.updateContent(it.toString()) }

        //These bottom two shouldn't work if the Note is empty
        compositeDisposable += saveNoteButton.clicks()
                .observeOn(Schedulers.io())
                .map { noteViewModel.saveNote() } //Check for true or false
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { returnToList("Note saved") }

        compositeDisposable += deleteNoteButton.clicks()
                .observeOn(Schedulers.io())
                .flatMapSingle { noteViewModel.getNote(currentNote?.id!!) }
                .map { noteViewModel.deleteNote(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { returnToList("Note deleted") }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    private fun returnToList(finishedToast: String) {
        navigationController.openList()
        toast(finishedToast)
    }

    private fun openOldNote() = arguments!!.getLong(argNoteid) > 0

    private fun handleNote(note: Note) {
        noteTitle.setText(note.title, TextView.BufferType.EDITABLE)
        noteContent.setText(note.contents, TextView.BufferType.EDITABLE)
    }

    companion object {
        private val argNoteid = "NOTE_ID"

        fun create(id: Long): NoteFragment {
            val fragment = NoteFragment()
            val arg = Bundle()
            arg.putLong(argNoteid, id)
            fragment.arguments = arg
            return fragment
        }
    }
}