package com.nicholasdoglio.notes.ui.about.libs


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicholasdoglio.notes.R
import kotlinx.android.synthetic.main.fragment_libs.*

/**
 * @author Nicholas Doglio
 */
class LibsFragment : Fragment() {

    private lateinit var libsAdapter: LibsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        libsAdapter = LibsAdapter(this.context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_libs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupList()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(libsToolbar)
        libsToolbar.title = "Licenses"
    }

    private fun setupList() {
        libsList.adapter = libsAdapter
        libsList.layoutManager = LinearLayoutManager(this.context)
    }
}