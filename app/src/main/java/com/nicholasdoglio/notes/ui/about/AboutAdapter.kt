package com.nicholasdoglio.notes.ui.about

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.about.AboutItem
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.util.Intents
import com.nicholasdoglio.notes.util.inflate
import kotlinx.android.synthetic.main.item_about.view.*


class AboutAdapter(
    private val aboutContext: Context,
    private val navigationController: NavigationController
) : RecyclerView.Adapter<AboutAdapter.AboutViewHolder>() {

    private val itemClickSubject: PublishRelay<AboutItem> = PublishRelay.create()

    private lateinit var aboutList: List<AboutItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder =
        AboutViewHolder(parent.inflate(R.layout.item_about))

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        holder.bindTo(aboutList[position])
    }

    override fun getItemCount(): Int = aboutList.size

    fun setList(list: List<AboutItem>) {
        aboutList = list
    }

    inner class AboutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.clicks()
                .map { openLink() }
                .subscribe { itemClickSubject }
        }

        fun bindTo(aboutItem: AboutItem) {
            itemView.aboutItemIcon.setImageResource(aboutItem.imageId)
            itemView.aboutItemName.text = aboutItem.text
        }

        private fun openLink() {
            when (adapterPosition) {
                0 -> Intents.openWebPage(aboutContext, aboutList[0].link)
                1 -> navigationController.openLibs()
                2 -> Intents.openWebPage(aboutContext, aboutList[2].link)
                3 -> Intents.openWebPage(aboutContext, aboutList[3].link)
            }
        }
    }
}