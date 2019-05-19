package com.nicholasdoglio.notes.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * @author Nicholas Doglio
 */

/**
 * Removes some boilerplate with inflating views
 * Used in onCreateView() methods in fragments and onCreateViewHolder() methods in adapters
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

/**  */
fun TextView.setEditableText(text: String) {
    this.text = text
    TextView.BufferType.EDITABLE
}

/** */
fun Activity.hideKeyboard() {
    val input = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    input.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun View.showIf(visibility: Boolean) {
    this.visibility = if (visibility) View.VISIBLE else View.GONE
}

fun FloatingActionButton.hideOnScroll(dy: Int) {
    if (dy > 0) this.hide() else this.show()
}

inline fun <reified VIEW_MODEL : ViewModel> Fragment.createViewModel(factory: ViewModelProvider.Factory): VIEW_MODEL =
    ViewModelProviders.of(this, factory).get(VIEW_MODEL::class.java)

fun Context.openWebPage(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(this.packageManager) != null) {
        this.startActivity(intent)
    }
}
