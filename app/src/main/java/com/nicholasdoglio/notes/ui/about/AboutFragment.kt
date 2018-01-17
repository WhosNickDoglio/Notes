package com.nicholasdoglio.notes.ui.about

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.common.NavigationController
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_about.*
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class AboutFragment : Fragment() {

    @Inject
    lateinit var navigationController: NavigationController

    private lateinit var aboutAdapter: AboutAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        aboutAdapter = AboutAdapter(this.context!!, navigationController)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupList()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(aboutToolbar)
        aboutToolbar.title = ""
    }

    private fun setupList() {
        val layoutManager = LinearLayoutManager(this.context)

        aboutList.adapter = aboutAdapter
        aboutList.layoutManager = layoutManager
        aboutList.addItemDecoration(DividerItemDecoration(this.context, layoutManager.orientation))
        aboutList.setHasFixedSize(true)
    }
}