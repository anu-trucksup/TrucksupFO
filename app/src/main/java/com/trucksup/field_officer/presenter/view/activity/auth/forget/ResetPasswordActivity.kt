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
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity(), View.OnClickListener, JWTtoken {
    private val randomOTP: String = ""
    var isTimer: Boolean = false
    private var mBinding: ActivityResetPasswordBinding? = null
    private var mViewModel: ResetPasswordViewModel? = null
    private var mTokenViewModel: TokenViewModel? = null
    private var otpTempCode: String? = ""
    private lateinit var smsClient: SmsRetrieverClient
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)

        //click Listener
        mBinding?.topView?.ivBack?.setOnClickListener(this)
        mBinding?.otpUpBtn?.setOnClickListener(this)
        mBinding?.tvVerify?.setOnClickListener(this)

        mViewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]
        mTokenViewModel = ViewModelProvider(this)[TokenViewModel::class.java]

        if (!PreferenceManager.getPhoneNo(this).isNullOrEmpty()) {
            mBinding?.phoneNoTxt?.setText(""+PreferenceManager.getPhoneNo(this))
        }

        smsClient = SmsRetriever.getClient(this)

        mBinding?.resendVerificationCodeTxt?.setOnClickListener {
            if (isTimer == false) {
                mBinding?.otpPinview?.setText("")
                setTimer()
                //generateToken()

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
                otpTempCode = randomOTP
                Toast.makeText(this, "OTP sent to your Mobile no.", Toast.LENGTH_SHORT).show()
                mBinding?.otpUpBtn?.isEnabled = false
                mBinding?.otpUpBtn?.setBackgroundColor(Color.parseColor("#C2C2C2"))
                mBinding?.otpUpBtn?.setTextColor(Color.parseColor("#6A6A6A"))

                mBinding?.otpLayout?.visibility = View.VISIBLE

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
                    otpTempCode = otp
                    LoggerMessage.LogErrorMsg("One time code", "=======" + otpTempCode)
                    // val oneTimeCode = parseOneTimeCode(message) // define this function
                    mBinding?.otpPinview?.setText(otpTempCode.toString())

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

                val intent = Intent(this@ResetPasswordActivity, CreatePasswordActivity::class.java)
                intent.putExtra("phoneNo", mBinding?.phoneNoTxt?.text.toString())
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            } else {
                LoggerMessage.onSNACK(
                    mBinding!!.phoneNoTxt, getString(R.string.mobile_no_validation),
                    applicationContext
                )
            }
        }
    }

    private fun verifyOTP() {
        if (otpTempCode == mBinding?.otpPinview?.text.toString() || mBinding?.otpPinview?.text.toString()
                .trim() == PreferenceManager.getOtp(this).toString().trim()
        ) {


        } else {
            mBinding?.otpPinview?.error = "Please Enter OTP"
            mBinding?.otpPinview?.visibility = View.VISIBLE
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
        mViewModel?.sendOTP("Basic " + response.accessToken, otpRequest)
    }

    override fun onTokenFailure(msg: String) {
        dismissProgressDialog()
    }
}