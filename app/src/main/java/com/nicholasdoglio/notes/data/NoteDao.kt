package com.nicholasdoglio.notes.data

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * @author Nicholas Doglio
 * Data access object for Note class for Room database
 */

@Dao
interface NoteDao {
    /**
     * Takes a given note from the user and enters it into the database
     */
    @Insert
    fun createNote(note: Note)

    /**
     * Deletes the given note from the database
     */
    @Delete
    fun deleteNote(note: Note)

    /**
     * Wipe out all the notes at once
     */
    @Query("DELETE * FROM NOTE")
    fun deleteAllNotes()

    /**
     * returns the selected note from the database
     */
    @Query("SELECT * From Note WHERE noteId = :id")
    fun getNote(id: Long): Note

    /**
     * Pulls all the notes from the database in a asynchronous manner
     */
    @Query("SELECT * FROM Note")
    fun getAllNotes(): DataSource.Factory<Int, Note>

}