package com.nicholasdoglio.notes.ui.note

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.jakewharton.rxbinding2.widget.textChanges
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
import io.reactivex.android.schedulers.AndroidSchedulers
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

    private var buttonsEnabled: Boolean = false
    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val noteViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(NoteViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupToolbar(activity as AppCompatActivity, noteToolbar, optionsMenu = true)

        when (oldNoteFound()) {
            true -> noteViewModel.start(arguments!!.getLong(Const.noteFragmentArgumentId))
                .subscribeOn(Schedulers.io())
                .map {
                    noteViewModel.note(it)
                    noteViewModel.id(it.id)
                }
                .autoDisposable(scopeProvider)
                .subscribe()
            false -> noteViewModel.id(0)
        }

        //TODO need to be able to discard a note with an empty title or contents

        //clean this up and add some toasts and maybe an animation?
        noteViewModel.isTitleEmpty(noteTitle.textChanges().map { it.isNotEmpty() })
        noteViewModel.isContentsEmpty(noteContent.textChanges().map { it.isNotEmpty() })
        noteViewModel.isNoteEmpty()
            .map { buttonsEnabled = it }
            .autoDisposable(scopeProvider)
            .subscribe()

        savedInstanceState?.let {
            noteViewModel.note(
                Note(
                    noteViewModel.currentId(),
                    savedInstanceState.getString(Const.noteFragmentTitleKey),
                    savedInstanceState.getString(Const.noteFragmentContentsKey)
                )
            )
        }

        noteTitle.textChanges()
            .map { noteViewModel.title(it.toString()) }
            .autoDisposable(scopeProvider)
            .subscribe()

        noteContent.textChanges()
            .map { noteViewModel.contents(it.toString()) }
            .autoDisposable(scopeProvider)
            .subscribe()
    }

    override fun onResume() {
        super.onResume()
        oldNoteFound().let {
            noteViewModel.currentNote()
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
        outState.putString(Const.noteFragmentTitleKey, noteViewModel.currentTitle())
        outState.putString(Const.noteFragmentContentsKey, noteViewModel.currentContent())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.save_item -> showAction("saved")
                R.id.delete_item -> showAction("deleted")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDiscardAlert() {

    }

    private fun returnToList(finished: String) {
        navigationController.openList()
        toast(finished)
    }

    private fun oldNoteFound() = arguments!!.getLong(Const.noteFragmentArgumentId) > 0

    private fun openOldNote(note: Note) {
        noteTitle.setEditableText(note.title)
        noteContent.setEditableText(note.contents)
    }

    private fun showAction(clickAction: String) { //These are very slow on Marshmallow
        if (buttonsEnabled) {
            when (clickAction) {
                "saved" -> saveNote(clickAction)
                "deleted" -> deleteNote(clickAction)
            }
        } else {
            toast(getString(R.string.empty_note_toast))
        }
    }

    private fun saveNote(clickAction: String) {
        noteViewModel.saveNote(arguments!!.getLong(Const.noteFragmentArgumentId))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scopeProvider)
            .subscribe { returnToList("Note $clickAction") }
    }

    private fun deleteNote(clickAction: String) {
        if (arguments!!.getLong(Const.noteFragmentArgumentId).toInt() == 0) {
            returnToList("Note $clickAction")
        } else {
            noteViewModel.deleteNote(noteViewModel.currentNote().value)
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