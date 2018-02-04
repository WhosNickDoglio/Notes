package com.nicholasdoglio.notes.data.local

import android.arch.lifecycle.MutableLiveData
import com.nicholasdoglio.notes.BuildConfig
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.about.AboutItem
import com.nicholasdoglio.notes.data.model.about.LibsItem

/**
 * @author Nicholas Doglio
 */
class AboutDataStore {
    private val aboutList: MutableLiveData<List<AboutItem>> = MutableLiveData()
    private val libraries: MutableLiveData<List<LibsItem>> = MutableLiveData()

    init {
        populateAbout()
        populateLibs()
    }

    private fun populateAbout() {
        val listOfAboutItems: MutableList<AboutItem> = mutableListOf()

        listOfAboutItems.add(
            AboutItem(
                R.drawable.dev_photo,
                "Developed by Nicholas Doglio",
                "https://whosnickdoglio.github.io/"
            )
        )
        listOfAboutItems.add(AboutItem(R.drawable.ic_about, "Libraries", ""))
        listOfAboutItems.add(
            AboutItem(
                R.drawable.ic_github,
                "Source Code",
                "https://github.com/WhosNickDoglio/Notes"
            )
        )
        listOfAboutItems.add(
            AboutItem(
                R.drawable.ic_about,
                "Version: ${BuildConfig.VERSION_NAME}",
                "https://github.com/WhosNickDoglio/Notes/releases"
            )
        )

        aboutList.value = listOfAboutItems
    }

    private fun populateLibs() {
        val listOfLibs: MutableList<LibsItem> = mutableListOf()

        listOfLibs.add(
            LibsItem(
                R.string.android_support,
                R.string.support_components_license
            )
        )
        listOfLibs.add(
            LibsItem(
                R.string.android_architecture_components,
                R.string.support_components_license
            )
        )

        listOfLibs.add(LibsItem(R.string.rxjava, R.string.rxjava_android_license))
        listOfLibs.add(
            LibsItem(
                R.string.rxandroid,
                R.string.rxjava_android_license
            )
        )

        listOfLibs.add(LibsItem(R.string.rxbinding, R.string.rxbinding_license))
        listOfLibs.add(LibsItem(R.string.autodispose, R.string.autodispose_license))
        listOfLibs.add(LibsItem(R.string.dagger, R.string.dagger_license))

        libraries.value = listOfLibs
    }

    fun aboutItems() = aboutList

    fun libItems() = libraries


}