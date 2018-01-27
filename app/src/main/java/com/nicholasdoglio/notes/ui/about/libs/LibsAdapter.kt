package com.nicholasdoglio.notes.ui.about.libs

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.about.LibsItem
import com.nicholasdoglio.notes.util.inflate
import kotlinx.android.synthetic.main.item_libs.view.*

/**
 * @author Nicholas Doglio
 */
class LibsAdapter(private val libContext: Context) :
    RecyclerView.Adapter<LibsAdapter.LibsViewHolder>() {

    private val libraries: MutableList<LibsItem> = mutableListOf()

    init {
        populateList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibsViewHolder =
        LibsViewHolder(parent.inflate(R.layout.item_libs))


    override fun onBindViewHolder(holderLibs: LibsViewHolder, position: Int) {
        holderLibs.bindTo(libraries[position])
    }

    override fun getItemCount(): Int = libraries.size

    private fun populateList() {
        libraries.add(
            LibsItem(
                libContext.resources.getString(R.string.android_support),
                libContext.resources.getString(R.string.support_components_license)
            )
        )
        libraries.add(
            LibsItem(
                libContext.resources.getString(R.string.android_architecture_components),
                libContext.resources.getString(R.string.support_components_license)
            )
        )
        libraries.add(
            LibsItem(
                libContext.resources.getString(R.string.rxjava),
                libContext.resources.getString(R.string.rxjava_android_license)
            )
        )
        libraries.add(
            LibsItem(
                libContext.resources.getString(R.string.rxandroid),
                libContext.resources.getString(R.string.rxjava_android_license)
            )
        )
        libraries.add(
            LibsItem(
                libContext.resources.getString(R.string.rxbinding),
                libContext.resources.getString(R.string.rxbinding_license)
            )
        )
        libraries.add(
            LibsItem(
                libContext.resources.getString(R.string.autodispose),
                libContext.resources.getString(R.string.autodispose_license)
            )
        )
        libraries.add(
            LibsItem(
                libContext.resources.getString(R.string.dagger),
                libContext.resources.getString(R.string.dagger_license)
            )
        )
    }

    inner class LibsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(library: LibsItem) {
            itemView.libTitle.text = library.name
            itemView.libLicense.text = library.license
        }
    }
}