package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.databinding.ActivityTruckSupplierDetailBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class TruckSupplierDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityTruckSupplierDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTruckSupplierDetailBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        //populateAdapterWithInfo()

        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

}