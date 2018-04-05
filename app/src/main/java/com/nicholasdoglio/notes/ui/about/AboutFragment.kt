package com.nicholasdoglio.notes.ui.about

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.MainActivity
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.ui.common.OnBackPressedListener
import com.nicholasdoglio.notes.util.inflate
import com.nicholasdoglio.notes.util.setupToolbar
import com.nicholasdoglio.notes.viewmodel.NotesViewModelFactory
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_about.*
import org.jetbrains.anko.support.v4.browse
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class AboutFragment : DaggerFragment(), OnBackPressedListener {
    @Inject
    lateinit var viewModelFactory: NotesViewModelFactory

    @Inject
    lateinit var navigationController: NavigationController

    private lateinit var linearLayoutManager: LinearLayoutManager

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val aboutAdapter by lazy { AboutAdapter() }
    private val aboutViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AboutViewModel::class.java)
    }

    override fun doBack() {
        activity!!.supportFragmentManager.popBackStack()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mainActivity = activity as MainActivity
        mainActivity.setOnBackPressedListener(this)

        setupToolbar(activity as AppCompatActivity, aboutToolbar, "About")

        aboutViewModel
            .aboutItems()
            .map { aboutAdapter.setList(it.toList()) }
            .autoDisposable(scopeProvider)
            .subscribe()

        linearLayoutManager = LinearLayoutManager(this.context)
        aboutList.apply {
            adapter = aboutAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(DividerItemDecoration(this.context, linearLayoutManager.orientation))
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        aboutAdapter.aboutListClickListener()
            .doOnNext { Timber.d(it.text) }
            .map {
                when (it.link) {
                    "" -> navigationController.openLibs()
                    else -> this.browse(it.link)
                }
            }
            .autoDisposable(scopeProvider)
            .subscribe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_about)
}