package com.nicholasdoglio.notes.ui.about.libs

import android.arch.lifecycle.ViewModel
import com.nicholasdoglio.notes.data.local.AboutDataStore
import javax.inject.Inject

class LibsViewModel @Inject constructor(private val aboutDataStore: AboutDataStore) : ViewModel() {

    fun libs() = aboutDataStore.libItems()
}
