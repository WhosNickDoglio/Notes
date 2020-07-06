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

package com.nicholasdoglio.notes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nicholasdoglio.notes.features.detail.DetailViewModel
import com.nicholasdoglio.notes.features.discard.DiscardViewModel
import com.nicholasdoglio.notes.features.overview.OverviewViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@MapKey
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(factory: NotesViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    fun bindDetailViewModel(detailViewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel::class)
    fun bindOverviewViewModel(overviewViewModel: OverviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiscardViewModel::class)
    fun bindsDiscardViewModel(discardViewModel: DiscardViewModel): ViewModel
}
