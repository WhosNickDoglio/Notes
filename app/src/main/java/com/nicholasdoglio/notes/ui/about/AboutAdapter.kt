package com.nicholasdoglio.notes.ui.about

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import com.nicholasdoglio.notes.BuildConfig
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

    private val aboutList: MutableList<AboutItem> = mutableListOf()

    init {
        populateList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder =
        AboutViewHolder(parent.inflate(R.layout.item_about))

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        holder.bindTo(aboutList[position])
        holder.itemView.clicks()
            .map { openLink(position) }
            .subscribe { itemClickSubject }
    }


    override fun getItemCount(): Int = aboutList.size

    private fun populateList() {
        aboutList.add(
            AboutItem(
                R.drawable.dev_photo,
                "Developed by Nicholas Doglio",
                "https://whosnickdoglio.github.io/"
            )
        )
        aboutList.add(AboutItem(R.drawable.ic_about, "Libraries", ""))
        aboutList.add(
            AboutItem(
                R.drawable.ic_github,
                "Source Code",
                "https://github.com/WhosNickDoglio/Notes"
            )
        )
        aboutList.add(
            AboutItem(
                R.drawable.ic_about,
                "Version: ${BuildConfig.VERSION_NAME}",
                "https://github.com/WhosNickDoglio/Notes/releases"
            )
        )
    }

    private fun openLink(position: Int) {
        when (position) {
            0 -> Intents.openWebPage(aboutContext, aboutList[0].link)
            1 -> navigationController.openLibs()
            2 -> Intents.openWebPage(aboutContext, aboutList[2].link)
            3 -> Intents.openWebPage(aboutContext, aboutList[3].link)
        }
    }

    class AboutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(aboutItem: AboutItem) {
            itemView.aboutItemIcon.setImageResource(aboutItem.imageId)
            itemView.aboutItemName.text = aboutItem.text
        }
    }
}