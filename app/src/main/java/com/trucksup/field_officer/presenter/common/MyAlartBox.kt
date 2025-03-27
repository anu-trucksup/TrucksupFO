package com.trucksup.field_officer.presenter.common

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
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.utils.PreferenceManager

class MyAlartBox(var context: Activity, var message: String, var type: String) : Dialog(context) {
    var meassageTexy: TextView? = null
    var ok: TextView? = null
    var verifymessagetxt: TextView? = null
    var imgLogo: ImageView? = null

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //  this.getWindow()?.setFlags(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT);
        setContentView(R.layout.alart_box)
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

        meassageTexy = findViewById(R.id.message)
        imgLogo = findViewById(R.id.imgLogo)
        verifymessagetxt = findViewById(R.id.verify_message_txt)
        ok = findViewById(R.id.ok)
        meassageTexy?.setText(message)

        if (type == "verifymsgadhar") {
            verifymessagetxt?.visibility = View.VISIBLE
        }

        if (type == "p") {
            Linkify.addLinks(meassageTexy!!, Linkify.ALL);
        }

        ok?.setOnClickListener {
            Log.e("type", ">>>>>" + type)
            if (type == "m") {
                this.dismiss()
            } else if (type == "p") {
                this.dismiss()
            } /*else if (type == "verifymsgadhar") {
                var intent: Intent = Intent(context, MainActivity::class.java)
                intent.putExtra("type", "xxx")
                context.startActivity(intent)
                this.dismiss()
                context.finishAffinity()
            } else if (type == "cl") {
                this.dismiss()
                context.finish()
            } else if (type == "i") {
                var intent: Intent = Intent(context, EditBussinDoc::class.java)
                intent.putExtra("type", "xxx")
                context.startActivity(intent)
                this.dismiss()
            } else if (type == "b") {
                if (isMyServiceRunning(LocationService::class.java, context)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.stopService(Intent(context, LocationService::class.java))
                    } else {
                        context.stopService(Intent(context, LocationService::class.java))
                    }
                }
                PreferenceManager.removeData(context)
                PreferenceManager.setLogout(false, context)

                val i = Intent(context, PhoneNo::class.java)

                context.startActivity(i)
                context.finishAffinity()
                this.dismiss()
            }*/

        }

        if (PreferenceManager.getLanguage(context) == "en") {
            imgLogo?.setImageResource(R.drawable.logo_blue)
        } else {
            imgLogo?.setImageResource(R.drawable.logo_blue)
        }

    }
}