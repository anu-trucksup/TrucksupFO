package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGpOnboardingPreviewBinding
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.GPOnboardingData

class GpOnboardingPreviewActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGpOnboardingPreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gp_onboarding_preview)
        val view = binding.root
        setContentView(view)


        //test
        val gson = Gson()
        val json = intent.getStringExtra("userDataJson")
        val gpOnboardingData = gson.fromJson(json, GPOnboardingData::class.java)

        //GP Basic Detail
        binding.tvContactName.setText(gpOnboardingData.ContactName)
        binding.tvContactNumber.setText(gpOnboardingData.ContactNumber)
        binding.tvPartnerType.setText(gpOnboardingData.PartnerType)

        //GP Personal Detail
        binding.tvSalesCodeofBO.setText(gpOnboardingData.SalesCodeofBO)
        binding.tvMobileNumber.setText(gpOnboardingData.GPMobileNumber)
        binding.tvName.setText(gpOnboardingData.GPName)
        binding.tvBusinessName.setText(gpOnboardingData.BusinessName)
        binding.tvBusinessType.setText(gpOnboardingData.BusinessType)
        binding.tvPincode.setText(gpOnboardingData.Pincode)

        //GP KYC Detail
        binding.tvKYCStatus.setText(gpOnboardingData.KYCStatus)
        //GP Establishment
        try {
            Glide.with(this)
                .load(gpOnboardingData.EstablishmentPhotoURL)
                .into(binding?.imgStorePhoto!!)
        } catch (e: Exception) {
        }

        //Bank Account Details
        binding.tvAccountHolderName.setText(gpOnboardingData.AccountHolderName)
        binding.tvAccountNumber.setText(gpOnboardingData.AccountNumber)
        binding.tvBankName.setText(gpOnboardingData.BankName)
        binding.tvIFSCCode.setText(gpOnboardingData.IFSCCode)
        binding.tvPANNumber.setText(gpOnboardingData.PANNumber)


        //binding.c.setText(finalData.City)
        //binding.tvSalesCodeofBO.setText(finalData.State)
        //test


        setOnClicks()
    }


    private fun HappinessDialog(){
        val happinessCodeBox = HappinessCodeBox(this, getString(R.string.hapinessCodeMsg),
            getString(R.string.EnterHappinessCode),
            getString(R.string.resand_sms))
        happinessCodeBox.show()
    }

     fun launchNextScreen(activity: Activity){
        val getIntent= Intent(this, activity::class.java)
        startActivity(getIntent)
    }

    private fun setOnClicks() {
        binding.BankAccountDetailsEdit.setOnClickListener(this)
        binding.personlDetail.setOnClickListener(this)
        binding.StorePhotoLinearEdit.setOnClickListener(this)
        binding.btnPreview.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        val item_id = v?.id
        when (item_id) {
            R.id.BankAccountDetailsEdit ->  startActivity(Intent(this,GPOnboardingActivity::class.java))
            R.id.personlDetail -> startActivity(Intent(this,GPPersonalDetailUpdateActivity::class.java))
            R.id.StorePhotoLinearEdit ->  startActivity(Intent(this,GPOnBoardStoreProofActivity::class.java))
            R.id.btnPreview ->  HappinessDialog()
        }
    }
}