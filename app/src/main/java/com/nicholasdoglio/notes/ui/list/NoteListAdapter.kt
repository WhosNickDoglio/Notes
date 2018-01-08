package com.nicholasdoglio.notes.ui.list

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.note.Note
import com.nicholasdoglio.notes.ui.common.NavigationController
import io.reactivex.subjects.PublishSubject

/**
 * @author Nicholas Doglio
 */
class NoteListAdapter(val navigationController: NavigationController) : PagedListAdapter<Note, NoteListAdapter.NoteListViewHolder>(diffcallback) {

    private val itemClickSubject: PublishSubject<Note> = PublishSubject.create()

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) = holder.bindTo(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteListViewHolder(view)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        itemClickSubject.onComplete()
    }

    inner class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView = itemView.findViewById<TextView>(R.id.titleListItem)//I think there's a better way to do this
        private val contentView = itemView.findViewById<TextView>(R.id.contentsListItem)
        var note: Note? = null

        init {
            itemView.clicks()
                    .map { navigationController.openNote(note!!.id) }
                    .subscribe { itemClickSubject }
        }

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