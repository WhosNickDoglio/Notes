package com.nicholasdoglio.notes.ui.about.libs

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.util.inflate
import com.nicholasdoglio.notes.util.setupToolbar
import com.nicholasdoglio.notes.viewmodel.NotesViewModelFactory
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.kotlin.autoDisposable
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_libs.*
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class LibsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: NotesViewModelFactory

    private val scopeProvider by lazy { AndroidLifecycleScopeProvider.from(this) }
    private val libsAdapter by lazy { LibsAdapter() }
    private val libsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LibsViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupToolbar(activity as AppCompatActivity, libsToolbar, "Licenses")

        libsViewModel.libs()
            .map { libsAdapter.setList(it.toList()) }
            .autoDisposable(scopeProvider)
            .subscribe()

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