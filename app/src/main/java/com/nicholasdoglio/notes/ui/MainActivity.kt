package com.nicholasdoglio.notes.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.crashlytics.android.Crashlytics
import com.nicholasdoglio.notes.BuildConfig
import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.ui.common.NavigationController
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
        if (!BuildConfig.DEBUG) Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)

        if (intent.action != null) {
            navigationController.openFragment(savedInstanceState, shortcutIntent = intent.action)
        } else {
            navigationController.openFragment(savedInstanceState, tileIntent = intent.extras)
        }
    }
}