package com.trucksup.field_officer.presenter.view.activity.auth.forget

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.authModel.ForgetRequest
import com.trucksup.field_officer.data.model.authModel.LoginRequest
import com.trucksup.field_officer.databinding.ActivityCreatePasswordBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.AppVersionUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import com.trucksup.field_officer.presenter.view.activity.other.WelcomeLocationActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class CreatePasswordActivity : BaseActivity(), View.OnClickListener {
    private var mBinding: ActivityCreatePasswordBinding? = null
    private var mViewModel: CreatePasswordViewModel? = null
    private var mobileNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_password)
        //click Listener

        mViewModel = ViewModelProvider(this)[CreatePasswordViewModel::class.java]

        mBinding?.topView?.ivBack?.setOnClickListener(this)
        mBinding?.updatepassBtn?.setOnClickListener(this)

        mobileNumber = intent.extras!!.getString("phoneNo", "")

        setupObserver()

        checkLocationPermission(){
            Log.e("Location",latitude+"@"+longitude)
//            Toast.makeText(this, "Location :"+latitude +"-"+longitude, Toast.LENGTH_SHORT).show()
        }

        //password
        mBinding!!.passwordTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val password: String = mBinding!!.passwordTxt.getText().toString()
                val confirmPassword: String = mBinding!!.confirmPasswordTxt.getText().toString()
                mBinding?.confirmPasswordTxt?.text?.clear()
                mBinding?.confirmPasswordTxt?.clearFocus()

            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        //confirem password
        mBinding!!.confirmPasswordTxt.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
             }

             override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                 val password: String = mBinding!!.passwordTxt.getText().toString()
                 val confirmPassword: String = mBinding!!.confirmPasswordTxt.getText().toString()
                 if (password.isNullOrEmpty()) {
                     LoggerMessage.onSNACK(mBinding!!.passwordTxt, resources.getString(R.string.enter_password), applicationContext)
                 }
                 else
                 {
                     if (isValidPassword(password)) {
                         if (confirmPassword.isNullOrEmpty()) {
                             LoggerMessage.onSNACK(
                                 mBinding!!.confirmPasswordTxt,
                                 getString(R.string.enter_confirm_password),
                                 applicationContext
                             )
                         }
                         else
                         {
                             if (confirmPassword.length > 0 && password.length > 0) {
                                 if (!confirmPassword.toString().equals(password)) {
                                     val customErrorDrawable =
                                         resources.getDrawable(R.drawable.error_warn)
                                     customErrorDrawable.setBounds(
                                         0,
                                         0,
                                         customErrorDrawable.intrinsicWidth,
                                         customErrorDrawable.intrinsicHeight
                                     )

                                     mBinding?.confirmPasswordTxt?.setError(
                                         getString(R.string.password_match), customErrorDrawable
                                     )

                                     // give an error that password and confirm password not match
                                 }
                                 else
                                 {
                                     val customErrorDrawable = resources.getDrawable(R.drawable.error_confirm)
                                     customErrorDrawable.setBounds(
                                         0,
                                         0,
                                         customErrorDrawable.intrinsicWidth,
                                         customErrorDrawable.intrinsicHeight
                                     )

                                     mBinding!!.confirmPasswordTxt.setError(
                                         getString(R.string.password_match_msg),
                                         customErrorDrawable
                                     )
                                 }
                             }
                         }
                     }
                     else
                     {
                         LoggerMessage.onSNACK(
                             mBinding?.passwordTxt!!,
                             getString(R.string.password_validation),
                             applicationContext
                         )
                     }
                 }
             }

             override fun afterTextChanged(editable: Editable) {
//                 val password: String = mBinding!!.passwordTxt.getText().toString()
//                 if (editable.length > 0 && password.length > 0) {
//                     if (!editable.toString().equals(password)) {
//                         val customErrorDrawable = resources.getDrawable(R.drawable.error_warn)
//                         customErrorDrawable.setBounds(
//                             0,
//                             0,
//                             customErrorDrawable.intrinsicWidth,
//                             customErrorDrawable.intrinsicHeight
//                         )
//
//                         mBinding?.confirmPasswordTxt?.setError(
//                             "Password and Confirm Password should be same.",
//                             customErrorDrawable
//                         )
//
//                         // give an error that password and confirm password not match
//                     } else {
//                         val customErrorDrawable = resources.getDrawable(R.drawable.error_confirm)
//                         customErrorDrawable.setBounds(
//                             0,
//                             0,
//                             customErrorDrawable.intrinsicWidth,
//                             customErrorDrawable.intrinsicHeight
//                         )
//                         mBinding?.confirmPasswordTxt?.setError("", customErrorDrawable)
//                     }
//                 }
             }
         })
    }


    private fun setupObserver() {

        mViewModel?.resultResetLD?.observe(this@CreatePasswordActivity) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@CreatePasswordActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {
//                    Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                    // start home screen
                    val intent = Intent(this@CreatePasswordActivity, LoginActivity::class.java)
                    intent.putExtra("mobile", "")
                    startActivity(intent)
                    finish()

                } else {
                    val abx = AlertBoxDialog(
                        this@CreatePasswordActivity,
                        responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }

            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onClick(view: View) {
        if (view.id == R.id.iv_back) {
            onBackPressed()
        } else if (view.id == R.id.updatepass_btn) {

            if (TextUtils.isEmpty(mBinding?.passwordTxt?.text.toString().trim())) {
                mBinding?.passwordTxt?.error = getString(R.string.enter_password)
                mBinding?.passwordTxt?.requestFocus()
                return
            }

            val password = mBinding!!.passwordTxt.text.toString()

            if (isValidPassword(password)) {

                if (mBinding?.confirmPasswordTxt?.text.toString().isEmpty()) {
                    LoggerMessage.onSNACK(
                        mBinding!!.confirmPasswordTxt,
                        getString(R.string.enter_confirm_password),
                        applicationContext
                    )
                    return
                }
                val confirmpassword = mBinding!!.confirmPasswordTxt.text.toString()

                if (confirmpassword.length > 0 && password.length > 0) {
                    if (!confirmpassword.equals(password)) {
                        val customErrorDrawable = resources.getDrawable(R.drawable.error_warn)
                        customErrorDrawable.setBounds(
                            0, 0,
                            customErrorDrawable.intrinsicWidth, customErrorDrawable.intrinsicHeight
                        )

                        mBinding?.confirmPasswordTxt?.setError(
                            getString(R.string.password_match), customErrorDrawable
                        )
                        //  mSignUpBinding!!.confirmPasswordTxt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.error_confirm, 0);
                        return
                    } else {

                        val customErrorDrawable = resources.getDrawable(R.drawable.error_confirm)
                        customErrorDrawable.setBounds(
                            0,
                            0,
                            customErrorDrawable.intrinsicWidth,
                            customErrorDrawable.intrinsicHeight
                        )

                        mBinding!!.confirmPasswordTxt.setError(
                            getString(R.string.password_match_msg),
                            customErrorDrawable
                        )

                    }

                }
            } else {
                LoggerMessage.onSNACK(
                    mBinding?.passwordTxt!!,
                    getString(R.string.password_validation),
                    applicationContext
                )

                return
            }

            // Proceed

            showProgressDialog(this, false)

            val request = ForgetRequest(
                requestedBy = mobileNumber.toString(),
                requestId = PreferenceManager.getRequestNo().toInt(),
                requestDatetime = PreferenceManager.getServerDateUtc(),
                deviceid = PreferenceManager.getAndroiDeviceId(this),
                appVersion = AppVersionUtils.getAppVersionName(this),
                androidVersion = Build.VERSION.SDK_INT.toString(),
                profilename = "",
                profilephoto = "",
                mobilenumber = mobileNumber.toString(),
                password = password.toString(),
                latitude = latitude,
                longitude = longitude,
                confirmPassword = mBinding!!.confirmPasswordTxt.text.toString()
            )
            mViewModel?.resetPassword(
                PreferenceManager.getAuthToken(), request
            )


        }
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = Regex(
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$"
        )
        return passwordRegex.matches(password)
    }

    /*  private fun sendOTP() {
          if (isEmailType) {
              showProgressDialog(this, false)
              mViewModel!!.sendOTP(email!!, "", "")
          } else if (!isEmailType && isResendEnabled) {
              showProgressDialog(this, false)
              mViewModel!!.sendOTP("", phoneNo!!, countryCode!!)
          }
      }*/
}