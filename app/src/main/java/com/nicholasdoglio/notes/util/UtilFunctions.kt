package com.nicholasdoglio.notes.util

import android.content.Context
import android.content.pm.PackageManager

class UtilFunctions {

    fun versionNumber(context: Context): String {
        var version = ""

        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return "Version: $version"
    }
}