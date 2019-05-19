package com.nicholasdoglio.notes.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding3.view.clicks
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.about.AboutFragment
import com.nicholasdoglio.notes.ui.base.NotesFragment
import com.nicholasdoglio.notes.util.createViewModel
import com.nicholasdoglio.notes.util.hideOnScroll
import com.nicholasdoglio.notes.util.inflate
import com.nicholasdoglio.notes.util.showIf
import com.uber.autodispose.android.lifecycle.autoDisposable
import kotlinx.android.synthetic.main.fragment_note_list.*

/**
 * @author Nicholas Doglio
 */
class NoteListFragment : NotesFragment<NoteListViewModel>() {

    override fun createViewModel() {
        viewModel = createViewModel(viewModelFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_note_list)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notesListAdapter = NoteListAdapter()

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(true)
            title = "Notes"
            setHasOptionsMenu(true)
        }

        notesRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = notesListAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    createNoteFab.hideOnScroll(dy)
                }
            })
        }

        createNoteFab.clicks()
            .autoDisposable(viewLifecycleOwner)
            .subscribe { findNavController().navigate(NoteListFragmentDirections.openNote()) }

        viewModel.notesList
            .subscribeOn(appSchedulers.database)
            .observeOn(appSchedulers.main)
            .autoDisposable(viewLifecycleOwner)
            .subscribe { list -> notesListAdapter.submitList(list) }

        viewModel.checkForNotes
            .subscribeOn(appSchedulers.database)
            .observeOn(appSchedulers.main)
            .autoDisposable(viewLifecycleOwner)
            .subscribe { count ->
                emptyStateView.showIf(count == 0)
                notesRecyclerView.showIf(count > 0)
            }

        notesListAdapter.noteListItemClickListener
            .autoDisposable(viewLifecycleOwner)
            .subscribe { findNavController().navigate(NoteListFragmentDirections.openNote(it.id)) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.about_item -> {
            AboutFragment().show(requireFragmentManager(), "ABOUT")
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
