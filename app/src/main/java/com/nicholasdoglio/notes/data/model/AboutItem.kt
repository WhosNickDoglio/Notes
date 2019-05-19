package com.nicholasdoglio.notes.data.model

import androidx.annotation.StringRes

/**
 * @author Nicholas Doglio
 * @param text text description of each item
 * @param link website URL for better understanding each of item
 */
data class AboutItem(@StringRes val text: Int, val action: AboutAction)

sealed class AboutAction {
    object OpenLibs : AboutAction()
    data class OpenWebsite(@StringRes val url: Int) : AboutAction()
}
