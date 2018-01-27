package com.nicholasdoglio.notes.data.model.note

import android.annotation.SuppressLint
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author Nicholas Doglio
 *
 *
 *  @param id: primary key used to identify notes
 *  @param title: The title or headline of the note
 *  @param contents: the body of the note
 */
@SuppressLint("ParcelCreator")
@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var title: String,
    var contents: String
) : Parcelable