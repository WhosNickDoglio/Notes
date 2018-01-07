package com.nicholasdoglio.notes.ui.about


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicholasdoglio.notes.R
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * @author Nicholas Doglio
 */
class AboutFragment : DialogFragment() {
    //This is still really ugly but I'll come back to it later

    private lateinit var aboutAdapter: AboutAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        aboutAdapter = AboutAdapter(this.context!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
    }

    private fun setupList() {
        aboutList.adapter = aboutAdapter
        aboutList.layoutManager = LinearLayoutManager(this.context)
        aboutList.setHasFixedSize(true)
    }
}