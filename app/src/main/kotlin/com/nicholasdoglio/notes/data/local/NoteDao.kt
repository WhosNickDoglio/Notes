/*
 * MIT License
 *
 * Copyright (c) 2019 Nicholas Doglio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.nicholasdoglio.notes.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nicholasdoglio.notes.data.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Data access object for Note class for Room database
 */
@Dao
interface NoteDao {
    /** Pulls all the notes from the database as a list */
    @get:Query("SELECT * FROM Note")
    val observeNotes: Flow<List<Note>>

    /** Gets the total number of notes in the table */
    @get:Query("SELECT count(*) FROM Note")
    val countOfNotes: Flow<Int>

    /** Returns the selected note from the database */
    @Query("SELECT * From Note WHERE id = :id")
    fun note(id: Long): Flow<Note?>

    /** Takes a given note from the user and enters it into the database */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note): Long

    /** Updates the selected note title or content */
    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateNote(note: Note)

    /** Deletes the given note from the database */
    @Delete
    suspend fun deleteNote(note: Note)
}
