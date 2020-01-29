/*
 * MIT License
 *
 * Copyright (c) 2020 Nicholas Doglio
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

package com.nicholasdoglio.notes.features.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.about.AboutItem
import com.nicholasdoglio.notes.data.about.AboutItem.Action
import com.nicholasdoglio.notes.util.openWebPage

class AboutItemViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
    private val aboutItemName: TextView = rootView.findViewById(R.id.about_item_name)

    fun bind(model: AboutItem, navController: NavController) {
        aboutItemName.apply {
            setText(model.text)
            setOnClickListener {
                when (model.action) {
                    is Action.OpenWebsite ->
                        context.openWebPage(context.getString(model.action.url))
                    is Action.OpenLibs -> navController.navigate(R.id.open_libs)
                }
            }
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
