package com.nicholasdoglio.notes.ui.about

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding2.view.clicks
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.about.AboutItem
import com.nicholasdoglio.notes.util.Intents
import com.nicholasdoglio.notes.util.UtilFunctions
import io.reactivex.subjects.PublishSubject
import java.util.*


class AboutAdapter(private val aboutContext: Context) : RecyclerView.Adapter<AboutAdapter.AboutViewHolder>() {

    private val aboutList: MutableList<AboutItem>
    private val itemClickSubject: PublishSubject<AboutItem> = PublishSubject.create()

    init {
        aboutList = ArrayList()
        populateList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.about_item, parent, false)
        return AboutViewHolder(view)
    }

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        holder.bindTo(aboutList[position])
    }

    override fun getItemCount(): Int = aboutList.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        itemClickSubject.onComplete()
    }

    private fun populateList() {
        aboutList.add(AboutItem(R.drawable.dev_photo, "Developed by Nicholas Doglio", "https://whosnickdoglio.github.io/"))
        aboutList.add(AboutItem(R.drawable.ic_github, "Source Code", "https://github.com/WhosNickDoglio/Notes"))
        aboutList.add(AboutItem(R.drawable.ic_about, UtilFunctions().versionNumber(aboutContext), "https://github.com/WhosNickDoglio/Notes/releases"))
    }

    inner class AboutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val aboutIcon = itemView.findViewById<ImageView>(R.id.aboutItemIcon)
        private val aboutText = itemView.findViewById<TextView>(R.id.aboutItemName)

        init {
            itemView.clicks()
                    .map { openLink() }
                    .subscribe { itemClickSubject }
        }

        fun bindTo(aboutItem: AboutItem) {
            aboutIcon.setImageResource(aboutItem.imageId)
            aboutText.text = aboutItem.text
        }

        private fun openLink() {
            when (adapterPosition) {
                0 -> Intents.openWebPage(aboutContext, aboutList[0].link)
                1 -> Intents.openWebPage(aboutContext, aboutList[1].link)
                2 -> Intents.openWebPage(aboutContext, aboutList[2].link)
            }
        }
    }
}