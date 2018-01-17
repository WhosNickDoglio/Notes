package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.TextView
import com.jakewharton.rxbinding2.widget.textChanges
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.note.Note
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.ui.viewmodel.NotesViewModelFactory
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
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

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }
    private lateinit var noteViewModel: NoteViewModel
    private var currentNote: Note? = null
    private var buttonsEnabled: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProviders.of(this, viewModelFactory).get(NoteViewModel::class.java)

        if (checkForOldNoteId()) {
            noteViewModel.start(arguments!!.getLong(argNoteid))
                    .subscribeOn(Schedulers.io())
                    .map {
                        currentNote = it
                        noteViewModel.id(it.id)
                    }
                    .autoDisposable(scopeProvider)
                    .subscribe()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (checkForOldNoteId()) {
            Single.just(currentNote)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .map { openOldNote(it) }
                    .autoDisposable(scopeProvider)
                    .subscribe()
        }

        setupToolbar()
        setupButtonObservables()

        noteTitle.textChanges()
                .autoDisposable(scopeProvider)
                .subscribe { noteViewModel.title(it.toString()) }

        noteContent.textChanges()
                .autoDisposable(scopeProvider)
                .subscribe { noteViewModel.contents(it.toString()) }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.save_item -> showAction("Saved")
                R.id.delete_item -> showAction("Deleted")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun returnToList(finishedToast: String) {
        navigationController.openList()
        toast(finishedToast)
    }

    private fun openOldNote(note: Note) {
        noteTitle.setText(note.title, TextView.BufferType.EDITABLE)
        noteContent.setText(note.contents, TextView.BufferType.EDITABLE)
    }

    private fun checkForOldNoteId() = arguments!!.getLong(argNoteid) > 0

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(noteToolbar)

        noteToolbar.title = ""

        setHasOptionsMenu(true)
    }

    private fun setupButtonObservables() {
        //clean this up and add some toasts and maybe an animation?

        //can I move any of this into the ViewModel?
        val isTitleEmpty = noteTitle.textChanges().map { it.isNotEmpty() }
        val isContentsEmpty = noteContent.textChanges().map { it.isNotEmpty() }

        Observable.combineLatest(isTitleEmpty, isContentsEmpty,
                BiFunction<Boolean, Boolean, Boolean> { firstBool, secondBool -> firstBool && secondBool })
                .distinctUntilChanged()
                .autoDisposable(scopeProvider)
                .subscribe { buttonsEnabled = it }
    }

    private fun showAction(clickAction: String) {
        if (this.buttonsEnabled) {
            when (clickAction) {
                "Saved" -> noteViewModel.saveNote(arguments!!.getLong(argNoteid))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .autoDisposable(scopeProvider)
                        .subscribe { returnToList("Note $clickAction") }
                "Deleted" -> Completable.fromAction { noteViewModel.deleteNote(currentNote!!) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .autoDisposable(scopeProvider)
                        .subscribe { returnToList("Note $clickAction") }
            }
        } else {
            toast("This note is empty! Try writing something.")
        }
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