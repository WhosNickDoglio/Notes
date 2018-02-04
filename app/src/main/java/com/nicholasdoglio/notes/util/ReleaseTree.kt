package com.nicholasdoglio.notes.util

import timber.log.Timber

class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        //TODO set up error logging for release and also move this exclusively to release builds
    }
}