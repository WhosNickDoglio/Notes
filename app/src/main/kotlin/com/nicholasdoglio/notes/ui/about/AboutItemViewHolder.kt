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
import androidx.databinding.ViewDataBinding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nicholasdoglio.notes.BR
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.AboutAction
import com.nicholasdoglio.notes.data.model.AboutItem
import com.nicholasdoglio.notes.databinding.ItemAboutBinding
import com.nicholasdoglio.notes.util.openWebPage

class AboutItemViewHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: AboutItem) {
        binding.setVariable(BR.item, model)
        binding.setVariable(BR.click, click(model.action))
        binding.executePendingBindings()
    }

    private fun click(action: AboutAction): View.OnClickListener = View.OnClickListener {
        when (action) {
            is AboutAction.OpenWebsite -> it.context.openWebPage(it.context.getString(action.url))
            is AboutAction.OpenLibs -> it.findNavController().navigate(R.id.open_libs) // TODO fix this
        }
    }

    companion object {
        fun create(view: ViewGroup): AboutItemViewHolder {
            val layoutInflater = LayoutInflater.from(view.context)
            val binding = ItemAboutBinding.inflate(layoutInflater, view, false)
            return AboutItemViewHolder(binding)
        }
    }
}
