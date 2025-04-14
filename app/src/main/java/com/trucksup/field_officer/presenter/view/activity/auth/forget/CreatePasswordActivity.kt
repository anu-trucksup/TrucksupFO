package com.trucksup.field_officer.presenter.view.activity.auth.forget

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.authModel.ForgetRequest
import com.trucksup.field_officer.data.model.authModel.LoginRequest
import com.trucksup.field_officer.databinding.ActivityCreatePasswordBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
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
    private var isResendEnabled: Boolean = false
    private var passWord: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_password)
        //click Listener

        mViewModel = ViewModelProvider(this)[CreatePasswordViewModel::class.java]

        mBinding?.topView?.ivBack?.setOnClickListener(this)
        mBinding?.updatepassBtn?.setOnClickListener(this)
        setupObserver()
        mobileNumber = intent.extras!!.getString("phoneNo", "")


        /* mBinding!!.confirmPasswordTxt.addTextChangedListener(object : TextWatcher {
             override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
             }

             override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
             }

             override fun afterTextChanged(editable: Editable) {
                 val password: String = mBinding!!.passwordTxt.getText().toString()
                 if (editable.length > 0 && password.length > 0) {
                     if (!editable.toString().equals(password)) {
                         val customErrorDrawable = resources.getDrawable(R.drawable.error_warn)
                         customErrorDrawable.setBounds(
                             0,
                             0,
                             customErrorDrawable.intrinsicWidth,
                             customErrorDrawable.intrinsicHeight
                         )

                         mBinding?.confirmPasswordTxt?.setError(
                             "Password and Confirm Password should be same.",
                             customErrorDrawable
                         )

                         // give an error that password and confirm password not match
                     } else {
                         val customErrorDrawable = resources.getDrawable(R.drawable.error_confirm)
                         customErrorDrawable.setBounds(
                             0,
                             0,
                             customErrorDrawable.intrinsicWidth,
                             customErrorDrawable.intrinsicHeight
                         )
                         mBinding?.confirmPasswordTxt?.setError("", customErrorDrawable)
                     }
                 }
             }
         })*/
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
                Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_SHORT).show()
                // start home screen
                val intent = Intent(this@CreatePasswordActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onClick(view: View) {
        if (view.id == R.id.iv_back) {
            onBackPressed()
        } else if (view.id == R.id.updatepass_btn) {
            startActivity(Intent(this, WelcomeLocationActivity::class.java))
            if (TextUtils.isEmpty(mBinding?.passwordTxt?.text.toString().trim())) {
                mBinding?.passwordTxt?.error = "Please Enter Password"
                mBinding?.passwordTxt?.requestFocus()
                return
            }

            val password = mBinding!!.passwordTxt.text.toString()
            val confirmpassword = mBinding!!.confirmPasswordTxt.text.toString()
            // regex
            val regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$"
            val p = Pattern.compile(regex)

            if (confirmpassword.length > 0 && password.length > 0) {
                if (!confirmpassword.equals(password)) {
                    val customErrorDrawable = resources.getDrawable(R.drawable.error_warn)
                    customErrorDrawable.setBounds(
                        0,
                        0,
                        customErrorDrawable.intrinsicWidth,
                        customErrorDrawable.intrinsicHeight
                    )

                    mBinding?.confirmPasswordTxt?.setError(
                        "Password and Confirm Password should be same.",
                        customErrorDrawable
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
                        "Both Password are same.",
                        customErrorDrawable
                    )


                    startActivity(Intent(this, WelcomeLocationActivity::class.java))
                    /* // Set drawables for left, top, right, and bottom - send 0 for nothing
                     mBinding!!.confirmPasswordTxt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.locked,
                         0, R.drawable.error_confirm, 0 )

                     LoggerMessage.onSNACK(
                         mBinding!!.updatepassBtn,
                         "Password match.",
                         applicationContext
                     )*/

                    //mSignUpBinding!!.confirmPasswordTxt.setdrawa("", customErrorDrawable)

                    //return
                }

            } else if (!p.matcher(password).matches()) {

                    showProgressDialog(this, false)
                    passWord = mBinding!!.confirmPasswordTxt.text.toString()

                    val request = ForgetRequest(
                        requestedBy = PreferenceManager.getPhoneNo(this),
                        requestId = PreferenceManager.getRequestNo().toInt(),
                        requestDatetime = PreferenceManager.getServerDateUtc(),
                        deviceid = PreferenceManager.getAndroiDeviceId(this),
                        appVersion = "",
                        androidVersion = "",
                        profilename = PreferenceManager.getUserName(this),
                        profilephoto = "",
                        mobilenumber = mobileNumber.toString(),
                        password = mBinding?.passwordTxt?.text.toString()
                    )
                    mViewModel!!.resetPassword(
                        PreferenceManager.getAuthToken(), request
                    )

            } else {
                LoggerMessage.onSNACK(
                    mBinding!!.updatepassBtn,
                    "Enter a valid password format.",
                    applicationContext
                )
            }
        }
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