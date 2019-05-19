package com.nicholasdoglio.notes.ui.about

import androidx.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.local.AboutDataStore
import com.nicholasdoglio.notes.data.model.AboutItem
import io.reactivex.Single
import javax.inject.Inject

class AboutViewModel @Inject constructor(aboutDataStore: AboutDataStore) : ViewModel() {

    val aboutItems: Single<List<AboutItem>> = aboutDataStore.aboutItems
}
