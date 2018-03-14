package com.nicholasdoglio.notes.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.jakewharton.rxbinding2.view.clicks
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.util.hideOnScroll
import com.nicholasdoglio.notes.util.inflate
import com.nicholasdoglio.notes.util.setupToolbar
import com.nicholasdoglio.notes.util.showIf
import com.nicholasdoglio.notes.viewmodel.NotesViewModelFactory
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_note_list.*
import javax.inject.Inject


/**
 * @author Nicholas Doglio
 */
class NoteListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: NotesViewModelFactory

    @Inject
    lateinit var navigationController: NavigationController

    private val noteListObserver by lazy { Observer(notesListAdapter::submitList) }
    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val notesListAdapter by lazy { NoteListAdapter() }
    private val notesListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NoteListViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        notesListViewModel.notesList.observe(this, noteListObserver)

        setupToolbar(activity as AppCompatActivity, noteListToolbar, "Notes", true)

        notesListRecyclerView.apply {
            adapter = notesListAdapter
            layoutManager = LinearLayoutManager(this.context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    createNoteFab.hideOnScroll(dy)
                }
            })
        }

        notesListViewModel.checkForNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                emptyList.showIf(it == 0)
                notesListRecyclerView.showIf(it > 0)
            }
            .autoDisposable(scopeProvider)
            .subscribe()
    }

    override fun onResume() {
        super.onResume()

        notesListAdapter.noteListItemClickListener()
            .map { navigationController.openNote(it.id) }
            .autoDisposable(scopeProvider)
            .subscribe()

        createNoteFab.clicks()
            .map { navigationController.openNote() }
            .autoDisposable(scopeProvider)
            .subscribe()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_note_list)

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.about_item -> {
            navigationController.openAbout()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        notesListViewModel.notesList.removeObserver(noteListObserver)
    }
}