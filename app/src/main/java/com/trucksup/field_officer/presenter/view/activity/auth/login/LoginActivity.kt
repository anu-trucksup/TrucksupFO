package com.trucksup.field_officer.presenter.view.activity.auth.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.authModel.LoginRequest
import com.trucksup.field_officer.databinding.ActivityLoginBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.Utils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.CommonApplication
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.auth.forget.ResetPasswordActivity
import com.trucksup.field_officer.presenter.view.activity.auth.signup.SignUpActivity
import com.trucksup.field_officer.presenter.view.activity.other.WelcomeLocationActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : BaseActivity(), View.OnClickListener {
    private var mLoginBinding: ActivityLoginBinding? = null
    private var mViewModel: LoginViewModel? = null

    private var loginPreferences: SharedPreferences? = null
    private var loginPrefsEditor: SharedPreferences.Editor? = null
    private var saveLogin: Boolean? = null

    //  private FirebaseAnalytics mFirebaseAnalytics;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        //Utils.setStatusBarColorAndIcons(this)

        loginPreferences = CommonApplication.getSharedPreferences()
        loginPrefsEditor = loginPreferences?.edit();

        saveLogin = loginPreferences?.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            mLoginBinding?.phoneTxt?.setText(loginPreferences?.getString("username", ""));
            mLoginBinding?.passwordTxt?.setText(loginPreferences?.getString("password", ""));
            mLoginBinding?.rbRemember?.setChecked(true);
        }
//        Show Password:
        //mLoginBinding?.passwordTxt?.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//        Hide Password:
//        mLoginBinding?.passwordTxt?.setTransformationMethod(PasswordTransformationMethod.getInstance());

        //click Listener
        mLoginBinding?.ivBackArrowLogin?.setOnClickListener(this)
        mLoginBinding?.createAccountTxt?.setOnClickListener(this)
        mLoginBinding?.forgetPasswordTxt?.setOnClickListener(this)

        mLoginBinding?.loginBtn?.setOnClickListener(this)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        mLoginBinding?.setViewModel(mViewModel)


        /* // set listener on radio button
         mLoginBinding?.rbRemember?.setOnCheckedChangeListener { compoundButton, isCheck ->
             // check condition
             if (isCheck) {
                 mLoginBinding?.rbRemember?.isChecked = false
             }else{
                 mLoginBinding?.rbRemember?.isChecked = true
             }
         }*/

        setupObserver()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setupObserver() {
        mViewModel?.resultLoginLD?.observe(this@LoginActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@LoginActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success != null) {
                    Toast.makeText(this, "Log in Successfully.", Toast.LENGTH_SHORT).show()
                    // move to home screen
                    Log.d("MYTAG",
                        "bearer token ==> " + responseModel.success.loginDetails.token)
                    val intent = Intent(this@LoginActivity, WelcomeLocationActivity::class.java)
                    intent.putExtra("status", 1)
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent)

                } else {

                    val abx = AlertBoxDialog(
                        this@LoginActivity,
                        responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }
            }
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.iv_back_arrow_login) {
            System.exit(0)

        } else if (view.id == R.id.create_account_txt) {
            val signIntent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(signIntent)

        } else if (view.id == R.id.forget_password_txt) {
            val resetPassword = Intent(this@LoginActivity, ResetPasswordActivity::class.java)
            // resetPassword.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(resetPassword)

        } else if (view.id == R.id.login_btn) {

            if (isOnline(this)) {
                if (TextUtils.isEmpty(mLoginBinding?.phoneTxt?.text.toString().trim())) {
                    mLoginBinding?.phoneTxt?.error = "Please enter mobile no."
                    mLoginBinding?.phoneTxt?.requestFocus()
                    /*LoggerMessage.onSNACK(
                        this.mLoginBinding?.phoneTxt!!,
                        "Please enter mobile no.",
                        this
                    )*/
                    return
                }

                if (mLoginBinding?.phoneTxt?.text.toString().length > 10) {
                    mLoginBinding?.phoneTxt?.error = "Please enter mobile no."
                    mLoginBinding?.phoneTxt?.requestFocus()
                }

                if (TextUtils.isEmpty(mLoginBinding?.passwordTxt?.text.toString().trim())) {
                    LoggerMessage.onSNACK(
                        mLoginBinding?.passwordTxt!!,
                        "Password should not empty.",
                        applicationContext
                    )

                    // mLoginBinding?.passwordTxt?.error = "Password should not empty."
                    mLoginBinding?.passwordTxt?.requestFocus()
                    return
                }


                if (mLoginBinding?.rbRemember?.isChecked!!) {
                    loginPrefsEditor?.putBoolean("saveLogin", true);
                    loginPrefsEditor?.putString(
                        "username",
                        mLoginBinding?.phoneTxt?.text.toString()
                    )
                    loginPrefsEditor?.putString(
                        "password",
                        mLoginBinding?.passwordTxt?.text.toString()
                    )
                    loginPrefsEditor?.commit();
                } else {
                    loginPrefsEditor?.clear();
                    loginPrefsEditor?.commit();
                }

                if (isValidMobile(mLoginBinding?.phoneTxt?.text.toString())) {
                    val request = LoginRequest(
                        requestedBy = PreferenceManager.getPhoneNo(this),
                        requestId = PreferenceManager.getRequestNo().toInt(),
                        requestDatetime = PreferenceManager.getServerDateUtc(),
                        deviceid = PreferenceManager.getAndroiDeviceId(this),
                        appVersion = "",
                        androidVersion = "",
                        profilename = PreferenceManager.getUserName(this),
                        profilephoto = "",
                        mobilenumber = mLoginBinding?.phoneTxt?.text.toString(),
                        password = mLoginBinding?.passwordTxt?.text.toString()
                    )

                    showProgressDialog(this, false)
                    mViewModel?.loginUser(PreferenceManager.getAuthToken(), request)
                } else {
                    LoggerMessage.onSNACK(
                        mLoginBinding?.phoneTxt!!,
                        "Mobile no should be 10 digit.",
                        applicationContext
                    )
                }

            } else {
                Utils.showToastDialog(
                    "No Internet connection.",
                    this, "Ok"
                )
            }
        }
    }

    // Function to validate password
    private fun isValidPassword(password: String): Boolean {
        // Regular expression for password validation
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$"
        return password.matches(passwordPattern.toRegex())

        //  tvPasswordError.text = "Password must be at least 8 characters, include a digit, an uppercase letter, and a special character."

    }

}