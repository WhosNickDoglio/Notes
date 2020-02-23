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

package com.nicholasdoglio.notes

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.nicholasdoglio.notes.util.DispatcherProvider
import com.nicholasdoglio.notes.util.FlipperInitializer
import dagger.Binds
import dagger.Module
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@Module
interface FlipperBindingModule {

    @Binds
    fun bindFlipper(debugFlipperInitializer: DebugFlipperInitializer): FlipperInitializer
}

class DebugFlipperInitializer @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val application: Application
) : FlipperInitializer {

    override fun invoke() {
        GlobalScope.launch(dispatcherProvider.background) {
            SoLoader.init(application, false)
            if (FlipperUtils.shouldEnableFlipper(application)) {
                AndroidFlipperClient.getInstance(application).apply {
                    addPlugin(InspectorFlipperPlugin(application, DescriptorMapping.withDefaults()))
                    addPlugin(DatabasesFlipperPlugin(application))
                    addPlugin(SharedPreferencesFlipperPlugin(application, NOTES_PREFERENCES))
                }.start()
            }
        }
    }

    companion object {
        private const val NOTES_PREFERENCES = "com.nicholasdoglio.notes_preferences"
    }
}
