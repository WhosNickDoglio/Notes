package com.nicholasdoglio.notes.util

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.NameNotFoundException
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager


class UtilFunctions {

    fun versionNumber(context: Context): String {
        var version = ""

        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = packageInfo.versionName
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }

        return "Version: $version"
    }

    fun hideSoftKeyboard(context: Context, activity: AppCompatActivity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}