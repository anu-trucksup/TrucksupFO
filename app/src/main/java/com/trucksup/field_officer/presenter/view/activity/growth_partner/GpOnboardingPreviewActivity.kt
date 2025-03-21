package com.trucksup.field_officer.presenter.view.activity.growth_partner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGpOnboardingPreviewBinding
import com.trucksup.field_officer.presenter.utils.HF
import com.trucksup.field_officer.presenter.view.activity.other.PersonalDetailUpdateActivity

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

     fun launchNextScreen(activity: Activity){
        val getIntent= Intent(this, activity::class.java)
        startActivity(getIntent)
    }

    private fun setOnClicks() {
        binding.BankAccountDetailsEdit.setOnClickListener(this)
        binding.personlDetail.setOnClickListener(this)
        binding.StorePhotoLinearEdit.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        val item_id = v?.id
        when (item_id) {
            R.id.BankAccountDetailsEdit ->  HF(this).startActivity(GPOnboardingActivity())
            R.id.personlDetail -> HF(this).startActivity(PersonalDetailUpdateActivity())
            R.id.StorePhotoLinearEdit ->  HF(this).startActivity(GPOnBoardingStoreOroofActivity())
        }
    }
}