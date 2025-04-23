package com.trucksup.field_officer.presenter.view.activity.todayFollowup

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.trucksup.field_officer.databinding.ActivityFollowUpBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.profile.vml.TotalDownloadViewModel
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.vml.TodayFollowUpViewModel
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.TSPlannedFollowupActivity
import com.trucksup.field_officer.presenter.view.adapter.FollowupSelectionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FollowUpActivity : BaseActivity() {
    private lateinit var binding: ActivityFollowUpBinding
    private var mViewModel: TodayFollowUpViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFollowUpBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[TodayFollowUpViewModel::class.java]

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