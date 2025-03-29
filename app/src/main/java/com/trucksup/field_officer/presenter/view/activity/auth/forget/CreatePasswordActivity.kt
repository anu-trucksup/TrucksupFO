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
import com.trucksup.field_officer.databinding.ActivityCreatePasswordBinding
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.common.Utils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import com.trucksup.field_officer.presenter.view.activity.other.WelcomeLocationActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class CreatePasswordActivity : BaseActivity(), View.OnClickListener {
    var mBinding: ActivityCreatePasswordBinding? = null
    private var mViewModel: ForgetPasswordViewModel? = null
    private var email: String? = null
    private var phoneNo: String? = null
    private var countryCode: String? = null
    var isEmailType: Boolean = false
    var isResendEnabled: Boolean = false
    var isValidateQuestion: Boolean = false
    private var passWord: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_create_password)
        //click Listener
       // Utils.setStatusBarColorAndIcons(this)

        // mBinding.retryTextView.setOnClickListener(this);
        mViewModel = ViewModelProvider(this).get(
            ForgetPasswordViewModel::class.java
        )

        mBinding?.topView?.ivBack?.setOnClickListener(this)
        mBinding?.updatepassBtn?.setOnClickListener(this)
        setupObserver()
        isValidateQuestion = intent.extras!!.getBoolean("isValidateQuestion", false)
        email = intent.extras!!.getString("email", "")
        phoneNo = intent.extras!!.getString("phoneNo", "")
        countryCode = intent.extras!!.getString("countryCode", "")

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
        mViewModel!!.resultValidateAnsResetLD.observe(this@CreatePasswordActivity) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@CreatePasswordActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            }  else {
                // nothing required as internally reset password API called
            }
        }

        mViewModel!!.resultLoginLD.observe(this@CreatePasswordActivity) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@CreatePasswordActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()
                // start home screen
                val intent = Intent(this@CreatePasswordActivity,
                    LoginActivity::class.java
                )
               startActivity(intent)
                finish()
                showInAnimation()
            }
        }

        mViewModel!!.resultResetLD.observe(this@CreatePasswordActivity) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@CreatePasswordActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            }  else {
                //showDeleteProfileDialog(mViewModel.getLabel(AppConstant.AppLabelName.passwordUpdatedSuccessfully));
                dismissProgressDialog()
                Toast.makeText(this, "New Password created Successfully.", Toast.LENGTH_SHORT)
                    .show()
                mViewModel!!.loginUser(
                    email!!, passWord!!,
                    "password", ""
                )
                /* new Timer().schedule(new TimerTask(){
                public void run() {
                    startActivity(new Intent(CreatePasswordActivity.this, DashboardActivity.class));
                    finish();
                }
            }, 1000 );*/
            }
        }
        mViewModel!!.disableCounterLD.observe(
            this@CreatePasswordActivity
        ) { value: Int? -> }

        mViewModel!!.resultSendOTPLD.observe(this@CreatePasswordActivity) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@CreatePasswordActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else if (responseModel.success != null) {
                dismissProgressDialog()
                Toast.makeText(this, "OTP sent to your email id", Toast.LENGTH_SHORT).show()

                //                Toast.makeText(SignUpActivity.this,
//                        "OTP sent to your email id", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
//                finish();

                // mBinding.otpMessageText.setVisibility(View.VISIBLE);
                // mBinding.otpMessageText.setText("check Email For OTP");

                /* if (isEmailType) {
                showDeleteProfileDialog(mViewModel.getLabel(AppConstant.AppLabelName.otpSentToEmail));
                */
                /*Toast.makeText(CreatePasswordActivity.this,
                        mViewModel.getLabel(AppConstant.AppLabelName.otpSentToEmail)
                        , Toast.LENGTH_SHORT).show();*/
                /*
            } else {
                showDeleteProfileDialog(mViewModel.getLabel(AppConstant.AppLabelName.otpSentTomobile));
               */
                /* Toast.makeText(CreatePasswordActivity.this,
                        mViewModel.getLabel(AppConstant.AppLabelName.otpSentTomobile)
                        , Toast.LENGTH_SHORT).show();*/
                /*
            }*/
            }
        }
        // mBinding.retryTextView.setVisibility(View.INVISIBLE);
        mViewModel!!.resetTimer()
        mViewModel!!.startTimer()
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onClick(view: View) {
        if (view.id == R.id.iv_back) {
            onBackPressed()
        } else if (view.id == R.id.updatepass_btn) {

            if (TextUtils.isEmpty(mBinding?.passwordTxt?.text.toString().trim())) {
                mBinding?.passwordTxt?.setError("Please Enter Password")
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

            }
            else if (!p.matcher(password).matches()) {

                if (validateOTPFields()) {
                    if (validatePasswordFields()) {
                        showProgressDialog(this,false)
                        passWord = mBinding!!.confirmPasswordTxt.text.toString()
                        mViewModel!!.resetPassword(
                            email!!,
                            phoneNo!!,
                            countryCode!!,
                            mBinding!!.confirmPasswordTxt.text.toString(),
                            mBinding!!.otpTxt.text.toString(),
                            ""
                        )
                    }
                }
            }else{
                LoggerMessage.onSNACK(
                    mBinding!!.updatepassBtn,
                    "Enter a valid password format.",
                    applicationContext
                )
            }
        }
    }

    //null field otp field
    private fun validateOTPFields(): Boolean {
        if (!isValidateQuestion && mBinding!!.otpTxt.text.toString() != null && mBinding!!.otpTxt.text.toString().length > 0) {
            return true
        } else {
            mBinding!!.otpErrorText.visibility = View.VISIBLE
            mBinding!!.otpTxt.background = getDrawable(R.drawable.error_red_background_view)
            return false
        }
    }


    private fun validatePasswordFields(): Boolean {
        if (mBinding!!.confirmPasswordTxt.text.toString() != null && mBinding!!.confirmPasswordTxt.text.toString().length > 0 &&
            mBinding!!.confirmPasswordTxt.text.toString() == mBinding!!.passwordTxt.text.toString()
        ) {
            return true
        } else {
            mBinding!!.passwordTxt.background = getDrawable(R.drawable.error_red_background_view)
            mBinding!!.confirmPasswordTxt.background =
                getDrawable(R.drawable.error_red_background_view)
            //Utils.showDeleteProfileDialog(mViewModel.getLabel(AppConstant.AppLabelName.checkPassword));
            /* Toast.makeText(CreatePasswordActivity.this,
                    mViewModel.getLabel(AppConstant.AppLabelName.checkPassword), Toast.LENGTH_SHORT).show();*/
            return false
        }
    }

    private fun sendOTP() {
        if (isEmailType) {
            showProgressDialog(this,false)
            mViewModel!!.sendOTP(email!!, "", "")
        } else if (!isEmailType && isResendEnabled) {
            showProgressDialog(this,false)
            mViewModel!!.sendOTP("", phoneNo!!, countryCode!!)
        }
    }
}