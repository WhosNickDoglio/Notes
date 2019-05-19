package com.nicholasdoglio.notes.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.AboutAction
import com.nicholasdoglio.notes.di.NotesViewModelFactory
import com.nicholasdoglio.notes.util.createViewModel
import com.nicholasdoglio.notes.util.openWebPage
import com.uber.autodispose.android.lifecycle.autoDisposable
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_about.*
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class AboutFragment : DaggerDialogFragment() {

    @Inject
    lateinit var viewModelFactory: NotesViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_about, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: AboutViewModel = createViewModel(viewModelFactory)

        val aboutAdapter = AboutAdapter()

        aboutRecyclerView.apply {
            adapter = aboutAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.aboutItems
            .autoDisposable(viewLifecycleOwner)
            .subscribe { list -> aboutAdapter.submitList(list) }

        aboutAdapter.aboutListClickListener
            .autoDisposable(viewLifecycleOwner)
            .subscribe {
                when (it.action) {
                    is AboutAction.OpenWebsite ->
                        requireContext().openWebPage(requireContext().getString(it.action.url))
                    is AboutAction.OpenLibs -> LibsFragment().show(requireFragmentManager(), "LIBS")
                }
            }
    }
}
