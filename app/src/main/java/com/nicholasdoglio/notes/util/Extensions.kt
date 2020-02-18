/*
 * MIT License
 *
 * Copyright (c) 2020 Nicholas Doglio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.nicholasdoglio.notes.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.aboutlibraries.LibsConfiguration
import com.mikepenz.aboutlibraries.entity.Library

fun Activity.hideKeyboard() {
    val input = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    input.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun Context.openWebPage(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(this.packageManager) != null) startActivity(intent)
}

inline fun LibsBuilder.withListener(
    crossinline onExtraClicked: (v: View, specialButton: Libs.SpecialButton) -> Boolean =
        { _, _ -> true },
    crossinline onIconClicked: (v: View) -> Unit = {},
    crossinline onIconLongClicked: (v: View) -> Boolean = { true },
    crossinline onLibraryAuthorClicked: (v: View, library: Library) -> Boolean =
        { _, _ -> true },
    crossinline onLibraryAuthorLongClicked: (v: View, library: Library) -> Boolean =
        { _, _ -> true },
    crossinline onLibraryBottomClicked: (v: View, library: Library) -> Boolean =
        { _, _ -> true },
    crossinline onLibraryBottomLongClicked: (v: View, library: Library) -> Boolean =
        { _, _ -> true },
    crossinline onLibraryContentClicked: (v: View, library: Library) -> Boolean =
        { _, _ -> true },
    crossinline onLibraryContentLongClicked: (v: View, library: Library) -> Boolean =
        { _, _ -> true }
): LibsBuilder {
    LibsConfiguration.instance.listener = object : LibsConfiguration.LibsListener {
        override fun onExtraClicked(v: View, specialButton: Libs.SpecialButton): Boolean =
            onExtraClicked(v, specialButton)

        override fun onIconClicked(v: View) {
            onIconClicked(v)
        }

        override fun onIconLongClicked(v: View): Boolean = onIconLongClicked(v)

        override fun onLibraryAuthorClicked(v: View, library: Library): Boolean =
            onLibraryAuthorClicked(v, library)

        override fun onLibraryAuthorLongClicked(v: View, library: Library): Boolean =
            onLibraryAuthorLongClicked(v, library)

        override fun onLibraryBottomClicked(v: View, library: Library): Boolean =
            onLibraryBottomClicked(v, library)

        override fun onLibraryBottomLongClicked(v: View, library: Library): Boolean =
            onLibraryBottomLongClicked(v, library)

        override fun onLibraryContentClicked(v: View, library: Library): Boolean =
            onLibraryContentClicked(v, library)

        override fun onLibraryContentLongClicked(v: View, library: Library): Boolean =
            onLibraryContentLongClicked(v, library)
    }

    return this
}
