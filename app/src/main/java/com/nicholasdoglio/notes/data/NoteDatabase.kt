package com.nicholasdoglio.notes.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * @author Nicholas Doglio
 */
@Database(entities = arrayOf(Note::class), version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}