package com.nicholasdoglio.notes

import android.os.StrictMode
import com.nicholasdoglio.notes.di.DaggerAppComponent
import com.nicholasdoglio.notes.util.ReleaseTree
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber


/**
 * @author Nicholas Doglio
 */
class NotesApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().application(this).build()

    override fun onCreate() {
        super.onCreate()
        initLeakCanary()
        initDebugTools()
    }

    private fun initDebugTools() {
        when (BuildConfig.DEBUG) {
            true -> {
                initStrictMode()
                Timber.plant(Timber.DebugTree())
            }
            false -> Timber.plant(ReleaseTree())
        }
    }

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build()
        )

        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build()
        )
    }

    private fun initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }

        LeakCanary.install(this)
    }
}