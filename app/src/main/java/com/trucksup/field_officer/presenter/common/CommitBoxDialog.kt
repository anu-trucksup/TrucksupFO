package com.trucksup.field_officer.presenter.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import com.trucksup.field_officer.presenter.view.activity.auth.signup.SignUpActivity
import com.trucksup.field_officer.presenter.view.activity.dashboard.HomeActivity

class CommitBoxDialog(var context: Activity, private var timeDate: String) :
    Dialog(context) {
    private var dateText: TextView? = null
    private var closeButton: ImageView? = null

    init { setCancelable(false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_today_commit)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        this.setCancelable(true)
        initializeUI()
    }

    @SuppressLint("NewApi")
    fun initializeUI() {

        this.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )

        dateText = findViewById(R.id.tv_date)
        dateText?.text = timeDate
        closeButton = findViewById(R.id.iv_close)
        closeButton?.setOnClickListener {
            this.dismiss()
        }

    }
}