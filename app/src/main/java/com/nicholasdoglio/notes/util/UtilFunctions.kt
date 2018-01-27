package com.nicholasdoglio.notes.util

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager


object UtilFunctions {
    fun hideSoftKeyboard(activity: AppCompatActivity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}