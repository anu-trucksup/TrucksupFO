package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGpOnboardingPreviewBinding
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox

class GpOnboardingPreviewActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityGpOnboardingPreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gp_onboarding_preview)
        val view = binding.root
        setContentView(view)

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