package com.nicholasdoglio.notes.util

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * @author Nicholas Doglio
 */
object Intents {
    fun openWebPage(context: Context, url: String) {
        val webpage = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

}