package com.trucksup.field_officer.presenter.common

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log

object AppVersionUtils {

    fun getAppVersionName(context: Context): String {
        return try {
            val packageInfo = getPackageInfo(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode.toString()
            } else {
                packageInfo.versionCode.toString()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("AppVersionUtils", "Error getting app version name", e)
            "Unknown"
        }
    }

    fun getAppVersionCode(context: Context): Long {
        return try {
            val packageInfo = getPackageInfo(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("AppVersionUtils", "Error getting app version code", e)
            -1
        }
    }

    private fun getPackageInfo(context: Context): PackageInfo {
        val packageManager = context.packageManager
        val packageName = context.packageName
        return packageManager.getPackageInfo(packageName, 0)
    }
}