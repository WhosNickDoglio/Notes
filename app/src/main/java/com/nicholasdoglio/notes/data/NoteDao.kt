package com.nicholasdoglio.notes.data

import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Completable

/**
 * @author Nicholas Doglio
 */
interface NoteDao {

    @Insert
    fun createNote(title: String, content: String): Completable

    @Delete
    fun deleteNote(title: String, content: String): Completable

    @Query("SELECT * FROM Note")
    fun todo() //Implement Paging Lib Aynsc list
}