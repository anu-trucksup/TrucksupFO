package com.trucksup.field_officer.presenter.view.activity.other

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import com.trucksup.field_officer.databinding.ActivityFollowUpBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.TSOnBoardStep2Activity
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.TSPlannedFollowupActivity
import com.trucksup.field_officer.presenter.view.adapter.FollowupSelectionAdapter

class FollowUpActivity : BaseActivity() {
    private lateinit var binding: ActivityFollowUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFollowUpBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.cslPlannedFollowup.setOnClickListener {
            startActivity(Intent(this, TSPlannedFollowupActivity::class.java))
        }
        setItemList()
    }

    private fun setItemList() {

        binding.rvSecondServices.apply {
            layoutManager = GridLayoutManager(this@FollowUpActivity, 3)
            adapter = FollowupSelectionAdapter(this@FollowUpActivity)
            hasFixedSize()
        }
    }
}