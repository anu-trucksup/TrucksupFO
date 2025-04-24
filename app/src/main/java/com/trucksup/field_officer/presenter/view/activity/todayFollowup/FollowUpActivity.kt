package com.trucksup.field_officer.presenter.view.activity.todayFollowup

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.trucksup.field_officer.databinding.ActivityFollowUpBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
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

        showProgressDialog(this, false)
        val request = FollowUpRequest(
            requestId = PreferenceManager.getRequestNo().toInt(),
            requestedBy = PreferenceManager.getPhoneNo(this),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            boID = PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0
        )
        mViewModel?.getTodayFollowup(PreferenceManager.getAuthToken(), request)


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.cslPlannedFollowup.setOnClickListener {
            startActivity(Intent(this, TSPlannedFollowupActivity::class.java))
        }

        setupObserver()
    }

    private fun setupObserver() {
        mViewModel?.resultTodayFollowupLD?.observe(this@FollowUpActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@FollowUpActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {
                    setItemList(responseModel.success)
                } else {
                    val abx =
                        AlertBoxDialog(
                            this@FollowUpActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }

    }

    private fun setItemList(followUpCounts: FollowUpResponse?) {

        binding.tvTotalCount.text = followUpCounts?.meetsCounts?.total.toString()
        binding.tvScheduledCount.text = followUpCounts?.todayFollowUpCounts?.scheduled.toString()
        binding.tvCompletedCount.text = followUpCounts?.todayFollowUpCounts?.completed.toString()
        binding.tvPlannedCount.text = followUpCounts?.todayFollowUpCounts?.plannedFollowUp.toString()
        binding.rvSecondServices.apply {
            layoutManager = GridLayoutManager(this@FollowUpActivity, 3)
            adapter = FollowupSelectionAdapter(this@FollowUpActivity,followUpCounts?.meetsCounts)
            hasFixedSize()
        }
    }
}