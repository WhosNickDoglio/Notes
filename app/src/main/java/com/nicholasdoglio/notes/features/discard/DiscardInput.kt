package com.nicholasdoglio.notes.features.discard

import com.nicholasdoglio.shared.db.Note

/**
 * Created by nicholas.doglio on 7/4/20.
 */
sealed class DiscardInput {
    data class Save(val note: Note): DiscardInput()
    data class Delete(val id: Long): DiscardInput()
}