package com.trucksup.field_officer.presenter.view.activity.other

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivitySplashBinding
import com.trucksup.field_officer.databinding.AlartBoxBinding
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import com.trucksup.field_officer.presenter.view.activity.dashboard.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : BaseActivity() {
    private var mSplashBinding: ActivitySplashBinding? = null
    private var splashViewModel: SplashViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mSplashBinding?.root)

        splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        if (isOnline(this)) {

            if (isLocationEnable(this)) {
                callMainScreen()
                setupObserver()
            } else {
                enableLocationPermission()
            }

        } else {
            val abx = AlertBoxDialog(
                this@SplashScreenActivity,
                "Please Check your Internet Connectivity.",
                "m"
            )
            abx.show()
        }
    }

    private fun setupObserver() {
        splashViewModel!!.loggedInStatusLD.observe(this) { status: Int? ->
            when (status) {
                0 -> {
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    intent.putExtra("mobile", "")
                    startActivity(intent)
                    finish()
                }

                1 -> {
                    val intent2 = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                    startActivity(intent2)
                    finish()
                }

            }
        }

        /* splashViewModel!!.forceUpdateResponseLD.observe(
            this, Observer<ResponseModel<ForceUpdateResponse>> { responseResponseModel: ResponseModel<ForceUpdateResponse> ->
                if (responseResponseModel.networkError != null) {
                    dismissProgressDialog()
                    callMainScreen()
                } else if (responseResponseModel.serverResponseError != null) {
                    dismissProgressDialog()
                    callMainScreen()
                } else if (responseResponseModel.genericError != null) {
                    dismissProgressDialog()
                    callMainScreen()
                } else {
                    dismissProgressDialog()
                    if (responseResponseModel.success?.payload != null) {
                        initDialog(responseResponseModel.success.payload)
                    } else {
                        callMainScreen()
                    }
                }
            })*/
    }

    /*private fun initDialog(payload: ForceUpdateItem) {                         // check force update
        val versionName = BuildConfig.VERSION_NAME
        Log.d("versionName", versionName)
        if (payload.version != versionName) {
            if (payload.status == "force") {
                //showToastDialog("Update SkipQ App with our latest version",this,payload);
            } else if (payload.status == "update") {
                // showToastDialogUpdate("New version availble to download",this,payload);
            } else {
                callMainScreen()
            }
        } else {
            callMainScreen()
        }
    }*/

    override fun onResume() {
        super.onResume()

        if (isOnline(this)) {

            if (isLocationEnable(this)) {
                callMainScreen()
                setupObserver()
            } else {
                enableLocationPermission()
            }

        } else {
            val abx = AlertBoxDialog(
                this@SplashScreenActivity,
                "Please Check your Internet Connectivity.",
                "m"
            )
            abx.show()
        }

    }

    private fun callMainScreen() {                // call main screen
        Handler().postDelayed({ splashViewModel?.checkLoggedInStatus() }, 2000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        // System.exit(0);
    }

    private fun locationDialog() {
        val builder = AlertDialog.Builder(this@SplashScreenActivity)
        val binding = AlartBoxBinding.inflate(LayoutInflater.from(this@SplashScreenActivity))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        binding.message.text = "Location permission is required to use this Application."

        //apply button
        binding.ok.setOnClickListener {
            dialog.dismiss()
        }

    }

}