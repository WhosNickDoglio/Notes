package com.nicholasdoglio.notes.util

import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * @author Nicholas Doglio
 */

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

//Change this
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