package com.nicholasdoglio.notes.ui.list

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.note.Note
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.util.inflate
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * @author Nicholas Doglio
 */
class NoteListAdapter(val navigationController: NavigationController) :
    PagedListAdapter<Note, NoteListAdapter.NoteListViewHolder>(noteListDiff) {

    private val itemClickSubject: PublishRelay<Note> = PublishRelay.create()

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) =
        holder.bindTo(this.getItem(position)!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder =
        NoteListViewHolder(parent.inflate(R.layout.item_note))

    inner class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var note: Note

        init {
            itemView.clicks()
                .map { navigationController.openNote(note.id) }
                .subscribe { itemClickSubject }
        }

        fun bindTo(note: Note) {
            this.note = note
            itemView.titleListItem.text = note.title
            itemView.contentsListItem.text = note.contents
        }
    }

    companion object {
        private val noteListDiff = object : DiffCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
                oldItem == newItem
        }
    }
}