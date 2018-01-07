package com.nicholasdoglio.notes.ui.list

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
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

    //TODO empty Recyclerview greeting?

    //TODO swipe to delete?

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
        notesListAdapter = NoteListAdapter(navigationController)
        notesListViewModel =
                ViewModelProviders.of(this,
                        viewModelFactory).get(NoteListViewModel::class.java)
        notesListViewModel.notesList.observe(this,
                Observer(notesListAdapter::setList))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        (activity as AppCompatActivity).setSupportActionBar(noteListToolbar)
        setHasOptionsMenu(true)
        setupList()

        compositeDisposable += createNoteFab.clicks()
                .subscribe { navigationController.openNote() }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.about_item -> navigationController.openAbout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupList() {
        val layoutManager = LinearLayoutManager(this.context)
        val divider = DividerItemDecoration(this.context, layoutManager.orientation)

        notesListRecyclerView.adapter = notesListAdapter
        notesListRecyclerView.layoutManager = layoutManager
        notesListRecyclerView.addItemDecoration(divider)
        notesListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0)
                    createNoteFab.hide()
                else if (dy < 0)
                    createNoteFab.show()
            }
        })
        startPostponedEnterTransition() //I have no idea if this does anything
        // transitions are inconsistent depending on device
    }


    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }
}