package com.trucksup.field_officer.presenter.view.activity.addLoads

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.ActivityTotaladdDetailsBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.addLoads.vml.TotalLoadsViewModel
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
import com.trucksup.field_officer.presenter.view.adapter.TotalLoadsAdapter2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TotalAddDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityTotaladdDetailsBinding
    private var mViewModel: TotalLoadsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTotaladdDetailsBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this)[TotalLoadsViewModel::class.java]

        showProgressDialog(this, false)
        val request = FollowUpRequest(
            requestId = PreferenceManager.getRequestNo().toInt(),
            requestedBy = PreferenceManager.getPhoneNo(this),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            boID = PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0
        )
        mViewModel?.getTotalAddLoadDetails(PreferenceManager.getAuthToken(), request)

        setupObserver()
        setListeners()
    }

    private fun setupObserver() {
        mViewModel?.totalAddLoadsDetailsLD?.observe(this@TotalAddDetailsActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@TotalAddDetailsActivity,
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
                            this@TotalAddDetailsActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }

    }

    private fun setItemList(success: FollowUpResponse) {
        setRecycleViewList()
    }

    private fun setRecycleViewList() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        binding.rv.apply {
            adapter = TotalLoadsAdapter2(this@TotalAddDetailsActivity, list)
            layoutManager =
                LinearLayoutManager(this@TotalAddDetailsActivity, RecyclerView.VERTICAL, false)
            hasFixedSize()
        }
    }

    private fun setListeners() {
        //back
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

}