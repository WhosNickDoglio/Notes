package com.nicholasdoglio.notes.data.local

import com.nicholasdoglio.notes.BuildConfig
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.about.AboutItem
import com.nicholasdoglio.notes.data.model.about.LibsItem
import io.reactivex.Single

/**
 * @author Nicholas Doglio
 */
class AboutDataStore {
    private val aboutItems: MutableList<AboutItem> = mutableListOf()
    private val libs: MutableList<LibsItem> = mutableListOf()

    init {
        populateAbout()
        populateLibs()
    }

    private fun populateAbout() {
        aboutItems.add(
            AboutItem(
                R.drawable.dev_photo,
                "Developed by Nicholas Doglio",
                "https://whosnickdoglio.github.io/"
            )
        )
        aboutItems.add(AboutItem(R.drawable.ic_about, "Libraries", ""))
        aboutItems.add(
            AboutItem(
                R.drawable.ic_github,
                "Source Code",
                "https://github.com/WhosNickDoglio/Notes"
            )
        )
        aboutItems.add(
            AboutItem(
                R.drawable.ic_about,
                "Version: ${BuildConfig.VERSION_NAME}",
                "https://github.com/WhosNickDoglio/Notes/releases"
            )
        )
    }

    private fun populateLibs() {
        libs.add(
            LibsItem(
                R.string.android_support,
                R.string.support_components_license
            )
        )
        libs.add(
            LibsItem(
                R.string.android_architecture_components,
                R.string.support_components_license
            )
        )

        libs.add(LibsItem(R.string.rxjava, R.string.rxjava_android_license))
        libs.add(
            LibsItem(
                R.string.rxandroid,
                R.string.rxjava_android_license
            )
        )

        libs.add(LibsItem(R.string.rxbinding, R.string.rxbinding_license))
        libs.add(LibsItem(R.string.autodispose, R.string.autodispose_license))
        libs.add(LibsItem(R.string.dagger, R.string.dagger_license))
    }

    fun aboutItems(): Single<MutableList<AboutItem>> = Single.just(aboutItems)

    fun libItems(): Single<MutableList<LibsItem>> = Single.just(libs)
}