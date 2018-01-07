package com.nicholasdoglio.notes.data.note

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author Nicholas Doglio
 *
 *
 *  @param id: primary key used to identify notes
 *  @param title: The title or headline of the note
 *  @param contents: the body of the note
 */
@Entity
data class Note(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        var title: String,
        var contents: String
)