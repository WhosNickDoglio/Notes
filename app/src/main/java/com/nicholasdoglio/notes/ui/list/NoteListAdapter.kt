package com.nicholasdoglio.notes.ui.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.jakewharton.rxrelay2.BehaviorRelay
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.Note
import com.nicholasdoglio.notes.ui.base.NotesViewHolder
import com.nicholasdoglio.notes.util.inflate
import kotlinx.android.synthetic.main.item_note.*

/**
 * @author Nicholas Doglio
 */
class NoteListAdapter : ListAdapter<Note, NoteListAdapter.NoteListViewHolder>(noteListDiff) {

    private val noteListClick: BehaviorRelay<Note> = BehaviorRelay.create<Note>()
    val noteListItemClickListener: BehaviorRelay<Note> = noteListClick

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder =
        NoteListViewHolder(parent.inflate(R.layout.item_note))

    inner class NoteListViewHolder(itemView: View) : NotesViewHolder<Note>(itemView) {

        override fun bind(model: Note) {
            titleListItem.text = model.title
            contentsListItem.text = model.contents
            itemView.setOnClickListener { noteListClick.accept(model) }
        }
    }

    companion object {
        private val noteListDiff = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
                oldItem == newItem
        }
    }
}
