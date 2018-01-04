package com.nicholasdoglio.notes.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.nicholasdoglio.notes.ui.list.NoteListViewModel
import com.nicholasdoglio.notes.ui.note.NoteViewModel
import com.nicholasdoglio.notes.ui.viewmodel.NotesViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author Nicholas Doglio
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NoteViewModel::class)
    abstract fun bindNoteViewModel(noteViewModel: NoteViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteListViewModel::class)
    abstract fun bindNoteListViewModel(noreListViewModel: NoteListViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: NotesViewModelFactory): ViewModelProvider.Factory
}