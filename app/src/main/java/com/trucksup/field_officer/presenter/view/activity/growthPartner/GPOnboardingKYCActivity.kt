package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGponboardingKycactivityBinding

class GPOnboardingKYCActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGponboardingKycactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gponboarding_kycactivity)
        val view = binding.root
        setContentView(view)
    }
    fun growth_proof(view: View) {
        startActivity(Intent(this, GPOnBoardStoreProofActivity::class.java))
    }
}