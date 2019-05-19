package com.nicholasdoglio.notes

import android.os.StrictMode
import com.nicholasdoglio.notes.di.DaggerAppComponent
import com.uber.rxdogtag.RxDogTag
import com.uber.rxdogtag.autodispose.AutoDisposeConfigurer
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

/**
 * @author Nicholas Doglio
 */
class NotesApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(this)

    override fun onCreate() {
        super.onCreate()
        initDebugTools()
    }

    private fun initDebugTools() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            initStrictMode()
            RxDogTag.builder()
                .configureWith(AutoDisposeConfigurer::configure)
                .install()
        }
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build()
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }
}
