package com.nicholasdoglio.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nicholasdoglio.notes.data.model.Note

/**
 * @author Nicholas Doglio
 */

@Database(entities = [(Note::class)], version = 1, exportSchema = true)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
