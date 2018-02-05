package com.nicholasdoglio.notes.ui.about

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.view.detaches
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.about.AboutItem
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.util.inflate
import kotlinx.android.synthetic.main.item_about.view.*
import org.jetbrains.anko.browse
import timber.log.Timber


class AboutAdapter(
    private val aboutContext: Context,
    private val navigationController: NavigationController
) : RecyclerView.Adapter<AboutAdapter.AboutViewHolder>() {

    private lateinit var aboutList: List<AboutItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutViewHolder =
        AboutViewHolder(parent.inflate(R.layout.item_about))

    override fun onBindViewHolder(holder: AboutViewHolder, position: Int) {
        holder.bindTo(aboutList[position])
        holder.itemView.clicks()
            .takeUntil(holder.itemView.detaches())
            .doOnTerminate { Timber.d("aboutAdapterDispoable is terminated") }
            .map { openLink(position) }
            .subscribe()
    }

    override fun getItemCount(): Int = aboutList.size

    private fun openLink(position: Int) {
        when (position) {
            0 -> aboutContext.browse(aboutList[0].link)
            1 -> navigationController.openLibs()
            2 -> aboutContext.browse(aboutList[2].link)
            3 -> aboutContext.browse(aboutList[3].link)
        }
    }

    fun setList(list: List<AboutItem>) {
        aboutList = list
    }

    class AboutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(aboutItem: AboutItem) {
            itemView.aboutItemIcon.setImageResource(aboutItem.imageId)
            itemView.aboutItemName.text = aboutItem.text
        }
    }
}