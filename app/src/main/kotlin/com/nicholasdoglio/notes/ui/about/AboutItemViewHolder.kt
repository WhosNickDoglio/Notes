/*
 * MIT License
 *
 * Copyright (c) 2019 Nicholas Doglio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.nicholasdoglio.notes.ui.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.AboutItem
import com.nicholasdoglio.notes.data.model.AboutItem.Action
import com.nicholasdoglio.notes.util.openWebPage
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_about.*

class AboutItemViewHolder(override val containerView: View) :
    RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(model: AboutItem) {
        aboutItemName.apply {
            setText(model.text)
            setOnClickListener { click(model.action) }
        }
    }

    private fun click(action: Action): View.OnClickListener = View.OnClickListener {
        when (action) {
            is Action.OpenWebsite -> it.context.openWebPage(it.context.getString(action.url))
            is Action.OpenLibs -> it.findNavController().navigate(R.id.open_libs) // TODO fix this
        }
    }

    companion object {
        fun create(view: ViewGroup): AboutItemViewHolder =
            AboutItemViewHolder(
                LayoutInflater.from(view.context).inflate(
                    R.layout.item_about,
                    view,
                    false
                )
            )
    }
}
