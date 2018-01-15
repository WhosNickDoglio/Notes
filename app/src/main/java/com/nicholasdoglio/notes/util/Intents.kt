package com.nicholasdoglio.notes.util

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * @author Nicholas Doglio
 */
object Intents {
    fun openWebPage(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }
}