package com.nicholasdoglio.notes.data.local

import android.arch.paging.DataSource
import android.arch.persistence.room.*
import com.nicholasdoglio.notes.data.model.note.Note
import io.reactivex.Single

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
    fun saveNote(note: Note)

    /**
     * Deletes the given note from the database
     */
    @Delete
    fun deleteNote(note: Note)

    /**
     * Updates the selected note title or content
     */
    @Update
    fun updateNote(note: Note)

    /**
     * Checks if a note is already in the database
     */

//    @Query("SELECT * FROM Note WHERE title = :title AND contents =:contents")
//    fun checkIfNoteAlreadyExists(title: String, contents: String): Single<Boolean>

    /**
     * Returns the selected note from the database
     */
    @Query("SELECT * From Note WHERE id = :id")
    fun getNote(id: Long): Single<Note>

    /**
     * Pulls all the notes from the database in a asynchronous manner
     */
    @Query("SELECT * FROM Note")
    fun getAllNotes(): DataSource.Factory<Int, Note>

    /**
     * Gets the total number of items in the Note table, really just used for testing
     */
    @Query("SELECT COUNT(*) FROM Note")
    fun getNumberOfItems(): Int
}