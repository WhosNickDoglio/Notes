package com.nicholasdoglio.notes.ui.about

import android.arch.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.local.AboutDataStore
import javax.inject.Inject

class AboutViewModel @Inject constructor(private val aboutDataStore: AboutDataStore) : ViewModel() {

    fun aboutItems() = aboutDataStore.aboutItems()


}
