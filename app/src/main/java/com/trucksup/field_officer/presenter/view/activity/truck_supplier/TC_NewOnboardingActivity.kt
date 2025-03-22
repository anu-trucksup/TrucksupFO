package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityTcNewOnboardingBinding
import com.trucksup.field_officer.databinding.VerifyOtpDialogBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity

class TC_NewOnboardingActivity : BaseActivity() {
    private lateinit var binding: ActivityTcNewOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTcNewOnboardingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
        //setContentView(R.layout.activity_banew_onboarding)

        binding.btnAdd.setOnClickListener(){
            CheckValidation()
        }
    }

    fun CheckValidation() {
        if (binding.ETAccountHolderName.text.isEmpty()) {
            binding.ETAccountHolderName.requestFocus()
            binding.ETAccountHolderName.setError(getString(R.string.PleaseEnterContactName))
        } else if (binding.ETAccountHolderNumber.text.isEmpty()) {
            binding.ETAccountHolderNumber.requestFocus()
            binding.ETAccountHolderNumber.setError(getString(R.string.PleaseEnterContactNumber))
        } else if (binding.ETBusinessName.text.isEmpty()) {
            binding.ETBusinessName.requestFocus()
            binding.ETBusinessName.setError(getString(R.string.PleaseEnterBusinessName))
        } else if (binding.ETBusinessAddress.text.isEmpty()) {
            binding.ETBusinessAddress.requestFocus()
            binding.ETBusinessAddress.setError(getString(R.string.PleaseEnterBusinessAddress))
        } else if (binding.ETPincode.text.isEmpty()) {
            binding.ETPincode.requestFocus()
            binding.ETPincode.setError(getString(R.string.PleaseEnterBusinessPincode))
        }else{
            setSubmitDialog()
        }
        /*else if (binding.ETPanNumberNOB.length() == 10) {
            val s = binding.ETPanNumberNOB.toString() // get your editext value here
            val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
            val matcher: Matcher = pattern.matcher(s)
            // Check if pattern matches
            if (matcher.matches()) {
                panNumber = binding.ETPanNumberNOB.toString()
            } else {
                //LoggerMessage.tostPrint("Enter Right PAN No", this)
                binding.ETPanNumberNOB?.setError("Enter Right PAN Noss")
                // pan_no?.setText("")
            }
        } else {
            binding.ETPanNumberNOB.requestFocus()
            *//*val customErrorDrawable = resources.getDrawable(com.trucksup.fieldofficer.R.drawable.ic_phone)
            customErrorDrawable.setBounds(
                0,
                0,
                customErrorDrawable.intrinsicWidth,
                customErrorDrawable.intrinsicHeight
            )
            binding.ETPanNumberNOB?.setError("Enter Right PAN No" , customErrorDrawable)*//*
            binding.ETPanNumberNOB?.setError("Enter Right PAN No")
            binding.ETPanNumberNOB?.setText("")
        }*/

    }

    private fun setSubmitDialog() {
        val builder = AlertDialog.Builder(this)
        val binding = VerifyOtpDialogBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.tvVerify.setOnClickListener{
            if(binding.pvHapinessCodeverify.text?.isEmpty() == true){
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.setError(getString(R.string.EnterHappinessCode))
                //Toast.makeText(this, "",Toast.LENGTH_SHORT).show()
            }else if(binding.pvHapinessCodeverify.text?.length!! > 6){
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.setError("Wrong")
            }else{
                dialog.dismiss()
            }
        }

        /*Glide.with(this)
            .load(R.drawable.ab_apni_chalao_image)
            .into(binding.img)
        binding.msg.text=respnse.message
        binding.message1.text=respnse.message1

        //ok button
        binding.btnOk.setOnClickListener {
            dialog.dismiss()
            finish()
        }*/

        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}