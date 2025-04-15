package com.trucksup.field_officer.presenter.view.activity.auth.forget

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.data.model.otp.OtpRequest
import com.trucksup.field_officer.databinding.ActivityResetPasswordBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.JWTtoken
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.other.TokenViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity(), View.OnClickListener, JWTtoken {
    private var mBinding: ActivityResetPasswordBinding? = null
    private var mViewModel: ResetPasswordViewModel? = null
    private var mTokenViewModel: TokenViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)

        //click Listener
        mBinding?.topView?.ivBack?.setOnClickListener(this)
        mBinding?.otpUpBtn?.setOnClickListener(this)
        mBinding?.tvVerify?.setOnClickListener(this)

        mViewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]
        mTokenViewModel = ViewModelProvider(this)[TokenViewModel::class.java]

        setupObserver()

    }

    private fun setupObserver() {
        mViewModel?.resultSendOTPLD?.observe(this@ResetPasswordActivity) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(this@ResetPasswordActivity,
                    responseModel.serverError.toString(), "m")
                abx.show()
            } else if (responseModel.success != null) {
                dismissProgressDialog()
                Toast.makeText(this, "OTP sent to your Mobile no.", Toast.LENGTH_SHORT).show()
                mBinding?.otpUpBtn?.isEnabled = false
                mBinding?.otpUpBtn?.setBackgroundColor(Color.parseColor("#C2C2C2"))
                mBinding?.otpUpBtn?.setTextColor(Color.parseColor("#6A6A6A"))

                mBinding?.otpLayout?.visibility = View.VISIBLE

            }
        }
    }

    private fun generateToken() {
        LoadingUtils.showDialog(this, false)
       /* val request = GenerateJWTtokenRequest(
            apiSecreteKey = PreferenceManager.getAccessKey(this),
            password = PreferenceManager.getAccesPassword(this),
            userAgent = PreferenceManager.getAccesUserAgaint(this),
            username = PreferenceManager.getAccesUserName(this),
            issuer = PreferenceManager.getAccesUserInssur(this)
        )*/
        //{"Issuer":"TrucksupOtpIssuer","apiSecreteKey":"D4812E46-1399-4EA0-9917-GCDFEB40A8DF","password":"trucksupotp@321#","userAgent":"TrucksupOtpAudience","username":"TRUCKSUPOTP"}

        val request = GenerateJWTtokenRequest(
            apiSecreteKey = "D4812E46-1399-4EA0-9917-GCDFEB40A8DF",
            password = "trucksupotp@321#",
            userAgent = "TrucksupOtpAudience",
            username = "TRUCKSUPOTP",
            issuer = "TrucksupOtpIssuer"
        )
        mTokenViewModel?.generateJWToken(request, this, this)
        Log.e(" volley main url ", PreferenceManager.getServerUrl(this))

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
                generateToken()

            } else {
                LoggerMessage.onSNACK(mBinding!!.phoneNoTxt, getString(R.string.enter_valid_mobile),
                    applicationContext
                )
            }
        }
    }

    override fun onTokenSuccess(response: GenerateJWTtokenResponse) {

        //val ask: String = appSignatureHelper?.substring(1, 12).toString()
        val random = Random()
        val otpTemp = String.format("%04d", random.nextInt(9999))
        val otpRequest = OtpRequest(
            actionType = "I", appSignatureKey = "VxEIOjjCUCZ", networkId = packageName,
            otp = 6655,
            recipients = PreferenceManager.getPhoneNo(this),
            requestType = "TrucksUpOtp",
            requestedBy = PreferenceManager.getPhoneNo(this),
            unicode = false,
            variables = "string",
            stringOtp = otpTemp,
            deviceId = PreferenceManager.getAndroiDeviceId(this)
        )
        mViewModel?.sendOTP("Basic "+response.accessToken, otpRequest)
    }

    override fun onTokenFailure(msg: String) {
      dismissProgressDialog()
    }
}