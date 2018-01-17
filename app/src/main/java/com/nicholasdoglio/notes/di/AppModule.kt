package com.nicholasdoglio.notes.di

import android.app.Application
import android.arch.persistence.room.Room
import com.nicholasdoglio.notes.data.local.NoteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Nicholas Doglio
 */

@Module
class AppModule {

    @Singleton
    @Provides
    fun room(app: Application): NoteDatabase
            = Room.databaseBuilder(app, NoteDatabase::class.java, "notes_db").build()
}