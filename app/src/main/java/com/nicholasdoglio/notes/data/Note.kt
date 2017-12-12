package com.nicholasdoglio.notes.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * @author Nicholas Doglio
 *
 * @param id: primary key used to identify notes
 * @param title: The title or headline of the note
 * @param contents: the body of the note
 */
@Entity
data class Note(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        var title: String,
        var contents: String
)