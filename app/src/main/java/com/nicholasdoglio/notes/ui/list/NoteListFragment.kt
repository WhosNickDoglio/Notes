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
import com.nicholasdoglio.notes.util.inflate
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import dagger.android.support.AndroidSupportInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val notesListAdapter by lazy { NoteListAdapter(navigationController) }
    private val notesListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NoteListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        notesListViewModel.notesList.observe(this, Observer(notesListAdapter::setList))

        (activity as AppCompatActivity).setSupportActionBar(noteListToolbar)
        setHasOptionsMenu(true)

        val linearLayoutManager = LinearLayoutManager(this.context)
        notesListRecyclerView.apply {
            adapter = notesListAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(this.context, linearLayoutManager.orientation))
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    if (dy > 0)
                        createNoteFab.hide()
                    else if (dy < 0)
                        createNoteFab.show()
                }
            })
        }

        createNoteFab.clicks()
            .autoDisposable(scopeProvider)
            .subscribe { navigationController.openNote() }
    }

    override fun onResume() {
        super.onResume()
        notesListViewModel.checkForNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { showViews(it) }
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

    private fun showViews(numItemsInDb: Int) {
        if (numItemsInDb > 0) { //Show Note List
            emptyList.visibility = View.INVISIBLE
            notesListRecyclerView.visibility = View.VISIBLE
        } else { //Show empty view
            emptyList.visibility = View.VISIBLE
            notesListRecyclerView.visibility = View.INVISIBLE
        }
    }
}