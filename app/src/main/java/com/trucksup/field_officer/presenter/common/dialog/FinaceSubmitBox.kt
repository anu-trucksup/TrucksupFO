package com.trucksup.field_officer.presenter.common.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.trucksup.field_officer.R

class FinaceSubmitBox(
    var context: Activity,
    var message: String,
    var message1: String,
    var type: String
) : Dialog(context) {
    var meassageTexy: TextView? = null
    var meassageTexy1: TextView? = null
    var ok: TextView? = null

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //  this.getWindow()?.setFlags(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        setContentView(R.layout.finance_insurance_submit_lay)
        this.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        //  this.getWindow()?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        //  this. getWindow()?.setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);

        // this.setCancelable(true)
        inst()
    }

    @SuppressLint("NewApi")
    fun inst() {

        this.getWindow()?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        );

        meassageTexy = findViewById(R.id.message)
        meassageTexy1 = findViewById(R.id.message1)
        ok = findViewById(R.id.ok)
        meassageTexy?.setText(message)
        meassageTexy1?.setText(message1)



        ok?.setOnClickListener {
            this.dismiss()
            context.finish()
        }

    }
}