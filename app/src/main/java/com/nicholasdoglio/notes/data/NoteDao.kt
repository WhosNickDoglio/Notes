package com.nicholasdoglio.notes.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Single

/**
 * @author Nicholas Doglio
 */
@Dao
interface NoteDao {

    @Insert
    fun createNote(note: Note)

    @Delete
    fun deleteNote(id: Int)

//    @Query("SELECT * FROM Note")
//    fun getAllNotes() //Implement Paging Lib Aynsc list

    @Query("Select * FROM Note WHERE id LIKE :id")
    fun getNote(id: Int): Single<Note>
}