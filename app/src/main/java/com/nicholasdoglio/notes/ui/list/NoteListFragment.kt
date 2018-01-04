package com.nicholasdoglio.notes.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.ui.viewmodel.NotesViewModelFactory
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_note_list.*
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: NotesViewModelFactory

    @Inject
    lateinit var navigationController: NavigationController

    private lateinit var notesListAdapter: NoteListAdapter
    private lateinit var notesListViewModel: NoteListViewModel
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
        notesListAdapter = NoteListAdapter()
        notesListViewModel =
                ViewModelProviders.of(this,
                        viewModelFactory).get(NoteListViewModel::class.java)
        notesListViewModel.notesList.observe(this,
                Observer(notesListAdapter::setList))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesListRecyclerView.adapter = notesListAdapter
        notesListRecyclerView.layoutManager = LinearLayoutManager(this.context)

        compositeDisposable += createNoteFab.clicks()
                .subscribe { navigationController.openNote() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}