package com.nicholasdoglio.notes.features.discard

sealed class DiscardInput {
    data class Save(
        val id: Long,
        val title: String,
        val content: String
    ) : DiscardInput()
    data class Delete(val id: Long) : DiscardInput()
}
