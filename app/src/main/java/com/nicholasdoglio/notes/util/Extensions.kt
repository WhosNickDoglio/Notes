package com.nicholasdoglio.notes.util

import android.app.Activity
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

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

/**
 * Removes boilerplate with navigating between fragments
 * Used in NavigationController
 */
fun AppCompatActivity.showFragment(
    fragment: Fragment,
    tag: String,
    name: String,
    transition: Int,
    @IdRes containerViewId: Int
) {
    supportFragmentManager
        .beginTransaction()
        .replace(containerViewId, fragment, tag)
        .addToBackStack(name)
        .setTransition(transition)
        .setReorderingAllowed(true)
        .commit()
}

/** Sets up toolbars in all the fragments */
fun Fragment.setupToolbar(
    activity: AppCompatActivity,
    toolbar: Toolbar,
    title: String = "",
    optionsMenu: Boolean = false
) {
    activity.setSupportActionBar(toolbar)
    toolbar.title = title
    setHasOptionsMenu(optionsMenu)
}

fun TextView.setEditableText(text: String) {
    setText(text)
    TextView.BufferType.EDITABLE
}

fun AppCompatActivity.hideKeyboard() {
    val input = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    input.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}