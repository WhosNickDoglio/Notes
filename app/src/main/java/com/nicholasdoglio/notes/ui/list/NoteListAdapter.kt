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
import com.nicholasdoglio.notes.data.note.Note
import com.nicholasdoglio.notes.ui.common.NavigationController
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

/**
 * @author Nicholas Doglio
 */
class NoteListAdapter(val navigationController: NavigationController) : PagedListAdapter<Note, NoteListAdapter.NoteListViewHolder>(diffcallback) {

    //TODO look into PublishSubject instead of RxBinding here

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) = holder.bindTo(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteListViewHolder(view)
    }

    inner class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView = itemView.findViewById<TextView>(R.id.titleListItem)//I think there's a better way to do this
        private val contentView = itemView.findViewById<TextView>(R.id.contentsListItem)
        private var compositeDisposable: CompositeDisposable
        var note: Note? = null

        init {
            compositeDisposable = CompositeDisposable()

            compositeDisposable += itemView.clicks()// I need to dispose this somehow
                    .subscribe { navigationController.openNote(note!!.id) } //Open up specific note
        }

        fun bindTo(note: Note?) {
            this.note = note

            titleView.text = note?.title
            contentView.text = note?.contents
        }

        fun clear() {
            compositeDisposable.clear()
        }
    }

    companion object {
        private val diffcallback = object : DiffCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
        }
    }
}