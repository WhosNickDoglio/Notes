package com.nicholasdoglio.notes.ui.about

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.about.AboutItem
import com.nicholasdoglio.notes.util.UtilFunctions
import java.util.*


class AboutAdapter(private val aboutContext: Context) : RecyclerView.Adapter<AboutAdapter.AboutViewHolder>() {

    private val aboutList: MutableList<AboutItem>

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

    private fun populateList() {
        aboutList.add(AboutItem(R.drawable.dev_photo, "Developed by Nicholas Doglio", "https://whosnickdoglio.github.io/"))
        aboutList.add(AboutItem(R.drawable.ic_github, "Source Code", "https://github.com/WhosNickDoglio/Notes"))
        aboutList.add(AboutItem(R.drawable.ic_about, UtilFunctions().versionNumber(aboutContext), ""))
    }

    inner class AboutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val aboutIcon = itemView.findViewById<ImageView>(R.id.aboutItemIcon)
        private val aboutText = itemView.findViewById<TextView>(R.id.aboutItemName)

        fun bindTo(aboutItem: AboutItem) {
            aboutIcon.setImageResource(aboutItem.imageId)
            aboutText.text = aboutItem.text
        }
    }
}