package com.trucksup.field_officer.presenter.view.activity.auth.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.authModel.LoginRequest
import com.trucksup.field_officer.databinding.ActivityLoginBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.AppVersionUtils
import com.trucksup.field_officer.presenter.common.location.LocationHelper
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mLoginBinding?.root)

        loginPreferences = CommonApplication.getSharedPreferences()
        loginPrefsEditor = loginPreferences?.edit()

        saveLogin = loginPreferences?.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            mLoginBinding?.phoneTxt?.setText(loginPreferences?.getString("username", ""));
            mLoginBinding?.passwordTxt?.setText(loginPreferences?.getString("password", ""));
            mLoginBinding?.rbRemember?.setChecked(true);
        } else {
            mLoginBinding?.phoneTxt?.setText(intent?.getStringExtra("mobile"))
        }


        //click Listener
        mLoginBinding?.ivBackArrowLogin?.setOnClickListener(this)
        mLoginBinding?.createAccountTxt?.setOnClickListener(this)
        mLoginBinding?.forgetPasswordTxt?.setOnClickListener(this)
        mLoginBinding?.phoneTxt?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().length==10)
                {
                    mLoginBinding?.phoneTxt?.hideKeyboard()
                }
            }
        })


        mLoginBinding?.loginBtn?.setOnClickListener(this)

        mViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

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

        checkLocationPermission(){
            Log.e("Location",latitude+"@"+longitude)
//            Toast.makeText(this, "Location :"+latitude +"-"+longitude, Toast.LENGTH_SHORT).show()
        }

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

                if (responseModel.success?.statuscode == 200) {
                    if (responseModel.success.loginDetails != null) {
                        val details = responseModel.success.loginDetails
                        val srdp = CommonApplication.getSharedPreferences()
                        srdp?.edit()?.putString("access_token", details.token)
                            ?.apply()
                        val jsonString = Gson().toJson(responseModel.success.loginDetails)
                        PreferenceManager.setUserData(jsonString, this)
                        PreferenceManager.setPhoneNo(mLoginBinding?.phoneTxt?.text.toString(), this)
                    }

                    val intent = Intent(this@LoginActivity, WelcomeLocationActivity::class.java)
                    startActivity(intent)
                    finish()

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

            if (latitude.isNullOrEmpty() || longitude.isNullOrEmpty()) {
                checkLocationPermission(){
                    Log.e("Location",latitude+"@"+longitude)
                    checkValidations()
                }
            }
            else
            {
                checkValidations()
            }

        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$"
        )
        return passwordRegex.matches(password)
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        System.exit(0)
       finishAffinity()
    }

    private fun checkValidations()
    {
        if (isOnline(this)) {
            if (TextUtils.isEmpty(mLoginBinding?.phoneTxt?.text.toString().trim())) {
                mLoginBinding?.phoneTxt?.error = getString(R.string.enter_mobile_no)
                mLoginBinding?.phoneTxt?.requestFocus()
                return
            }

            if (!isValidMobile(mLoginBinding?.phoneTxt?.text.toString())) {
                mLoginBinding?.phoneTxt?.error = getString(R.string.mobile_no_validation)
                mLoginBinding?.phoneTxt?.requestFocus()
                return
            }
            val password = mLoginBinding?.passwordTxt?.text.toString().trim()
            if (TextUtils.isEmpty(password)) {
                LoggerMessage.onSNACK(
                    mLoginBinding?.passwordTxt!!,
                    getString(R.string.enter_password),
                    applicationContext
                )

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


            if (isValidPassword(password)) {
                // Proceed
                val request = LoginRequest(
                    requestedBy = mLoginBinding?.phoneTxt?.text.toString(),
                    requestId = PreferenceManager.getRequestNo().toInt(),
                    requestDatetime = PreferenceManager.getServerDateUtc(),
                    deviceid = PreferenceManager.getAndroiDeviceId(this),
                    appVersion = AppVersionUtils.getAppVersionName(this),
                    androidVersion = Build.VERSION.SDK_INT.toString(),
                    profilename = "",
                    profilephoto = "",
                    mobilenumber = mLoginBinding?.phoneTxt?.text.toString(),
                    password = password,
                    latitude = latitude ?: "",
                    longitude = longitude ?: "",
                    confirmPassword = password
                )

                showProgressDialog(this, false)
                mViewModel?.loginUser(PreferenceManager.getAuthToken(), request)
            } else {
                LoggerMessage.onSNACK(
                    mLoginBinding?.passwordTxt!!,
                    getString(R.string.password_validation),
                    applicationContext
                )

                return
            }

        } else {

            val abx = AlertBoxDialog(
                this@LoginActivity,
                getString(R.string.no_internet),
                "m"
            )
            abx.show()
        }
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }


}