package com.trucksup.field_officer.presenter.view.activity.auth.forget

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.otp.OtpRequest
import com.trucksup.field_officer.databinding.ActivityResetPasswordBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity(), View.OnClickListener {
    private var mBinding: ActivityResetPasswordBinding? = null
    private var mViewModel: ResetPasswordViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)

        //click Listener
        mBinding?.topView?.ivBack?.setOnClickListener(this)
        mBinding?.otpUpBtn?.setOnClickListener(this)
        mBinding?.tvVerify?.setOnClickListener(this)

        mViewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]

        setupObserver()

    }

    private fun setupObserver() {
        mViewModel?.resultSendOTPLD?.observe(this@ResetPasswordActivity) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@ResetPasswordActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else if (responseModel.success != null) {
                dismissProgressDialog()
                Toast.makeText(this, "OTP sent to your email id", Toast.LENGTH_SHORT).show()

                val intent = Intent(
                    this@ResetPasswordActivity,
                    CreatePasswordActivity::class.java
                )
                intent.putExtra("phoneNo", mBinding?.phoneNoTxt?.text.toString())
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
        }

    }

    override fun onClick(view: View) {
        if (view.id == R.id.iv_back) {
            onBackPressed()
        } else if (view.id == R.id.tv_verify) {
            val intent = Intent(this@ResetPasswordActivity, CreatePasswordActivity::class.java)
            intent.putExtra("isValidateQuestion", true)
            intent.putExtra("phoneNo", mBinding?.phoneNoTxt?.text.toString())
            intent.putExtra("countryCode", "+91")

            dismissProgressDialog()
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        } else if (view.id == R.id.otp_up_btn) {

            if (TextUtils.isEmpty(mBinding?.phoneNoTxt?.text.toString().trim())) {
                mBinding?.phoneNoTxt?.error = getString(R.string.error_mobile)
                mBinding?.phoneNoTxt?.requestFocus()
                return
            }

            if (isValidMobile(mBinding?.phoneNoTxt?.text.toString())) {

                mBinding?.otpUpBtn?.isEnabled = false
                mBinding?.otpUpBtn?.setBackgroundColor(Color.parseColor("#C2C2C2"))
                mBinding?.otpUpBtn?.setTextColor(Color.parseColor("#6A6A6A"))

                mBinding?.otpLayout?.visibility = View.VISIBLE

                //val ask: String = appSignatureHelper?.substring(1, 12).toString()

                val otpRequest = OtpRequest(
                    actionType = "I",
                    appSignatureKey = "",
                    networkId = packageName,
                    otp = 6555,
                    recipients = "91",
                    requestType = "TrucksUpOtp",
                    requestedBy = PreferenceManager.getPhoneNo(this),
                    unicode = false,
                    variables = "string",
                    stringOtp = PreferenceManager.getRequestNo(),
                    deviceId = PreferenceManager.getAndroiDeviceId(this)
                )
                mViewModel?.sendOTP("", otpRequest)

            } else {
                LoggerMessage.onSNACK(mBinding!!.phoneNoTxt, getString(R.string.enter_valid_mobile),
                    applicationContext
                )
            }
        }
    }
}