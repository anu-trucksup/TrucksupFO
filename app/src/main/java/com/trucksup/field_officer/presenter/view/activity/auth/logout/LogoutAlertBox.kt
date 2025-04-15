package com.trucksup.field_officer.presenter.view.activity.auth.logout

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.AddNewTruckLayoutBinding
import com.trucksup.field_officer.databinding.LogoutBoxBinding
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.view.service.LocationService


class LogoutAlertBox (var context: Activity, private var manager: LogoutManager) : Dialog(context){

    private lateinit var logoutBinding: LogoutBoxBinding

    init { setCancelable(false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        logoutBinding = LogoutBoxBinding.inflate(layoutInflater)
        setContentView(logoutBinding.root)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        this.window?.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);

        this.setCancelable(false)

        LoggerMessage.LogErrorMsg("Service ","Service is running "+isMyServiceRunning(
            LocationService::class.java))
        inst()
    }
    @SuppressLint("NewApi")
    fun inst() {
        this.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        logoutBinding.cancel.setOnClickListener {
            this.dismiss()
        }
        logoutBinding.confirm.setOnClickListener {
            manager.onClickLogout()
            dismiss()
        }

    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }



}