package com.nicholasdoglio.notes.util

import android.util.Log
import com.google.firebase.crash.FirebaseCrash
import timber.log.Timber

class ReleaseTree : Timber.Tree() {

    //TODO look into moving this to exclusively on the release build

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return when (priority) {
            Log.VERBOSE -> false
            Log.DEBUG -> false
            Log.INFO -> false
            else -> true
        }
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (isLoggable(tag, priority)) {
            when (priority) {
                Log.ERROR -> {
                    FirebaseCrash.log(message)
                }
            }
        }
    }
}