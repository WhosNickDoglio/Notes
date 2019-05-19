package com.nicholasdoglio.notes.data.local

import com.nicholasdoglio.notes.R
import com.nicholasdoglio.notes.data.model.AboutAction
import com.nicholasdoglio.notes.data.model.AboutItem
import io.reactivex.Single
import javax.inject.Inject

/**
 * @author Nicholas Doglio
 */
class AboutDataStore @Inject constructor() {
    private val items: List<AboutItem> = listOf(
        AboutItem(R.string.dev, AboutAction.OpenWebsite(R.string.dev_url)),
        AboutItem(R.string.libs, AboutAction.OpenLibs),
        AboutItem(R.string.source_code, AboutAction.OpenWebsite(R.string.source_code_url))
    )

    val aboutItems: Single<List<AboutItem>> = Single.just(items)
}
