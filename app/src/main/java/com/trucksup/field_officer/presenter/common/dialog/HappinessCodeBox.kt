package com.trucksup.field_officer.presenter.common.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.VerifyOtpDialogBinding
import com.trucksup.field_officer.presenter.utils.PrefsManager.getString
import java.util.concurrent.TimeUnit

class HappinessCodeBox(
    var context: Activity,
    private var headerMsg: String,
    private var subTittleMsg: String,
    private var resendButtonTxt: String,
) : Dialog(context) {
    private lateinit var binding: VerifyOtpDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = VerifyOtpDialogBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(binding.root)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(true)
        inst()
    }

    @SuppressLint("NewApi")
    private fun inst() {
        this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        binding.txtHeader.text = headerMsg
        binding.txtSubtittle.text = subTittleMsg
        binding.resendVerificationCodeTxt.text = resendButtonTxt


        binding.tvVerify.setOnClickListener {
            if (binding.pvHapinessCodeverify.text.toString().isEmpty()
                || binding.pvHapinessCodeverify.text?.length == 0) {

                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.setError(
                    getString(
                        R.string.EnterHappinessCode.toString(),
                        context
                    )
                )
                //Toast.makeText(this, "",Toast.LENGTH_SHORT).show()
            } else if (binding.pvHapinessCodeverify.text?.length!! > 6) {
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.setError("Wrong")
            } else {
                Toast.makeText(context, "Hi"+binding.pvHapinessCodeverify.text,Toast.LENGTH_SHORT).show()

                /*this.dismiss()
                context.finish()*/
            }
        }

        object : CountDownTimer(60000, 1000) {
            override fun onTick(msUntilFinished: Long) {
                // displayText.setText("remaining sec: " + msUntilFinished / 1000)
                var tm: Long = msUntilFinished % 1000
                binding.timecounter.text = String.format(
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
                binding.timecounter.visibility = View.GONE
                binding.txtHinttimecounter.visibility = View.GONE
                binding.resendVerificationCodeTxt.visibility = View.VISIBLE
            }
        }.start()
    }

}