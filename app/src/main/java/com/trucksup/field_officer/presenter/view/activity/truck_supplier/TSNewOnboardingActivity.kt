package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityTcNewOnboardingBinding
import com.trucksup.field_officer.databinding.VerifyOtpDialogBinding
import com.trucksup.field_officer.presenter.common.dialog.FinaceSubmitBox
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import java.util.concurrent.TimeUnit

class TSNewOnboardingActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityTcNewOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTcNewOnboardingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(binding.root)
        //setContentView(R.layout.activity_banew_onboarding)

        setOnClicks()
    }

    private fun setOnClicks() {
        binding.btnAdd.setOnClickListener(this)
        binding.btnCancel.setOnClickListener(this)
        binding.TCBackBtn.IBbackBtn.setOnClickListener(this)
    }

    private fun CheckValidation() {
        if (binding.ETAccountHolderName.text.isEmpty()) {
            binding.ETAccountHolderName.requestFocus()
            binding.ETAccountHolderName.setError(getString(R.string.PleaseEnterContactName))
        } else if (binding.ETAccountHolderNumber.text.isEmpty()) {
            binding.ETAccountHolderNumber.requestFocus()
            binding.ETAccountHolderNumber.setError(getString(R.string.PleaseEnterContactNumber))
        } else if (binding.ETAccountHolderNumber.text.length < 10) {
            binding.ETAccountHolderNumber.requestFocus()
            binding.ETAccountHolderNumber.setError(getString(R.string.enter_right_number_v))
        } else if (binding.ETBusinessName.text.isEmpty()) {
            binding.ETBusinessName.requestFocus()
            binding.ETBusinessName.setError(getString(R.string.PleaseEnterBusinessName))
        } else if (binding.ETBusinessAddress.text.isEmpty()) {
            binding.ETBusinessAddress.requestFocus()
            binding.ETBusinessAddress.setError(getString(R.string.PleaseEnterBusinessAddress))
        } else if (binding.ETPincode.text.isEmpty()) {
            binding.ETPincode.requestFocus()
            binding.ETPincode.setError(getString(R.string.PleaseEnterBusinessPincode))
        } else if (binding.ETPincode.text.length < 6) {
            binding.ETPincode.requestFocus()
            binding.ETPincode.setError(getString(R.string.PleaseEnterRightPincode))
        } else {
            val HappinessCodeBox = HappinessCodeBox(this, getString(R.string.hapinessCodeMsg),
                getString(R.string.EnterHappinessCode),
                getString(R.string.resand_sms))
            HappinessCodeBox.show()
        }
    }

    private fun setSubmitDialog() {
        val builder = AlertDialog.Builder(this)
        val binding = VerifyOtpDialogBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.tvVerify.setOnClickListener {
            if (binding.pvHapinessCodeverify.text?.isEmpty() == true) {
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.setError(getString(R.string.EnterHappinessCode))
                //Toast.makeText(this, "",Toast.LENGTH_SHORT).show()
            } else if (binding.pvHapinessCodeverify.text?.length!! > 6) {
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.setError("Wrong")
            } else {
                dialog.dismiss()
            }
        }

        //  timer_progress?.setProgressWithAnimation(65f, 1000)
        object : CountDownTimer(60000, 1000) {
            override fun onTick(msUntilFinished: Long) {
                // displayText.setText("remaining sec: " + msUntilFinished / 1000)
                var tm: Long = msUntilFinished % 1000
                binding.timecounter?.text = String.format(
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
                //goback = true
                binding.timecounter?.visibility = View.GONE
                binding.txtHinttimecounter?.visibility = View.GONE
                binding.resendVerificationCodeTxt?.visibility = View.VISIBLE
                //cancle?.visibility = View.VISIBLE
            }
        }.start()

        dialog.show()
    }


    override fun onClick(v: View?) {
        val item_id = v?.id
        when (item_id) {
            R.id.btnAdd -> CheckValidation()
            R.id.IBbackBtn -> onBackPressed()
            R.id.btnCancel -> onBackPressed()
        }
    }
}