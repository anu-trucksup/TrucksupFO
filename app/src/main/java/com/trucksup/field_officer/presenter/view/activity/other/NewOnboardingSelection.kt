package com.trucksup.field_officer.presenter.view.activity.other

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.databinding.ActivityNewOnboardingBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.business_associate.BA_NewOnboardingActivity
import com.trucksup.field_officer.presenter.view.activity.growth_partner.GA_NewOnboardingActivity
import com.trucksup.field_officer.presenter.view.activity.growth_partner.GPOnboardingActivity
import com.trucksup.field_officer.presenter.view.activity.truck_owner.TruckSupplierDetailActivity
import com.trucksup.field_officer.presenter.view.activity.truck_supplier.TC_NewOnboardingActivity

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
        startActivity(Intent(this, TC_NewOnboardingActivity::class.java))
    }

    fun business_associate(view: View) {
        //startActivity(Intent(this, BA_NewOnboardingActivity::class.java))
    }

    fun growth_partner(view: View) {
        //startActivity(Intent(this, GA_NewOnboardingActivity::class.java))
    }
}