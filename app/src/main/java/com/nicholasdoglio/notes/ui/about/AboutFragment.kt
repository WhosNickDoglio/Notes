package com.nicholasdoglio.notes.ui.about

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
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
import com.nicholasdoglio.notes.util.inflate
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_about.*
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class AboutFragment : Fragment() {

    @Inject
    lateinit var navigationController: NavigationController

    private val aboutAdapter by lazy { AboutAdapter(this.context!!, navigationController) }
    private val aboutViewModel by lazy {
        ViewModelProviders.of(this).get(AboutViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        aboutViewModel.list().observe(this, Observer { aboutAdapter.setList(it!!) })

        (activity as AppCompatActivity).setSupportActionBar(aboutToolbar)
        aboutToolbar.title = "About"

        val linearLayoutManager = LinearLayoutManager(this.context)
        aboutList.apply {
            adapter = aboutAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(this.context, linearLayoutManager.orientation))
            setHasFixedSize(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_about)
}