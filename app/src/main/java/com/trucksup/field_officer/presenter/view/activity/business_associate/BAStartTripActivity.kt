package com.trucksup.field_officer.presenter.view.activity.business_associate

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityBaMaptripBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class BAStartTripActivity : BaseActivity() {
    private lateinit var binding: ActivityBaMaptripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaMaptripBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            startActivity(Intent(this, BAScheduleMeetingActivity::class.java))
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
}