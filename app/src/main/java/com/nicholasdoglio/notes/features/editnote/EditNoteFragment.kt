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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.util.SchedulersProvider
import com.uber.autodispose.android.lifecycle.autoDispose
import javax.inject.Inject

class EditNoteFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val schedulersProvider: SchedulersProvider
) : Fragment(R.layout.fragment_edit_note) {

    private val args: EditNoteFragmentArgs by navArgs()
    private val viewModel: EditNoteViewModel by viewModels { viewModelFactory }

    private lateinit var deleteButton: Button
    private lateinit var insertButton: Button
    private lateinit var title: EditText
    private lateinit var content: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        deleteButton = view.findViewById(R.id.delete_button)
        insertButton = view.findViewById(R.id.insert_button)
        title = view.findViewById(R.id.note_title)
        content = view.findViewById(R.id.note_content)

        viewModel
            .input
            .autoDispose(viewLifecycleOwner)
            .subscribe()

        viewModel
            .insert
            .autoDispose(viewLifecycleOwner)
            .subscribe()

        viewModel
            .delete
            .autoDispose(viewLifecycleOwner)
            .subscribe()

        viewModel.inputId(args.noteId)

        // activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
        //     findNavController().navigate(EditNoteFragmentDirections.openDiscard())
        // }

        title.textChanges()
            .autoDispose(viewLifecycleOwner)
            .subscribe { viewModel.inputTitle(it.toString()) }

        content.textChanges()
            .autoDispose(viewLifecycleOwner)
            .subscribe { viewModel.inputContents(it.toString()) }

        viewModel.title
            .autoDispose(viewLifecycleOwner)
            .subscribe { title.setText(it) }

        viewModel.contents
            .autoDispose(viewLifecycleOwner)
            .subscribe { content.setText(it) }

        // TODO on unsaved notes remember to just go back to list here
        deleteButton.clicks()
            .autoDispose(viewLifecycleOwner)
            .subscribe { viewModel.triggerDelete() }

        insertButton.clicks()
            .autoDispose(viewLifecycleOwner)
            .subscribe { viewModel.triggerInsert() }
    }
}
