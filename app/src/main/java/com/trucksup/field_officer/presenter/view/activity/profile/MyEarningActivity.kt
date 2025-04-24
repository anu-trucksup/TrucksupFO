package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.trucksup.field_officer.databinding.ActivityMyEarningBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.profile.vml.EditProfileViewModel
import com.trucksup.field_officer.presenter.view.activity.profile.vml.MyEarningViewModel
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
import com.trucksup.field_officer.presenter.view.adapter.MyEarningAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyEarningActivity : BaseActivity() {

    private lateinit var binding: ActivityMyEarningBinding
    private var mViewModel: MyEarningViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyEarningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[MyEarningViewModel::class.java]

        showProgressDialog(this, false)
        val request = FollowUpRequest(
            requestId = PreferenceManager.getRequestNo().toInt(),
            requestedBy = PreferenceManager.getPhoneNo(this),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            boID = PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0
        )
        mViewModel?.getTotalEarning(PreferenceManager.getAuthToken(), request)

        setlistofEarn()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        setupObserver()
    }

    private fun setupObserver() {
        mViewModel?.resultTodayEarningLD?.observe(this@MyEarningActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@MyEarningActivity,
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
                            this@MyEarningActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }

    }

    private fun setItemList(success: FollowUpResponse) {
        setlistofEarn()
    }

    private fun setlistofEarn() {

        binding.rvTotalEarn.apply {
            layoutManager = GridLayoutManager(this@MyEarningActivity, 2)
            adapter = MyEarningAdapter(this@MyEarningActivity)
            hasFixedSize()
        }
    }
}