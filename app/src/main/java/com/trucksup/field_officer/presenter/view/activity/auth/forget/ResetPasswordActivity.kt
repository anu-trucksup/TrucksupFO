package com.trucksup.field_officer.presenter.view.activity.auth.forget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.auth.api.phone.SmsRetrieverClient
import com.google.android.gms.tasks.Task
import com.trucksup.field_officer.presenter.common.smsReader.MySMSBroadcastReceiver
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.data.model.otp.OtpRequest
import com.trucksup.field_officer.data.model.otp.VerifyOtpRequest
import com.trucksup.field_officer.databinding.ActivityResetPasswordBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.AppVersionUtils
import com.trucksup.field_officer.presenter.common.JWTtoken
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.other.TokenViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity(), View.OnClickListener {

    var isTimer: Boolean = false
    private var mBinding: ActivityResetPasswordBinding? = null
    private var mViewModel: ResetPasswordViewModel? = null
    private var mTokenViewModel: TokenViewModel? = null
    private var otpTempCode: String? = ""
    private lateinit var smsClient: SmsRetrieverClient
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(mBinding?.root)

        //click Listener
        mBinding?.topView?.ivBack?.setOnClickListener(this)
        mBinding?.otpUpBtn?.setOnClickListener(this)
        mBinding?.tvVerify?.setOnClickListener(this)

        mViewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]
        mTokenViewModel = ViewModelProvider(this)[TokenViewModel::class.java]

        /* if (!PreferenceManager.getPhoneNo(this).isNullOrEmpty()) {
             mBinding?.phoneNoTxt?.setText("" + PreferenceManager.getPhoneNo(this))
         }*/
        mBinding?.phoneNoTxt?.setText("" + PreferenceManager.getPhoneNo(this))

        smsClient = SmsRetriever.getClient(this)

        mBinding?.resendVerificationCodeTxt?.setOnClickListener {
            if (isTimer == false) {
                mBinding?.otpPinview?.setText("")
                setTimer()
                if (isValidMobile(mBinding?.phoneNoTxt?.text.toString())) {
                    // generateToken()
                   showProgressDialog(this,false)
                    val otpRequest = OtpRequest(
                        actionType = "I", appSignatureKey = "SFDSYT", appPkgName = "com.logistics.trucksup",
                        recipients = mBinding?.phoneNoTxt?.text.toString(),
                        requestType = "OTP",
                        requestedBy = mBinding?.phoneNoTxt?.text.toString(),
                        unicode = true,
                        variables = "string",
                        deviceId = PreferenceManager.getAndroiDeviceId(this),
                        mobileNumber = mBinding?.phoneNoTxt?.text.toString(),
                        appVersion = AppVersionUtils.getAppVersionName(this),
                        oStype = "Android",
                        useFor = "Generate",
                        modelName = Build.BRAND
                    )
                    mViewModel?.sendOTP(PreferenceManager.getAuthToken(), otpRequest)


                } else {
                    LoggerMessage.onSNACK(
                        mBinding!!.phoneNoTxt, getString(R.string.mobile_no_validation),
                        applicationContext
                    )
                }

            }
        }
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


                if (responseModel.success.statusCode == 200) {
                    Toast.makeText(this, "OTP sent to your Mobile no.", Toast.LENGTH_SHORT).show()
                    mBinding?.otpUpBtn?.isEnabled = false
                    mBinding?.phoneNoTxt?.isClickable = false
                    mBinding?.phoneNoTxt?.isFocusable = false
                    setTimer()
                    mBinding?.otpUpBtn?.setBackgroundColor(Color.parseColor("#C2C2C2"))
                    mBinding?.otpUpBtn?.setTextColor(Color.parseColor("#6A6A6A"))

                    mBinding?.otpLayout?.visibility = View.VISIBLE
                    mBinding?.otpMessage?.text = ""+responseModel.success.message
                    otpTempCode = responseModel.success.otP_ID
                } else {
                    val abx = AlertBoxDialog(
                        this@ResetPasswordActivity,
                        responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }


            }
        }

        mViewModel?.resultVerifyOTPLD?.observe(this@ResetPasswordActivity) { responseModel ->
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
                if (responseModel.success.statusCode == 200) {
                    Toast.makeText(this, "OTP Verified.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ResetPasswordActivity, CreatePasswordActivity::class.java)
                    intent.putExtra("phoneNo", mBinding?.phoneNoTxt?.text.toString())
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }else {
                    val abx = AlertBoxDialog(
                        this@ResetPasswordActivity,
                        responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }


            }
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun autoOtp() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val task: Task<Void> = smsClient.startSmsRetriever()
            task.addOnSuccessListener { // Successfully started retriever, expect broadcast intent
            }

            task.addOnFailureListener { // Failed to start retriever, inspect Exception for more details
            }

            val mySMSBroadcastReceiver = MySMSBroadcastReceiver()
            registerReceiver(
                mySMSBroadcastReceiver,
                IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
            )

            mySMSBroadcastReceiver.init(object : MySMSBroadcastReceiver.OTPReceiveListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onOTPReceived(otp: String?) {
                    LoggerMessage.LogErrorMsg("One time code", "=======" + otp)
                    // val oneTimeCode = parseOneTimeCode(message) // define this function
                    mBinding?.otpPinview?.setText(otp.toString())

                    android.os.Handler().postDelayed(
                        Runnable {
                            hideKeyboard()
                        },
                        250
                    )

                }

                override fun onOTPTimeOut() {}
            })
        }

    }

    @SuppressLint("ResourceAsColor")
    fun setTimer() {

        mBinding?.resendVerificationCodeTxt?.setTextColor(resources.getColor(R.color.disable_text_color))
        if (isTimer) {
            countDownTimer?.cancel()
        }
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(msUntilFinished: Long) {
                isTimer = true
                // displayText.setText("remaining sec: " + msUntilFinished / 1000)
                var tm: Long = msUntilFinished % 1000
                Log.e("Timer", "=====" + tm)
                mBinding?.timecounter?.text = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(msUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(msUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    msUntilFinished
                                )
                            )
                )

            }


            override fun onFinish() {
                isTimer = false
                mBinding?.resendVerificationCodeTxt?.setTextColor(resources.getColor(R.color.blue))

            }
        }.start()
    }

    fun hideKeyboard() {
        if (isKeyboardOpened()) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } else {
            Log.e("Keyboard", "Not open")
        }
    }

    private fun Activity.isKeyboardOpened(): Boolean {
        val r = Rect()
        val activityRoot = getActivityRoot()
        val visibleThreshold = dip(100)

        activityRoot.getWindowVisibleDisplayFrame(r)
        val heightDiff = activityRoot.rootView.height - r.height()

        return heightDiff > visibleThreshold;
    }

    private fun Activity.getActivityRoot(): View {
        return (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0);
    }

    private fun dip(value: Int): Int {
        return (value * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun generateToken() {
        LoadingUtils.showDialog(this, false)
        val request = GenerateJWTtokenRequest(
            apiSecreteKey = "D4812E46-1399-4EA0-9917-GCDFEB40A8DF",
            password = "trucksupotp@321#",
            userAgent = "TrucksupOtpAudience",
            username = "TRUCKSUPOTP",
            issuer = "TrucksupOtpIssuer"
        )
        // mTokenViewModel?.generateJWToken(request, this, this)

    }

    override fun onClick(view: View) {
        if (view.id == R.id.iv_back) {
            onBackPressed()
        } else if (view.id == R.id.tv_verify) {

            if (TextUtils.isEmpty(mBinding?.otpPinview?.text.toString().trim())) {
                mBinding?.otpPinview?.error = "Please Enter OTP."
                mBinding?.otpPinview?.visibility = View.VISIBLE
            } else {
                verifyOTP()
            }
            /*val intent = Intent(this@ResetPasswordActivity, CreatePasswordActivity::class.java)
            intent.putExtra("isValidateQuestion", true)
            intent.putExtra("phoneNo", mBinding?.phoneNoTxt?.text.toString())
            intent.putExtra("countryCode", "+91")

            dismissProgressDialog()
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)*/
        } else if (view.id == R.id.otp_up_btn) {

            if (TextUtils.isEmpty(mBinding?.phoneNoTxt?.text.toString().trim())) {
                mBinding?.phoneNoTxt?.error = getString(R.string.error_mobile)
                mBinding?.phoneNoTxt?.requestFocus()
                return
            }

            if (isValidMobile(mBinding?.phoneNoTxt?.text.toString())) {
                // generateToken()
               showProgressDialog(this,false)
                val otpRequest = OtpRequest(
                    actionType = "I", appSignatureKey = "SFDSYT", appPkgName = "com.logistics.trucksup",
                    recipients = mBinding?.phoneNoTxt?.text.toString(),
                    requestType = "OTP",
                    requestedBy = mBinding?.phoneNoTxt?.text.toString(),
                    unicode = true,
                    variables = "string",
                    deviceId = PreferenceManager.getAndroiDeviceId(this),
                    mobileNumber = mBinding?.phoneNoTxt?.text.toString(),
                    appVersion = AppVersionUtils.getAppVersionName(this),
                    oStype = "Android",
                    useFor = "Generate",
                    modelName = Build.BRAND
                )
                mViewModel?.sendOTP(PreferenceManager.getAuthToken(), otpRequest)


            } else {
                LoggerMessage.onSNACK(
                    mBinding!!.phoneNoTxt, getString(R.string.mobile_no_validation),
                    applicationContext
                )
            }
        }
    }

    private fun verifyOTP() {
        showProgressDialog(this,false)
        val request = VerifyOtpRequest(
            otp = mBinding?.otpPinview?.text.toString(),
            mobileNumber = mBinding?.phoneNoTxt?.text.toString(),
            appVersion = AppVersionUtils.getAppVersionName(this),
            oStype = "Android",
            deviceId = PreferenceManager.getAndroiDeviceId(this),
            appSignatureKey = "SFDSYT",
            appPkgName = "com.logistics.trucksup",
            actionType = "I",
            requestType = "OTP",
            requestedBy = mBinding?.phoneNoTxt?.text.toString(),
            useFor = "Verify",
            modelName = Build.BRAND,
            userIP = "192.9800.988",
            userMacAddress = "wdefs",
            deviceName = Build.DEVICE,
            imeiNumber1 = "asdfgdsa",
            imeiNumber2 = "sadfgh",
            otP_ID = otpTempCode.toString(),
            recipients = mBinding?.phoneNoTxt?.text.toString()
        )

        mViewModel?.verifyOTP(PreferenceManager.getAuthToken(), request)

    }

}