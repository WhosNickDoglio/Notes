package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.jakewharton.rxbinding2.widget.textChanges
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.note.Note
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.util.Const
import com.nicholasdoglio.notes.util.inflate
import com.nicholasdoglio.notes.util.setEditableText
import com.nicholasdoglio.notes.util.setupToolbar
import com.nicholasdoglio.notes.viewmodel.NotesViewModelFactory
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import dagger.android.support.DaggerFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_note.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class NoteFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: NotesViewModelFactory

    @Inject
    lateinit var navigationController: NavigationController

    private lateinit var currentTitle: String
    private lateinit var currentContents: String
    private var buttonsEnabled: Boolean = false
    private val currentNote: BehaviorRelay<Note> = BehaviorRelay.create()
    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val noteViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NoteViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupToolbar(activity as AppCompatActivity, noteToolbar, optionsMenu = true)

        if (oldNoteFound()) {
            noteViewModel.start(arguments!!.getLong(Const.noteFragmentArgumentId))
                .subscribeOn(Schedulers.io())
                .map {
                    currentNote.accept(it)
                    noteViewModel.id(it.id)
                }
                .autoDisposable(scopeProvider)
                .subscribe()
        } else {
            noteViewModel.id(0)
        }

        setupButtonObservables()

        if (savedInstanceState != null) {
            val note = Note(
                0,
                savedInstanceState.getString(Const.noteFragmentTitleKey),
                savedInstanceState.getString(Const.noteFragmentContentsKey)
            )
            currentNote.accept(note)
        }

        noteTitle.textChanges()
            .autoDisposable(scopeProvider)
            .subscribe {
                noteViewModel.title(it.toString())
                currentTitle = it.toString()
            }

        noteContent.textChanges()
            .autoDisposable(scopeProvider)
            .subscribe {
                noteViewModel.contents(it.toString())
                currentContents = it.toString()
            }
    }

    override fun onResume() {
        super.onResume()
        if (oldNoteFound()) {
            currentNote
                .subscribeOn(AndroidSchedulers.mainThread())
                .map { openOldNote(it) }
                .autoDisposable(scopeProvider)
                .subscribe()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_note)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(Const.noteFragmentTitleKey, currentTitle)
        outState.putString(Const.noteFragmentContentsKey, currentContents)
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

    private fun oldNoteFound() = arguments!!.getLong(Const.noteFragmentArgumentId) > 0

    private fun openOldNote(note: Note) {
        noteTitle.setEditableText(note.title)
        noteContent.setEditableText(note.contents)
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

    private fun showAction(clickAction: String) { //These are very slow in Marshmallow
        if (this.buttonsEnabled) {
            when (clickAction) {
                "Saved" -> noteViewModel.saveNote(arguments!!.getLong(Const.noteFragmentArgumentId))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .autoDisposable(scopeProvider)
                    .subscribe { returnToList("Note $clickAction") }
                "Deleted" -> deleteNote(clickAction)
            }
        } else {
            toast(getString(R.string.empty_note_toast))
        }
    }

    private fun deleteNote(clickAction: String) {
        if (arguments!!.getLong(Const.noteFragmentArgumentId).toInt() == 0) {
            returnToList("Note $clickAction")
        } else {
            noteViewModel.deleteNote(currentNote.value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .autoDisposable(scopeProvider)
                .subscribe { returnToList("Note $clickAction") }
        }
    }

    companion object {
        fun create(id: Long): NoteFragment {
            val fragment = NoteFragment()
            val arg = Bundle()
            arg.putLong(Const.noteFragmentArgumentId, id)
            fragment.arguments = arg
            return fragment
        }
    }
}