package com.trucksup.field_officer.presenter.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.trucksup.field_officer.R

object LoggerMessage {
    fun logErrorMsg(tag: String, message: String?) {
        if (message != null)
            Log.e(tag, message)
    }

    fun toastPrint(tag: String, activity: Context?) {
        Toast.makeText(activity, tag, Toast.LENGTH_SHORT).show()

    }

    fun onSNACK(view: View, message:String, context: Context){
        val snackBar =
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)

        snackBar.setAction("OK") {
            snackBar.dismiss()
        }
        snackBar.setTextColor(context.resources.getColor(R.color.white))
        snackBar.setBackgroundTint(context.resources.getColor(R.color.red))
        snackBar.setActionTextColor(context.resources.getColor(R.color.white))
        snackBar.show()
    }

    fun LogErrorMsg(s: String, path: String?) {

    }
}