package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.databinding.ActivityMyTargetBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class MyTargetScreen : BaseActivity() {
    private lateinit var binding: ActivityMyTargetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTargetBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }
}