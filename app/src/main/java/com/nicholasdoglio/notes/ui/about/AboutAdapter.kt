package com.nicholasdoglio.notes.ui.about

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.about.AboutItem
import com.nicholasdoglio.notes.util.inflate
import kotlinx.android.synthetic.main.item_about.view.*


class AboutAdapter : RecyclerView.Adapter<AboutAdapter.AboutViewHolder>() {

    private val aboutAdapterPublishRelay: PublishRelay<AboutItem> = PublishRelay.create()
    private lateinit var aboutList: List<AboutItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder =
        AboutViewHolder(parent.inflate(R.layout.item_about))

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) =
        holder.bindTo(aboutList[position])

    override fun getItemCount(): Int = aboutList.size

    fun aboutListClickListener(): PublishRelay<AboutItem> = aboutAdapterPublishRelay

    fun setList(list: List<AboutItem>) {
        aboutList = list
    }

    inner class AboutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.clicks()
                .map { aboutAdapterPublishRelay.accept(aboutList[adapterPosition]) }
                .subscribe()
        }

        fun bindTo(aboutItem: AboutItem) {
            itemView.aboutItemIcon.setImageResource(aboutItem.imageId)
            itemView.aboutItemName.text = aboutItem.text
        }
    }
}