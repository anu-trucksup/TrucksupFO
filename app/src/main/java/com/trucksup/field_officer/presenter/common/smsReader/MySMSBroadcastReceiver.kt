package com.trucksup.field_officer.presenter.common.smsReader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import java.util.regex.Matcher
import java.util.regex.Pattern


class MySMSBroadcastReceiver : BroadcastReceiver() {
    private var otpReceiveListener: OTPReceiveListener? = null
    fun init(otpReceiveListener: OTPReceiveListener) {
        this.otpReceiveListener = otpReceiveListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            if (extras!![SmsRetriever.EXTRA_STATUS] != null) {
                val status = extras[SmsRetriever.EXTRA_STATUS] as Status?
                when (status!!.statusCode) {
                    CommonStatusCodes.SUCCESS -> {                   // Get SMS message contents
                        val message: String? = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                        LoggerMessage.LogErrorMsg("OTP Message", ">>> " + message)

                        if (message != null) {
                            val pattern: Pattern = Pattern.compile("(\\d{4})")
                            //   \d is for a digit
                            //   {} is the number of digits here 4.
                            val matcher: Matcher = pattern.matcher(message);
                            var mg: String = "";
                            if (matcher.find()) {
                                mg = matcher.group(0)?.toString() ?: ""  // 4 digit number
                                if (this.otpReceiveListener != null)
                                    this.otpReceiveListener!!.onOTPReceived(mg);
                            } else {
                                if (this.otpReceiveListener != null)
                                    this.otpReceiveListener!!.onOTPReceived(null)
                            }
                        }
                    }

                    CommonStatusCodes.TIMEOUT -> {}
                }
            }
        }
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
        fun onOTPTimeOut()
    }
}