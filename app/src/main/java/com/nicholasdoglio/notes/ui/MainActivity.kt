package com.nicholasdoglio.notes.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.crashlytics.android.Crashlytics
import com.nicholasdoglio.notes.BuildConfig
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.common.NavigationController
import com.nicholasdoglio.notes.util.Const
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.HasSupportFragmentInjector
import io.fabric.sdk.android.Fabric
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class MainActivity : DaggerAppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var navigationController: NavigationController

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> =
        dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        when (BuildConfig.DEBUG) {
            false -> Fabric.with(this, Crashlytics())
        }

        if (savedInstanceState == null) {
            navigationController.openList()
        }

        if (Const.shortcutNoteIntentId == intent.action) {
            navigationController.openNote()
        }

        intent.extras?.let {
            if (intent.extras.containsKey(Const.shortcutNoteIntentId)) {
                navigationController.openNote()
            }
        }
    }
}