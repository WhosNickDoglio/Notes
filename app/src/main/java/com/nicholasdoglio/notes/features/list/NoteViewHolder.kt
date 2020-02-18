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

package com.nicholasdoglio.notes.features.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.nicholasdoglio.notes.Note
import com.nicholasdoglio.notes.R

class NoteViewHolder(private val rootView: View) : RecyclerView.ViewHolder(rootView) {
    private val note: ConstraintLayout = rootView.findViewById(R.id.note)
    private val titleListItem: TextView = rootView.findViewById(R.id.title_list_item)
    private val contentsListItem: TextView = rootView.findViewById(R.id.contents_list_item)

    fun bind(model: Note, navController: NavController) {
        titleListItem.text = model.title
        contentsListItem.text = model.contents
        note.setOnClickListener {
            navController.navigate(NoteListFragmentDirections.openNote(model.id))
        }
    }

    companion object {
        fun create(view: ViewGroup): NoteViewHolder =
            NoteViewHolder(
                LayoutInflater.from(view.context).inflate(
                    R.layout.item_note,
                    view,
                    false
                )
            )
    }
}
