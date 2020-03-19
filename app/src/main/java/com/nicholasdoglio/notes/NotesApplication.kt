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
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.nicholasdoglio.notes.di.AppComponent
import com.nicholasdoglio.notes.di.AppComponentProvider
import com.nicholasdoglio.notes.di.DaggerAppComponent
import com.nicholasdoglio.notes.features.daynight.DayNightModel
import com.nicholasdoglio.notes.util.DispatcherProvider
import com.nicholasdoglio.notes.util.FlipperInitializer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import timber.log.Timber
import javax.inject.Inject

// TODO design inspiration https://material.io/design/material-studies/fortnightly.html#
// Standardize how I handle navigation (directions vs xml ids)
class NotesApplication : Application(), AppComponentProvider {

    @Inject
    lateinit var dayNightModel: DayNightModel

    @Inject
    lateinit var flipperInitializer: FlipperInitializer

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    override val component: AppComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
        initDebugTools()

        dayNightModel.nightMode
            .onEach { AppCompatDelegate.setDefaultNightMode(it.value) }
            .launchIn(GlobalScope + NonCancellable)
    }

    private fun initDebugTools() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            initStrictMode()
            flipperInitializer()
        }
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )
    }
}
