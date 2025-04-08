package com.trucksup.field_officer.presenter.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.trucksup.field_officer.R

class ScheduleAlertBoxDialog(var context: Activity, var message: String, var type: String) :
    Dialog(context) {
    private var msgText: TextView? = null
    private var okButton: TextView? = null
    private var verifymessagetxt: TextView? = null

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.schedule_alertbox)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        this.setCancelable(true)
        initializeUI()
    }

    @SuppressLint("NewApi")
    fun initializeUI() {

        this.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )

        msgText = findViewById(R.id.message)

        verifymessagetxt = findViewById(R.id.verify_message_txt)
        okButton = findViewById(R.id.ok)
        msgText?.text = message

        okButton?.setOnClickListener {

            this.dismiss()


        }

    }
}