package com.nicholasdoglio.notes.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding3.widget.textChanges
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.base.NotesFragment
import com.nicholasdoglio.notes.ui.common.OnBackPressedListener
import com.nicholasdoglio.notes.util.createViewModel
import com.nicholasdoglio.notes.util.hideKeyboard
import com.nicholasdoglio.notes.util.inflate
import com.nicholasdoglio.notes.util.setEditableText
import com.uber.autodispose.android.lifecycle.autoDisposable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_note.*

/**
 * @author Nicholas Doglio
 */
class NoteFragment : NotesFragment<NoteViewModel>(), OnBackPressedListener {

    private val args: NoteFragmentArgs by navArgs()
    private val buttonsRelay: BehaviorRelay<Boolean> = BehaviorRelay.create<Boolean>()

    override fun createViewModel() {
        viewModel = createViewModel(viewModelFactory)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_note)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setHasOptionsMenu(true)
            title = ""
        }

        viewModel.start(args.noteId)
            .subscribeOn(appSchedulers.database)
            .observeOn(appSchedulers.main)
            .autoDisposable(viewLifecycleOwner)
            .subscribe { note ->
                viewModel.note(note)
                noteTitle.setEditableText(note.title)
                noteContent.setEditableText(note.contents)
            }

        Observable.combineLatest(
            noteTitle.textChanges().map { it.isNotEmpty() },
            noteContent.textChanges().map { it.isNotEmpty() },
            BiFunction<Boolean, Boolean, Boolean> { firstBool, secondBool -> firstBool && secondBool })
            .distinctUntilChanged()
            .autoDisposable(viewLifecycleOwner)
            .subscribe { buttonsRelay.accept(it) }

        noteTitle.textChanges()
            .autoDisposable(viewLifecycleOwner)
            .subscribe { viewModel.title(it.toString()) }

        noteContent.textChanges()
            .autoDisposable(viewLifecycleOwner)
            .subscribe { viewModel.contents(it.toString()) }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.save_item -> showAction(NoteAction.SAVE)
        R.id.delete_item -> showAction(NoteAction.DELETE)
        else -> super.onOptionsItemSelected(item)
    }

    override fun doBack() {
        if (noteTitle.text.toString().isNotEmpty() && noteContent.text.toString().isNotEmpty()) {
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.discard_warning)
                .setPositiveButton(R.string.save_button) { _, _ -> saveNote() }
                .setNegativeButton(R.string.discard_button) { _, _ -> deleteNote() }
                .create()
                .show()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun returnToList() {
        findNavController()
            .popBackStack()
            .also { requireActivity().hideKeyboard() }
    }

    private fun showAction(action: NoteAction): Boolean {
        val enabled = buttonsRelay.value ?: false

        if (enabled) {
            when (action) {
                NoteAction.SAVE -> saveNote()
                NoteAction.DELETE -> deleteNote()
            }

            return true
        } else {
            Toast.makeText(context, R.string.empty_note_toast, Toast.LENGTH_SHORT).show()
            return true
        }
    }

    private fun saveNote() {
        viewModel.saveNote()
            .subscribeOn(appSchedulers.database)
            .observeOn(appSchedulers.main)
            .autoDisposable(viewLifecycleOwner)
            .subscribe { returnToList() }
    }

    private fun deleteNote() {
        if (args.noteId.toInt() == -1) {
            returnToList()
        } else {
            viewModel.deleteNote()
                .subscribeOn(appSchedulers.database)
                .observeOn(appSchedulers.main)
                .autoDisposable(viewLifecycleOwner)
                .subscribe { returnToList() }
        }
    }
}
