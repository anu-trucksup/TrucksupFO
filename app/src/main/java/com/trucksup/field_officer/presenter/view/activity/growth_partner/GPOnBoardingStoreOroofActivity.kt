package com.trucksup.field_officer.presenter.view.activity.growth_partner

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGponBoardingStoreOroofBinding

class GPOnBoardingStoreOroofActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGponBoardingStoreOroofBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gpon_boarding_store_oroof)
        val view = binding.root
        setContentView(view)
    }
}