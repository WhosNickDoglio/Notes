package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.ui.viewmodel.NotesViewModelFactory
import dagger.android.support.AndroidSupportInjection
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        compositeDisposable += noteTitle.textChanges()
                .subscribe {
                    noteViewModel.updateTitle(it.toString())
                }

        compositeDisposable += noteContent.textChanges()
                .subscribe {
                    noteViewModel.updateContent(it.toString())
                }

        //These bottom two shouldn't work if the Note is empty
        compositeDisposable += saveNoteButton.clicks()
                .observeOn(Schedulers.io())
                .map { noteViewModel.saveNote() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { returnToList("Note saved") }

        compositeDisposable += deleteNoteButton.clicks()
                .observeOn(Schedulers.io())
                .map { noteViewModel.deleteNote() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { returnToList("Note deleted") }
    }

    private fun returnToList(finishedToast: String) {
        navigationController.openList()
        toast(finishedToast)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}