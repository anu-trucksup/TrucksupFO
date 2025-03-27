package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityBaMaptripBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class TSStartTripActivity : BaseActivity() {
    private lateinit var binding: ActivityBaMaptripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ba_maptrip)
        binding = ActivityBaMaptripBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            startActivity(Intent(this, TSScheduledMeetingActivity::class.java))
        }
    }
}