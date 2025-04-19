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
import android.widget.TextView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import com.trucksup.field_officer.presenter.view.activity.auth.signup.SignUpActivity

class AlertBoxDialog(var context: Activity, var message: String, var type: String) :
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
        setContentView(R.layout.alart_box)
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

        if (type == "verifymsgadhar") {
            verifymessagetxt?.visibility = View.VISIBLE
        }

        if (type == "p") {
            Linkify.addLinks(msgText!!, Linkify.ALL);
        }

        okButton?.setOnClickListener {
            Log.e("type", ">>>>>" + type)
            if (type == "m" || type == "p") {
                this.dismiss()
            } else if (type == "sign") {
                val intent = Intent(context, SignUpActivity::class.java)
                context.startActivity(intent)
                this.dismiss()
            } else if (type == "location") {
                this.dismiss()
            }/*else if (type == "verifymsgadhar") {
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

    }
}