package com.nicholasdoglio.notes.di

import com.nicholasdoglio.notes.ui.about.AboutFragment
import com.nicholasdoglio.notes.ui.about.libs.LibsFragment
import com.nicholasdoglio.notes.ui.list.NoteListFragment
import com.nicholasdoglio.notes.ui.note.NoteFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun contributesNoteFragment(): NoteFragment

    @ContributesAndroidInjector
    abstract fun contributesNoteListFragment(): NoteListFragment

    @ContributesAndroidInjector
    abstract fun contributesAboutFragment(): AboutFragment

    @ContributesAndroidInjector
    abstract fun contributesLibsFragment(): LibsFragment
}