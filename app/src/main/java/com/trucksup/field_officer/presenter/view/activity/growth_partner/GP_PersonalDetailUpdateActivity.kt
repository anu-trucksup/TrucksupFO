package com.trucksup.field_officer.presenter.view.activity.growth_partner

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGpPersonalDetailUpdateBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class GP_PersonalDetailUpdateActivity : BaseActivity() {
    private lateinit var binding: ActivityGpPersonalDetailUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gp_personal_detail_update)
        val view = binding.root
        setContentView(view)
    }
    fun growth_kyc(view: View) {
        startActivity(Intent(this, GPOnboardingKYCActivity::class.java))
    }
}