package com.nicholasdoglio.notes.ui.about.libs

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.about.LibsItem

/**
 * @author Nicholas Doglio
 */
class LibsAdapter(val context: Context) : RecyclerView.Adapter<LibsAdapter.LibsViewHolder>() {

    private val libraries: MutableList<LibsItem> = mutableListOf()

    init {
        populateList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_libs, parent, false)
        return LibsViewHolder(view)
    }

    override fun onBindViewHolder(holderLibs: LibsViewHolder, position: Int) {
        holderLibs.bindTo(libraries[position])
    }

    override fun getItemCount(): Int = libraries.size

    private fun populateList() {
        libraries.add(LibsItem(context.resources.getString(R.string.android_support), context.resources.getString(R.string.support_components_license)))
        libraries.add(LibsItem(context.resources.getString(R.string.android_architecture_components), context.resources.getString(R.string.support_components_license)))
        libraries.add(LibsItem(context.resources.getString(R.string.rxjava), context.resources.getString(R.string.rxjava_android_license)))
        libraries.add(LibsItem(context.resources.getString(R.string.rxandroid), context.resources.getString(R.string.rxjava_android_license)))
        libraries.add(LibsItem(context.resources.getString(R.string.rxbinding), context.resources.getString(R.string.rxbinding_license)))
        libraries.add(LibsItem(context.resources.getString(R.string.autodispose), context.resources.getString(R.string.autodispose_license)))
        libraries.add(LibsItem(context.resources.getString(R.string.dagger), context.resources.getString(R.string.dagger_license)))
    }

    inner class LibsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val libName: TextView = itemView.findViewById(R.id.libTitle)
        private val libLicense: TextView = itemView.findViewById(R.id.libLicense)

        fun bindTo(library: LibsItem) {
            libName.text = library.name
            libLicense.text = library.license
        }
    }
}