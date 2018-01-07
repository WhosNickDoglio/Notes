package com.nicholasdoglio.notes.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.nicholasdoglio.notes.data.note.Note

/**
 * @author Nicholas Doglio
 */

@Database(entities = [(Note::class)], version = 1, exportSchema = true)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}