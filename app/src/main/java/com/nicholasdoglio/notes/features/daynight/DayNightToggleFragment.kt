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

package com.nicholasdoglio.notes.features.daynight

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.util.DispatcherProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DayNightToggleFragment @Inject constructor(
    private val factory: ViewModelProvider.Factory,
    private val dispatcherProvider: DispatcherProvider
) : DialogFragment() {

    private val viewModel: DayNightToggleViewModel by viewModels { factory }

    init {
        lifecycleScope.launchWhenCreated {
            viewModel.nightModeState
                .flowOn(dispatcherProvider.background)
                .collect { AppCompatDelegate.setDefaultNightMode(it.value) }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Night Mode")
            .setSingleChoiceItems(R.array.night_mode, viewModel.selected) { _, selected ->
                viewModel.saveSelected(selected)
                when (selected) {
                    0 -> viewModel.changeNightMode(NightMode.FOLLOW_SYSTEM)
                    1 -> viewModel.changeNightMode(NightMode.ALWAYS_LIGHT)
                    2 -> viewModel.changeNightMode(NightMode.ALWAYS_NIGHT)
                }
            }
            .create()
}
