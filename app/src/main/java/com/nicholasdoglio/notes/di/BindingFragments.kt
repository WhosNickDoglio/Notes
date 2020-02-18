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

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.nicholasdoglio.notes.features.daynight.DayNightToggleFragment
import com.nicholasdoglio.notes.features.editnote.DiscardFragment
import com.nicholasdoglio.notes.features.editnote.EditNoteFragment
import com.nicholasdoglio.notes.features.list.NoteListFragment
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
@Retention(value = AnnotationRetention.RUNTIME)
@MapKey
annotation class FragmentKey(val value: KClass<out Fragment>)

@Module
interface FragmentBindingModule {

    @Binds
    fun bindFragmentFactory(factory: NotesFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(EditNoteFragment::class)
    fun bindEditNoteFragment(editNoteFragment: EditNoteFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DiscardFragment::class)
    fun bindDiscardFragment(discardFragment: DiscardFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(NoteListFragment::class)
    fun bindListFragment(listFragment: NoteListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DayNightToggleFragment::class)
    fun bindDayNightFragment(dayNightToggleFragment: DayNightToggleFragment): Fragment
}
