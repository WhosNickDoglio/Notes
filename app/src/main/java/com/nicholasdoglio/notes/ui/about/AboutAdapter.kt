package com.nicholasdoglio.notes.ui.about

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.jakewharton.rxrelay2.PublishRelay
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.AboutItem
import com.nicholasdoglio.notes.ui.base.NotesViewHolder
import com.nicholasdoglio.notes.util.inflate
import kotlinx.android.synthetic.main.item_about.*

class AboutAdapter : ListAdapter<AboutItem, AboutAdapter.AboutViewHolder>(diffCallBack) {

    private val aboutAdapterPublishRelay: PublishRelay<AboutItem> = PublishRelay.create()
    val aboutListClickListener: PublishRelay<AboutItem> = aboutAdapterPublishRelay

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder =
        AboutViewHolder(parent.inflate(R.layout.item_about))

    inner class AboutViewHolder(itemView: View) : NotesViewHolder<AboutItem>(itemView) {

        override fun bind(model: AboutItem) {
            itemView.setOnClickListener { aboutAdapterPublishRelay.accept(model) }
            aboutItemName.text = itemView.resources.getString(model.text)
        }
    }

    companion object {
        private val diffCallBack = object : DiffUtil.ItemCallback<AboutItem>() {
            override fun areItemsTheSame(oldItem: AboutItem, newItem: AboutItem): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: AboutItem, newItem: AboutItem): Boolean =
                oldItem.text == newItem.text
        }
    }
}
