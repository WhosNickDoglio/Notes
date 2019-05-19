package com.nicholasdoglio.notes.di

import com.nicholasdoglio.notes.util.AppSchedulers
import com.nicholasdoglio.notes.util.NotesSchedulers
import dagger.Binds
import dagger.Module

@Module
abstract class BindingModule {

    @Binds
    abstract fun bindsSchedulers(schedulers: NotesSchedulers): AppSchedulers
}
