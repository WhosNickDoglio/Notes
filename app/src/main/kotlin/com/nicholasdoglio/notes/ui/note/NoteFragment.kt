/*
 * MIT License
 *
 * Copyright (c) 2019 Nicholas Doglio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.nicholasdoglio.notes.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.databinding.FragmentNoteBinding
import com.nicholasdoglio.notes.di.injector
import com.nicholasdoglio.notes.util.hideKeyboard
import kotlinx.android.synthetic.main.fragment_note.*

class NoteFragment : Fragment() {

    private val args: NoteFragmentArgs by navArgs()
    private val viewModel by viewModels<NoteViewModel> {
        requireActivity().injector.viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentNoteBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_note, container, false)

        val view = binding.root

        binding.apply {
            viewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) { doBack() }

        (activity as AppCompatActivity).supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setHasOptionsMenu(true)
            title = ""
        }

//        viewModel.start(args.noteId)
//            .subscribeOn(appSchedulers.database)
//            .observeOn(appSchedulers.main)
//            .autoDisposable(viewLifecycleOwner)
//            .subscribe { findNote ->
//                viewModel.findNote.accept(findNote)
//                noteTitle.setEditableText(findNote.title)
//                noteContent.setEditableText(findNote.contents)
//            }
//
//        Observable.combineLatest(
//            noteTitle.textChanges().map { it.isNotEmpty() },
//            noteContent.textChanges().map { it.isNotEmpty() },
//            BiFunction<Boolean, Boolean, Boolean> { firstBool, secondBool -> firstBool && secondBool })
//            .distinctUntilChanged()
//            .autoDisposable(viewLifecycleOwner)
//            .subscribe { buttonsRelay.accept(it) }
//
//        noteTitle.textChanges()
//            .autoDisposable(viewLifecycleOwner)
//            .subscribe { viewModel.title.accept(it.toString()) }
//
//        noteContent.textChanges()
//            .autoDisposable(viewLifecycleOwner)
//            .subscribe { viewModel.contents.accept(it.toString()) }
//
//        viewModel.delete
//            .subscribeOn(appSchedulers.database)
//            .observeOn(appSchedulers.main)
//            .autoDisposable(viewLifecycleOwner)
//            .subscribe { returnToList() }
//
//        viewModel.insertNote
//            .subscribeOn(appSchedulers.database)
//            .observeOn(appSchedulers.main)
//            .autoDisposable(viewLifecycleOwner)
//            .subscribe { returnToList() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.save_item -> showAction(NoteAction.SAVE)
        R.id.delete_item -> showAction(NoteAction.DELETE)
        else -> super.onOptionsItemSelected(item)
    }

    private fun doBack() {
        // TODO move this to own fragment
        if (noteTitle.text.toString().isNotEmpty() && noteContent.text.toString().isNotEmpty()) {
            MaterialAlertDialogBuilder(requireContext())
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
//        val enabled = buttonsRelay.value ?: false

        if (true) {
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
//        viewModel.saveTrigger.accept(Unit)
    }

    private fun deleteNote() {
        if (args.noteId.toInt() == -1) {
            returnToList()
        } else {
//            viewModel.deleteTrigger.accept(Unit)
        }
    }
}
