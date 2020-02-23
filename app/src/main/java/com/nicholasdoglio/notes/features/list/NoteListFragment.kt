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

package com.nicholasdoglio.notes.features.list

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.util.DispatcherProvider
import com.nicholasdoglio.notes.util.openWebPage
import com.nicholasdoglio.notes.util.withListener
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.android.view.clicks
import timber.log.Timber
import javax.inject.Inject

class NoteListFragment @Inject constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
    private val dispatcherProvider: DispatcherProvider
) : Fragment(R.layout.fragment_note_list) {

    private val viewModel: NoteListViewModel by viewModels { viewModelFactory }

    private lateinit var appBar: BottomAppBar
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var createNoteFab: FloatingActionButton
    private lateinit var emptyStateView: LinearLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notesListAdapter = NoteListAdapter(findNavController())

        initViews(view)

        notesRecyclerView.apply {
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            adapter = notesListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        createNoteFab
            .clicks()
            .flowOn(dispatcherProvider.main)
            .onEach { findNavController().navigate(NoteListFragmentDirections.openNote()) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.notesList
            .flowOn(dispatcherProvider.main)
            .onEach { notesListAdapter.submitList(it) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.hasNotes
            .flowOn(dispatcherProvider.main)
            .onEach {
                emptyStateView.isVisible = !it
                notesRecyclerView.isVisible = it
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initViews(view: View) {
        appBar = view.findViewById(R.id.app_bar)
        notesRecyclerView = view.findViewById(R.id.notes_recyclerview)
        createNoteFab = view.findViewById(R.id.create_note_fab)
        emptyStateView = view.findViewById(R.id.empty_state_view)

        appBar.apply {
            inflateMenu(R.menu.list_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.about_item -> {
                        findNavController().navigate(
                            NoteListFragmentDirections.openLibs(
                                LibsBuilder()
                                    .withAboutVersionShownCode(false)
                                    .withEdgeToEdge(true)
                                    .withLicenseShown(true)
                                    .withAboutSpecial1("Nicholas Doglio")
                                    .withAboutSpecial2("Source Code")
                                    .withListener(onExtraClicked = { v, specialButton ->
                                        when (specialButton) {
                                            Libs.SpecialButton.SPECIAL1 ->
                                                v.context.openWebPage("https://whosnickdoglio.dev")
                                            Libs.SpecialButton.SPECIAL2 ->
                                                v.context.openWebPage(
                                                    "https://github.com/WhosNickDoglio/Notes"
                                                )
                                            else -> Timber.i("This shouldn't have been clicked 🤷‍")
                                        }
                                        true
                                    })
                            )
                        )
                        true
                    }
                    R.id.night_mode_toggle_item -> {
                        findNavController().navigate(R.id.open_night_toggle)
                        true
                    }
                    else -> false
                }
            }
        }
    }
}

data class NoteUi(val id: Long, val title: String?, val contents: String?)
