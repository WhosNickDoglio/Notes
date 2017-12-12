package com.nicholasdoglio.notes.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.nicholasdoglio.notes.data.NoteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Nicholas Doglio
 */
@Module
class AppModule {
    @Provides
    @Singleton
    fun providesContext(application: Application): Context = application

    @Provides
    @Singleton
    fun providesRoom(application: Application): NoteDatabase =
            Room.databaseBuilder(application, NoteDatabase::class.java, "notes_db").build();
}