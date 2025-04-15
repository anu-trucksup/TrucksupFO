package com.trucksup.field_officer.presenter.view.activity.other

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivitySplashBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : BaseActivity() {
    private var mSplashBinding: ActivitySplashBinding? = null
    private var splashViewModel: SplashViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        setStatusBarColorAndIcons(this);
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        //splashViewModel.engLangDefault(this);
        //splashViewModel!!.syncLanguageLabels()

        if (isOnline(this)) {
            callMainScreen()
            // splashViewModel.getForceUpdate();                // force update call
            //splashViewModel.getCancellationPolicy();
            setupObserver()
        } else {
            //showToastDialogg("Please Check your Internet Connectivity.", this, "Ok");
            // Toast.makeText(this, "Please Check your Internet Connectivity.", Toast.LENGTH_SHORT).show();
        }
    }

    private fun setupObserver() {
        splashViewModel!!.loggedInStatusLD.observe(this) { status: Int? ->
            when (status) {
                0 -> {
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    intent.putExtra("mobile", "")
                    intent.putExtra("status", 0)
                    //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent)
                    showInAnimation()
                    finish()
                }

                1 -> {
                    val intent2 = Intent(this@SplashScreenActivity, MainActivity::class.java)
                    intent2.putExtra("status", 1)
                    //intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2)
                    showInAnimation()
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


    private fun callMainScreen() {                // call main screen
        Handler().postDelayed({ splashViewModel!!.checkLoggedInStatus() }, 4000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        // System.exit(0);
    }


    private fun setStatusBarColorAndIcons(activity: AppCompatActivity) {
        val window = activity.window
        val colorPrimary = ContextCompat.getColor(activity, R.color.colorPrimary)
        window.statusBarColor = colorPrimary

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val isLightColor = ColorUtils.calculateLuminance(colorPrimary) > 0.5
            var flags = window.decorView.systemUiVisibility
            flags = if (isLightColor) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            window.decorView.systemUiVisibility = flags
        }
    }


}