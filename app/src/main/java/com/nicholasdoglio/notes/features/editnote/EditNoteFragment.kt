/*
 * MIT License
 *
 * Copyright (c) 2020 Nicholas Doglio
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
package com.nicholasdoglio.notes.features.editnote

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.setupWithNavController
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.util.DispatcherProvider
import com.nicholasdoglio.notes.util.hideKeyboard
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.activity.backPresses
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.widget.textChanges
import javax.inject.Inject

// BUG Shortcut doesn't seem to actually save the notes?
class EditNoteFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dispatcherProvider: DispatcherProvider
) : Fragment(R.layout.fragment_edit_note) {

    private val args: EditNoteFragmentArgs by navArgs()
    private val viewModel: EditNoteViewModel
        by navGraphViewModels(R.id.edit_navigation) { viewModelFactory }

    private lateinit var toolbar: Toolbar
    private lateinit var deleteButton: Button
    private lateinit var insertButton: Button
    private lateinit var title: EditText
    private lateinit var content: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)
        deleteButton = view.findViewById(R.id.delete_button)
        insertButton = view.findViewById(R.id.insert_button)
        title = view.findViewById(R.id.note_title)
        content = view.findViewById(R.id.note_content)

        toolbar.setupWithNavController(findNavController())

        viewModel.inputNoteId.offer(args.noteId)

        viewLifecycleOwner.lifecycleScope.launch {
            title.setText(viewModel.title.first())
            content.setText(viewModel.contents.first())
        }

        activity?.onBackPressedDispatcher?.backPresses(viewLifecycleOwner)
            ?.map { viewModel.isEmpty }
            ?.onEach { findNavController().navigate(EditNoteFragmentDirections.openDiscard()) }
            ?.launchIn(viewLifecycleOwner.lifecycleScope)

        title.textChanges()
            .flowOn(dispatcherProvider.main)
            .onEach { viewModel.titleChannel.offer(it.toString()) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        content.textChanges()
            .flowOn(dispatcherProvider.main)
            .onEach { viewModel.contentChannel.offer(it.toString()) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        // // TODO on unsaved notes remember to just go back to list here
        deleteButton.clicks()
            .flowOn(dispatcherProvider.main)
            .onEach { viewModel.inputDelete.offer(Unit) }
            .onEach { findNavController().popBackStack() }
            .onEach { activity?.hideKeyboard() }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        insertButton.clicks()
            .flowOn(dispatcherProvider.main)
            .onEach { viewModel.inputInsert.offer(Unit) }
            .onEach { findNavController().popBackStack() }
            .onEach { activity?.hideKeyboard() }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
