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

import com.tfcporciuncula.flow.FlowSharedPreferences
import com.tfcporciuncula.flow.Preference
import javax.inject.Inject

interface DayNightPreferences {
    val nightModePreference: Preference<NightMode>
    val currentlySelectedNightModePreference: Preference<Int>
    val currentlySelectedNightMode: Int
}

class DefaultDayNightPreferences @Inject constructor(
    private val flowSharedPreferences: FlowSharedPreferences
) :
    DayNightPreferences {

    override val nightModePreference: Preference<NightMode> =
        flowSharedPreferences.getEnum(NIGHT_MODE, NightMode.FOLLOW_SYSTEM)

    override val currentlySelectedNightModePreference: Preference<Int> =
        flowSharedPreferences.getInt(SELECTED_NIGHT_MODE, 0)

    override val currentlySelectedNightMode: Int
        get() = currentlySelectedNightModePreference.get()

    companion object {
        private const val SELECTED_NIGHT_MODE = "SELECTED_NIGHT_MODE"
        private const val NIGHT_MODE = "NIGHT_MODE"
    }
}
