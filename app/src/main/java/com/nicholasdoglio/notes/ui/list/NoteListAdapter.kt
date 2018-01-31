package com.nicholasdoglio.notes.ui.list

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.note.Note
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.util.inflate
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_note.view.*

/**
 * @author Nicholas Doglio
 */
class NoteListAdapter(private val navigationController: NavigationController) :
    PagedListAdapter<Note, NoteListAdapter.NoteListViewHolder>(noteListDiff) {

    private lateinit var disposable: Disposable

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        holder.bindTo(this.getItem(position)!!)
        disposable = holder.itemView.clicks()
            .map { navigationController.openNote(this.getItem(position)!!.id) }
            .subscribe()
    }

    override fun onViewDetachedFromWindow(holder: NoteListViewHolder?) {
        super.onViewDetachedFromWindow(holder)
        disposable.dispose()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder =
        NoteListViewHolder(parent.inflate(R.layout.item_note))

    class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(note: Note) {
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