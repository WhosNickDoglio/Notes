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

import com.nicholasdoglio.notes.util.DispatcherProvider
import com.tfcporciuncula.flow.FlowSharedPreferences
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DayNightModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    flowSharedPreferences: FlowSharedPreferences
) {
    private val toggleNightModeChannel = ConflatedBroadcastChannel<NightMode>()

    private val nightModePref = flowSharedPreferences.getEnum(NIGHT_MODE, NightMode.FOLLOW_SYSTEM)

    private val selectedNightMode = flowSharedPreferences.getInt(SELECTED_NIGHT_MODE, 0)

    val nightMode: Flow<NightMode> =
        nightModePref.asFlow()
            .flowOn(dispatcherProvider.background)

    val selected = selectedNightMode.get()

    suspend fun saveSelected(selected: Int) = toggleNightModeChannel.asFlow()
        .flowOn(dispatcherProvider.background)
        .onEach { selectedNightMode.setAndCommit(selected) }

    suspend fun toggleNightMode(mode: NightMode) {
        toggleNightModeChannel.offer(mode)

        toggleNightModeChannel.asFlow()
            .flowOn(dispatcherProvider.background)
            .distinctUntilChanged()
            .onEach { nightModePref.setAndCommit(it) }
    }

    companion object {
        private const val SELECTED_NIGHT_MODE = "SELECTED_NIGHT_MODE"
        private const val NIGHT_MODE = "NIGHT_MODE"
    }
}
