package com.nicholasdoglio.notes.ui.about

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nicholasdoglio.notes.BuildConfig
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.about.AboutItem

/**
 * @author Nicholas Doglio
 */
class AboutViewModel : ViewModel() {

    private val aboutList: MutableLiveData<List<AboutItem>> = MutableLiveData()

    fun list(): MutableLiveData<List<AboutItem>> {
        val list: List<AboutItem> = listOf(
            AboutItem(
                R.drawable.dev_photo,
                "Developed by Nicholas Doglio",
                "https://whosnickdoglio.github.io/"
            ), AboutItem(R.drawable.ic_about, "Libraries", ""),
            AboutItem(
                R.drawable.ic_github,
                "Source Code",
                "https://github.com/WhosNickDoglio/Notes"
            ), AboutItem(
                R.drawable.ic_about,
                "Version: ${BuildConfig.VERSION_NAME}",
                "https://github.com/WhosNickDoglio/Notes/releases"
            )

        )

        aboutList.value = list

        return aboutList
    }
}