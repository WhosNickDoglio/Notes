package com.nicholasdoglio.notes.di

import android.app.Application
import androidx.room.Room
import com.nicholasdoglio.notes.data.local.NoteDao
import com.nicholasdoglio.notes.data.local.NoteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Nicholas Doglio
 */

@Module
object DatabaseModule {

    private const val NOTES_DB = "notes_db"

    @Singleton
    @Provides
    @JvmStatic
    fun room(app: Application): NoteDatabase =
        Room.databaseBuilder(app, NoteDatabase::class.java, NOTES_DB).build()

    @Singleton
    @Provides
    @JvmStatic
    fun noteDao(roomDatabase: NoteDatabase): NoteDao = roomDatabase.noteDao()
}
