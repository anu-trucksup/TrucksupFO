package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.databinding.ActivityTotalAddLoadBinding
import com.trucksup.field_officer.databinding.ActivityTotalDownloadBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.profile.vml.EditProfileViewModel
import com.trucksup.field_officer.presenter.view.activity.profile.vml.TotalDownloadViewModel
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
import com.trucksup.field_officer.presenter.view.adapter.ServicesMainAdapter
import com.trucksup.field_officer.presenter.view.adapter.TotalLoadsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TotalDownloadsActivity : BaseActivity() {

    private lateinit var binding: ActivityTotalDownloadBinding
    private var mViewModel: TotalDownloadViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTotalDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[TotalDownloadViewModel::class.java]

        showProgressDialog(this, false)
        val request = FollowUpRequest(
            requestId = PreferenceManager.getRequestNo().toInt(),
            requestedBy = PreferenceManager.getPhoneNo(this),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            boID = PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0
        )
        mViewModel?.getTotalDownload(PreferenceManager.getAuthToken(), request)

        setupObserver()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupObserver() {
        mViewModel?.resultTotalDownloadLD?.observe(this@TotalDownloadsActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@TotalDownloadsActivity,
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
                            this@TotalDownloadsActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }

    }

    private fun setItemList(success: FollowUpResponse) {
        setListOfLoad()
    }

    private fun setListOfLoad() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")

        binding.rvTotalLoads.apply {
            layoutManager = LinearLayoutManager(this@TotalDownloadsActivity)
            adapter = TotalLoadsAdapter(this@TotalDownloadsActivity,list)
            hasFixedSize()
        }
    }
}