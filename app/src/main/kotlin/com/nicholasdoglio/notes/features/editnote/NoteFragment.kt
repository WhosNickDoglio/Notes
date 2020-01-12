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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.util.SchedulersProvider
import com.nicholasdoglio.notes.util.hideKeyboard
import javax.inject.Inject

class NoteFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val schedulersProvider: SchedulersProvider
) : Fragment(R.layout.fragment_note) {

    private val args: NoteFragmentArgs by navArgs()
    private val viewModel: NoteViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) { TODO() }

        // TODO I don't think I'll show a toolbar here, just want home arrow
        // (activity as AppCompatActivity).supportActionBar?.apply {
        //     setDisplayShowTitleEnabled(false)
        //     setHasOptionsMenu(true)
        //     title = ""
        // }

        // viewLifecycleOwner.lifecycleScope.launch {
        //     viewModel.inputNoteId.send(args.noteId)
        // }
        //
        // // viewModel.note.observe(viewLifecycleOwner, Observer {
        // //     if (it != null) {
        // //         noteTitle.textEditable = it.title
        // //         noteContent.textEditable = it.contents
        // //     }
        // // })
        //
        // deleteButton.setOnClickListener {
        //     viewLifecycleOwner.lifecycleScope.launch {
        //         viewModel.triggerDelete.send(Unit)
        //         // TODO on unsaved notes remember to just go back to list here
        //     }
        // }
        //
        // upsertButton.setOnClickListener {
        //     viewLifecycleOwner.lifecycleScope.launch {
        //         viewModel.triggerUpsert.send(Unit)
        //     }
        // }
    }

    private fun returnToList() {
        findNavController()
            .popBackStack()
            .also { requireActivity().hideKeyboard() }
    }
}
