package com.trucksup.field_officer.presenter.view.activity.other

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.databinding.ActivityNewOnboardingBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.business_associate.BAOnboardingUploadDocActivity
import com.trucksup.field_officer.presenter.view.activity.business_associate.BAnOnboardingActivity
import com.trucksup.field_officer.presenter.view.activity.growth_partner.GPOnboardingActivity
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.MiscActivity
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.TSOnboardingActivity

class NewOnboardingSelection : BaseActivity() {
    private lateinit var binding: ActivityNewOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewOnboardingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        //back button
        binding.ivBack.setOnClickListener {
           onBackPressed()
        }


    }

    fun truck_supplier(view: View) {
        startActivity(Intent(this, TSOnboardingActivity::class.java))
    }

    fun business_associate(view: View) {
        startActivity(Intent(this, BAOnboardingUploadDocActivity::class.java))
    }

    fun growth_partner(view: View) {
        startActivity(Intent(this, GPOnboardingActivity::class.java))
    }

    fun miscell_onboard(view: View) {
        startActivity(Intent(this, MiscActivity::class.java))
    }

}