package com.nicholasdoglio.notes.ui.list

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.Note

/**
 * @author Nicholas Doglio
 */
class NoteListAdapter : PagedListAdapter<Note, NoteListAdapter.NoteListViewHolder>(diffcallback) {

    //TODO set up click to open note

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) = holder.bindTo(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder = NoteListViewHolder(parent)

    class NoteListViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
    ) {
        private val titleView = itemView.findViewById<TextView>(R.id.cardListTitle)//I think there's a better way to do this
        private val contentView = itemView.findViewById<TextView>(R.id.cardListContent)
        var note: Note? = null

        fun bindTo(note: Note?) {
            this.note = note
            titleView.text = note?.title
            contentView.text = note?.contents
        }
    }

    companion object {
        private val diffcallback = object : DiffCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
        }
    }
}