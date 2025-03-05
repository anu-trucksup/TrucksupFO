package com.trucksup.field_officer.presenter.view.activity.other

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.databinding.ActivityNewOnboardingBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.truck_owner.TruckSupplierDetailActivity

class NewOnboardingSelection : BaseActivity() {
    private lateinit var binding: ActivityNewOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewOnboardingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

    }
    fun onBack(view: View) {
        finish()
    }

    fun truck_supplier(view: View) {

        startActivity(Intent(this, TruckSupplierDetailActivity::class.java))
    }

    fun business_associate(view: View) {

    }

    fun growth_partner(view: View) {

    }
}