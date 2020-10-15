package com.nicholasdoglio.notes.features.discard

import com.nicholasdoglio.notes.db.Note

sealed class DiscardInput {
    data class Save(val note: Note) : DiscardInput()
    data class Delete(val id: Long) : DiscardInput()
}
