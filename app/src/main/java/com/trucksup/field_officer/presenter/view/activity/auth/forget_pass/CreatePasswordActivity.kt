package com.trucksup.field_officer.presenter.view.activity.auth.forget_pass

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityCreatePasswordBinding
import com.trucksup.field_officer.presenter.common.Utils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.other.DashboardActivity
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
        Utils.setStatusBarColorAndIcons(this)
        mBinding?.topView?.ivBack?.setOnClickListener(this)
        mBinding?.updatepassBtn?.setOnClickListener(this)

        // mBinding.retryTextView.setOnClickListener(this);
        mViewModel = ViewModelProvider(this).get(
            ForgetPasswordViewModel::class.java
        )

        setupObserver()
        isValidateQuestion = intent.extras!!.getBoolean("isValidateQuestion", false)
        email = intent.extras!!.getString("email", "")
        phoneNo = intent.extras!!.getString("phoneNo", "")
        countryCode = intent.extras!!.getString("countryCode", "")

        mBinding!!.confirmPasswordTxt.addTextChangedListener(object : TextWatcher {
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
        })
    }


    private fun setupObserver() {
        mViewModel!!.resultValidateAnsResetLD.observe(this@CreatePasswordActivity) { responseModel ->
            if (responseModel.networkError != null) {
                dismissProgressDialog()
                // showDeleteProfileDialog(mViewModel.getLabel(AppConstant.AppLabelName.networkError));
                /* Toast.makeText(CreatePasswordActivity.this,
                    mViewModel.getLabel(AppConstant.AppLabelName.networkError), Toast.LENGTH_SHORT).show();*/
            } else if (responseModel.serverResponseError != null) {
                dismissProgressDialog()
                // showDeleteProfileDialog(mViewModel.getLabel(responseModel.getServerResponseError()));
            } else if (responseModel.genericError != null) {
                dismissProgressDialog()

                //sshowDeleteProfileDialog(mViewModel.getLabel(AppConstant.AppLabelName.networkError));
            } else {
                // nothing required as internally reset password API called
            }
        }

        mViewModel!!.resultLoginLD.observe(this@CreatePasswordActivity) { responseModel ->
            if (responseModel.networkError != null) {
                dismissProgressDialog()
                Utils.showToastDialog(
                    "Network Error",
                    this, "Ok"
                )
            } else if (responseModel.serverResponseError != null) {
                dismissProgressDialog()
            } else if (responseModel.genericError != null) {
                dismissProgressDialog()
            } else {
                dismissProgressDialog()
                // start home screen
                // move to home screen
                //  Log.d("MYTAG", "bearear token ==> " + responseModel.getSuccess().getPayload().getAccess_token());
                val intent = Intent(
                    this@CreatePasswordActivity,
                    DashboardActivity::class.java
                )
                intent.putExtra("airportId", 0)
                //                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent)
                setResult(RESULT_OK, getIntent())
                finish()
                showInAnimation()
            }
        }

        mViewModel!!.resultResetLD.observe(this@CreatePasswordActivity) { responseModel ->
            if (responseModel.networkError != null) {
                dismissProgressDialog()
                // showDeleteProfileDialog(mViewModel.getLabel(AppConstant.AppLabelName.wrongsecret));
                /* Toast.makeText(CreatePasswordActivity.this,
                    mViewModel.getLabel(AppConstant.AppLabelName.networkError), Toast.LENGTH_SHORT).show();*/
            } else if (responseModel.serverResponseError != null) {
                dismissProgressDialog()
                if (responseModel.serverResponseError == "in.co.ksquaretech.backend.current.password.not.match") {
                    Utils.showToastDialog(
                        "Invalid Otp.",
                        this, "Ok"
                    )
                } else {
                    Utils.showToastDialog(
                        responseModel.serverResponseError,
                        this, "Ok"
                    )
                }

            } else if (responseModel.genericError != null) {
                dismissProgressDialog()

            } else {
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
            if (responseModel.networkError != null) {
                dismissProgressDialog()
                //showDeleteProfileDialog(mViewModel.getLabel(AppConstant.AppLabelName.networkError));
                /*Toast.makeText(CreatePasswordActivity.this,
                    mViewModel.getLabel(AppConstant.AppLabelName.networkError), Toast.LENGTH_SHORT).show();*/
            } else if (responseModel.serverResponseError != null) {
                dismissProgressDialog()
                //showDeleteProfileDialog(mViewModel.getLabel(responseModel.getServerResponseError()));
                /* Toast.makeText(CreatePasswordActivity.this,
                    mViewModel.getLabel(responseModel.getServerResponseError()), Toast.LENGTH_SHORT).show();*/
            } else if (responseModel.genericError != null) {
                dismissProgressDialog()
                // showDeleteProfileDialog(mViewModel.getLabel(AppConstant.AppLabelName.genericError));
                /*Toast.makeText(CreatePasswordActivity.this,
                    mViewModel.getLabel(AppConstant.AppLabelName.genericError), Toast.LENGTH_SHORT).show();*/
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


    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(
            this@CreatePasswordActivity,
            ResetPasswordActivity::class.java
        )
        startActivity(intent)
        //  setResult(Activity.RESULT_OK, getIntent());
        finish()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onClick(view: View) {
        if (view.id == R.id.iv_back) {
            onBackPressed()
        } else if (view.id == R.id.updatepass_btn) {
           startActivity(Intent(this, WelcomeLocationActivity::class.java))

            val passwordString = mBinding!!.passwordTxt.text.toString()
            val otp_txt = mBinding!!.otpTxt.text.toString()
            // mBinding!!.invalidPassword.visibility = View.GONE
            // regex
            val regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$"
            val p = Pattern.compile(regex)
            if (passwordString != mBinding!!.confirmPasswordTxt.text.toString()) {
                // password not matching
                /* mBinding!!.invalidPassword.text = "passwoord Not Matching Error"
                 mBinding!!.invalidPassword.visibility = View.VISIBLE*/
                mBinding!!.confirmPasswordTxt.background =
                    getDrawable(R.drawable.error_red_background_view)
                mBinding!!.confirmPasswordTxt.background =
                    getDrawable(R.drawable.error_red_background_view)
                return
            } else if (passwordString.isEmpty()) {
                // password should not be blank
                /* mBinding!!.invalidPassword.text = "passwoord Empty Error"
                 mBinding!!.invalidPassword.visibility = View.VISIBLE
                 mBinding!!.answerErrorText.visibility = View.VISIBLE
                 mBinding!!.question.background = getDrawable(R.drawable.error_red_background_view)*/
                mBinding!!.otpTxt.background = getDrawable(R.drawable.error_red_background_view)
                mBinding!!.otpErrorText.visibility = View.VISIBLE
                mBinding!!.passwordTxt.background =
                    getDrawable(R.drawable.error_red_background_view)
                mBinding!!.confirmPasswordTxt.background =
                    getDrawable(R.drawable.error_red_background_view)
                return
            } else if (otp_txt.isEmpty()) {
                // otp_txt should not be blank
                mBinding!!.otpTxt.background = getDrawable(R.drawable.error_red_background_view)
                mBinding!!.otpErrorText.visibility = View.VISIBLE

                return
            } else if (!p.matcher(passwordString).matches()) {
                // invalid
                /* mBinding!!.invalidPassword.text = "passwoordValidationError"
                 mBinding!!.invalidPassword.visibility = View.VISIBLE*/
                mBinding!!.passwordTxt.background =
                    getDrawable(R.drawable.error_red_background_view)
                mBinding!!.confirmPasswordTxt.background =
                    getDrawable(R.drawable.error_red_background_view)
                return
            }
            mBinding!!.otpErrorText.visibility = View.GONE
            mBinding!!.otpTxt.background = getDrawable(R.drawable.rounded_corner_edit_view)

            mBinding!!.passwordTxt.background = getDrawable(R.drawable.rounded_corner_edit_view)
            mBinding!!.confirmPasswordTxt.background =
                getDrawable(R.drawable.rounded_corner_edit_view)
            if (validateOTPFields()) {
                if (validatePasswordFields()) {
                    showProgressDialog()
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
        } else if (view.id == R.id.retry_text_view) {
            if (mViewModel!!.disableCounterLD.value!! <= 0) {
                sendOTP()
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
            showProgressDialog()
            mViewModel!!.sendOTP(email!!, "", "")
        } else if (!isEmailType && isResendEnabled) {
            showProgressDialog()
            mViewModel!!.sendOTP("", phoneNo!!, countryCode!!)
        }
    }
}