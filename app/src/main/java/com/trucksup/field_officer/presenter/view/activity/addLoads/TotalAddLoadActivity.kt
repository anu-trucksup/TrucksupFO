package com.trucksup.field_officer.presenter.view.activity.addLoads

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.databinding.ActivityTotalAddLoadBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.addLoads.vml.TotalLoadsViewModel
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
import com.trucksup.field_officer.presenter.view.adapter.TotalLoadsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TotalAddLoadActivity : BaseActivity() {

    private lateinit var binding: ActivityTotalAddLoadBinding
    private var mViewModel: TotalLoadsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTotalAddLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this)[TotalLoadsViewModel::class.java]

        showProgressDialog(this, false)
        val request = FollowUpRequest(
            requestId = PreferenceManager.getRequestNo().toInt(),
            requestedBy = PreferenceManager.getPhoneNo(this),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            boID = PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0
        )
        mViewModel?.getTotalAddLoad(PreferenceManager.getAuthToken(), request)

        setupObserver()

        setListofLoad()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupObserver() {
        mViewModel?.resultTotalAddLoadsLD?.observe(this@TotalAddLoadActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@TotalAddLoadActivity,
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
                            this@TotalAddLoadActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }

    }

    private fun setItemList(success: FollowUpResponse) {
        setListofLoad()
    }

    private fun setListofLoad() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")

        binding.rvTotalLoads.apply {
            layoutManager = LinearLayoutManager(this@TotalAddLoadActivity)
            adapter = TotalLoadsAdapter(this@TotalAddLoadActivity,list)
            hasFixedSize()
        }
    }
}