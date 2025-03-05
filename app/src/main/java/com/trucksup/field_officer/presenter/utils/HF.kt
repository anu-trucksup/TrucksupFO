package com.trucksup.field_officer.presenter.utils

import android.app.Activity
import android.content.Context
import android.content.Intent

class HF(private val context: Context) {

    fun startActivity(activity: Activity) {
        context.startActivity(Intent(context, activity::class.java))
    }
}