package com.nicholasdoglio.notes.ui.about.libs

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.util.inflate
import com.nicholasdoglio.notes.util.setupToolbar
import kotlinx.android.synthetic.main.fragment_libs.*

/**
 * @author Nicholas Doglio
 */
class LibsFragment : Fragment() {

    private val libsAdapter by lazy { LibsAdapter(this.context!!) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupToolbar(activity as AppCompatActivity, libsToolbar, "Licenses")


        libsList.apply {
            adapter = libsAdapter
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_libs)
}