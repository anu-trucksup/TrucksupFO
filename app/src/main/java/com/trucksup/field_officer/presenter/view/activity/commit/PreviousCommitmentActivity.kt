package com.trucksup.field_officer.presenter.view.activity.commit

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.data.model.home.HomeCountRequest
import com.trucksup.field_officer.databinding.ActivityPreviousCommitBinding
import com.trucksup.field_officer.databinding.ActivityTotalAddLoadBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.commit.vml.CommitmentViewModel
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.adapter.TotalLoadsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviousCommitmentActivity : BaseActivity() {

    private lateinit var binding: ActivityPreviousCommitBinding
    private var mViewModel: CommitmentViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPreviousCommitBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[CommitmentViewModel::class.java]
        setlistofLoad()

        showProgressDialog(this, false)
        val request = HomeCountRequest(
            PreferenceManager.getServerDateUtc(),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0
        )
        mViewModel?.getAllPrevCommitList(request)

        setupObserver()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }
    private fun setupObserver() {
        mViewModel?.resultPrevCommitListLD?.observe(this@PreviousCommitmentActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@PreviousCommitmentActivity,
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
                            this@PreviousCommitmentActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }

    }

    private fun setItemList(success: Any) {
        setlistofLoad()
    }

    private fun setlistofLoad() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")

        binding.rvTotalLoads.apply {
            layoutManager = LinearLayoutManager(this@PreviousCommitmentActivity)
            adapter = AllCommitAdapter(this@PreviousCommitmentActivity,list)
            hasFixedSize()
        }
    }
}