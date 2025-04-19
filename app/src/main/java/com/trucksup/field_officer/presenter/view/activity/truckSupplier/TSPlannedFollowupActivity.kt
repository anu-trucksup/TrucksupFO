package com.trucksup.field_officer.presenter.view.activity.truckSupplier

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.databinding.ActivityFinanceHistoryBinding
import com.trucksup.field_officer.databinding.ActivityTsplannedFollowupBinding
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceHistoryViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InquiryHistoryRequest
import com.trucksup.field_officer.presenter.view.adapter.FragmentAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList


@AndroidEntryPoint
class TSPlannedFollowupActivity : BaseActivity() {

    private var activehistoryList: ArrayList<InquiryHistoryResponse.InquiryHistory> = arrayListOf()
    private var completehistoryList: ArrayList<InquiryHistoryResponse.InquiryHistory> = arrayListOf()
    private var rejectedhistoryList: ArrayList<InquiryHistoryResponse.InquiryHistory> = arrayListOf()
    private lateinit var fragment1: PlannedFollowupFragment
    private lateinit var fragment2: PlannedFollowupFragment
    private lateinit var fragment3: PlannedFollowupFragment
    private lateinit var binding: ActivityTsplannedFollowupBinding
    private var historyType: String? = ""
    private var mViewModel: FinanceHistoryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        adjustFontScale(getResources().configuration, 1.0f);
        binding = ActivityTsplannedFollowupBinding.inflate(layoutInflater)
        mViewModel = ViewModelProvider(this)[FinanceHistoryViewModel::class.java]
        historyType = intent.getStringExtra("HISTORY_TYPE")

        enquiryHistory()
        setupObserver()

        setListener()
    }

    private fun setListener() {

        //back button
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        //active button
        binding.tabActive.setOnClickListener {
            binding.viewPager2.setCurrentItem(0, true)
        }

        //completed button
        binding.tabCompleted.setOnClickListener {
            binding.viewPager2.setCurrentItem(1, true)
        }

        //reject button
        binding.tabRejected.setOnClickListener {
            binding.viewPager2.setCurrentItem(2, true)
        }
    }

    private fun setupViewPager() {
        try {
            val adapter = FragmentAdapter(this)
            fragment1 = PlannedFollowupFragment("active", activehistoryList)
            fragment2 = PlannedFollowupFragment("complete", completehistoryList)
            fragment3 = PlannedFollowupFragment("reject", rejectedhistoryList)
            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)
            adapter.addFragment(fragment3)
            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled = false

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@TSPlannedFollowupActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        );
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@TSPlannedFollowupActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@TSPlannedFollowupActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.white))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.blue))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.blue))


                    } else if (position == 1) {

                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@TSPlannedFollowupActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@TSPlannedFollowupActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        )
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@TSPlannedFollowupActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.blue))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.white))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.blue))

                    } else if (position == 2) {
                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@TSPlannedFollowupActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@TSPlannedFollowupActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@TSPlannedFollowupActivity,
                                R.drawable.tab_unselected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.blue))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.blue))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.white))

                    }
                }
            })
        } catch (_: Exception) {
        }

    }

    private fun enquiryHistory() {
        LoadingUtils.showDialog(this, false)
        val request = InquiryHistoryRequest(
            requestId = PreferenceManager.getRequestNo(),
            requestedBy = PreferenceManager.getPhoneNo(this),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            mobilenumber = PreferenceManager.getPhoneNo(this),
            referralCode = "7B4C18",
            historyType = historyType!!
        )
        mViewModel?.inquiryHistory(request)
    }

    private fun setupObserver() {
        mViewModel?.resultInquiryHistoryLD?.observe(this) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                LoadingUtils.hideDialog()

                val abx = AlertBoxDialog(this, responseModel.serverError.toString(), "m")
                abx.show()
            } else {
                LoadingUtils.hideDialog()

                if (responseModel.success != null) {
                    inquiryHistorySuccess(responseModel.success)
                } else {
                }
            }
        }
    }

    private fun inquiryHistorySuccess(inquiryHistoryResponse: InquiryHistoryResponse) {
        inquiryHistoryResponse?.inquiryHistory?.forEachIndexed { _, inquiryHistory ->
            run {
                inquiryHistory.historyDetails.forEachIndexed { _, historyDetails ->
                    run {
                        if (historyDetails.status.equals("Amount Disbursed")) {
                            completehistoryList.add(inquiryHistory)
                        } else if (historyDetails.status.equals("Rejected")) {
                            rejectedhistoryList.add(inquiryHistory)
                        } else {
                            activehistoryList.add(inquiryHistory)
                        }
                    }
                }

            }
        }

        setupViewPager()

    }


}