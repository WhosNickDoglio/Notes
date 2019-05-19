package com.nicholasdoglio.notes.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nicholasdoglio.notes.data.model.Note
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * @author Nicholas Doglio
 * Data access object for Note class for Room database
 */
@Dao
interface NoteDao {

    /** Takes a given note from the user and enters it into the database */
    @Insert
    fun saveNote(note: Note)

    /** Deletes the given note from the database */
    @Delete
    fun deleteNote(note: Note)

    /** Updates the selected note title or content */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNote(note: Note)

    /** Gets the total number of notes in the table */
    @Query("SELECT count(*) FROM Note")
    fun countOfNotes(): Flowable<Int>

    /** Returns the selected note from the database */
    @Query("SELECT * From Note WHERE id = :id")
    fun note(id: Long): Maybe<Note>

    /** Pulls all the notes from the database as a list */
    @Query("SELECT * FROM Note")
    fun observeNotes(): Flowable<List<Note>>
}
