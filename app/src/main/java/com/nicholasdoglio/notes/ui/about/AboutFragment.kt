package com.nicholasdoglio.notes.ui.about

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.util.inflate
import com.nicholasdoglio.notes.util.setupToolbar
import com.nicholasdoglio.notes.viewmodel.NotesViewModelFactory
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_about.*
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class AboutFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: NotesViewModelFactory

    @Inject
    lateinit var navigationController: NavigationController

    private val aboutAdapter by lazy { AboutAdapter(this.context!!, navigationController) }
    private val aboutViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AboutViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupToolbar(activity as AppCompatActivity, aboutToolbar, "About")

        aboutViewModel.aboutItems().observe(this, Observer { aboutAdapter.setList(it!!) })

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