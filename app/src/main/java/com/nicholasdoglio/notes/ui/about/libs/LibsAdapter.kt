package com.nicholasdoglio.notes.ui.about.libs

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
class LibsAdapter : RecyclerView.Adapter<LibsAdapter.LibsViewHolder>() {

    private lateinit var libraries: List<LibsItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibsViewHolder =
        LibsViewHolder(parent.inflate(R.layout.item_libs))

    override fun onBindViewHolder(holderLibs: LibsViewHolder, position: Int) =
        holderLibs.bindTo(libraries[position])

    override fun getItemCount(): Int = libraries.size

    fun setList(list: List<LibsItem>) {
        libraries = list
    }


    class LibsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindTo(library: LibsItem) {
            itemView.libTitle.setText(library.name)
            itemView.libLicense.setText(library.license)
        }
    }
}