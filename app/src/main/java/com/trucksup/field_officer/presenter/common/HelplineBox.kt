package com.trucksup.field_officer.presenter.common

import android.app.ActionBar
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.trucksup.field_officer.R


class HelplineBox(var context: Activity, var number: String) : Dialog(context) {

    private var cancle: TextView? = null
    private var confiram: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //  this.getWindow()?.setFlags(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        setContentView(R.layout.dail_call_box)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        this.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        );
        this.window?.setLayout(
            ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.FILL_PARENT
        );

        this.setCancelable(true)

        init()
    }

    fun init() {

        this.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        );
        cancle = findViewById(R.id.cancle)
        confiram = findViewById(R.id.confiram)

        cancle?.setOnClickListener {
            this.dismiss()
        }
        confiram?.setOnClickListener {
            this.dismiss()

            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + number)
            context.startActivity(intent)

        }

    }



}