package com.trucksup.field_officer.presenter.view.activity.vehicleVerify

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.controller.TripPlanBoxCantroler

class TripPlanBox (var context: Activity,  var message: String,var cantroler: TripPlanBoxCantroler) : Dialog(context) {

    var ok: TextView? = null
    var no: TextView? = null
    var meassageTexy: TextView?=null
    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //  this.getWindow()?.setFlags(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        setContentView(R.layout.trip_plan_box)
        this.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        //  this.getWindow()?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        //  this. getWindow()?.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);

        this.setCancelable(true)
        inst()
    }

    @SuppressLint("NewApi")
    fun inst() {

        this.getWindow()?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        );

        meassageTexy=findViewById(R.id.message)
        ok = findViewById(R.id.ok)
        no = findViewById(R.id.no)
        meassageTexy?.setText(message)
        ok?.setOnClickListener {
            cantroler.buyPlan()
            this.dismiss()

        }
        no?.setOnClickListener {

            this.dismiss()

        }

    }
}