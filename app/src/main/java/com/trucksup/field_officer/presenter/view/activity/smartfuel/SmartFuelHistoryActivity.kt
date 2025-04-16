package com.trucksup.field_officer.presenter.view.activity.smartfuel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.data.model.smartfuel.SmartFuelHistoryRequest
import com.trucksup.field_officer.data.model.smartfuel.SmartFuelHistoryResponse
import com.trucksup.field_officer.databinding.ActivityFinanceHistoryBinding
import com.trucksup.field_officer.databinding.ActivitySmartfuelHistoryBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.HistoryFnIsFragment
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceHistoryViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InquiryHistoryRequest
import com.trucksup.field_officer.presenter.view.adapter.FragmentAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList


@AndroidEntryPoint
class SmartFuelHistoryActivity : BaseActivity() {

    private lateinit var binding: ActivitySmartfuelHistoryBinding
    private var mViewModel: SmartFuelViewModel? = null
    private var activatedHistoryList: ArrayList<SmartFuelHistoryResponse.LeadsHistory> = arrayListOf()
    private var currentHistoryList: ArrayList<SmartFuelHistoryResponse.LeadsHistory> = arrayListOf()
    private var rejectedHistoryList: ArrayList<SmartFuelHistoryResponse.LeadsHistory> = arrayListOf()
    private lateinit var fragment1: HistorySmartFuelFragment
    private lateinit var fragment2: HistorySmartFuelFragment
    private lateinit var fragment3: HistorySmartFuelFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_smartfuel_history)

        mViewModel = ViewModelProvider(this)[SmartFuelViewModel::class.java]

//        setListener()
//        setupViewPager()

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
//            val fragment1 = HistorySmartFuelFragment("active")
//            val fragment2 = HistorySmartFuelFragment("complete")
//            val fragment3 = HistorySmartFuelFragment("reject")

            fragment1 = HistorySmartFuelFragment("current",currentHistoryList)//current
            fragment2 = HistorySmartFuelFragment("activated",activatedHistoryList)//activated
            fragment3 = HistorySmartFuelFragment("reject",rejectedHistoryList)//rejected

            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)
            adapter.addFragment(fragment3)
            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled = false

            binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        );
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.white))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.blue))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.blue))

                    } else if (position == 1) {

                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        )
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.blue))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.white))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.blue))
                    } else if (position == 2) {
                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@SmartFuelHistoryActivity,
                                R.drawable.ba_tab_unselected_background
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

    fun addNewEnquiry(view: View) {
        val intent = Intent(this, AddSmartFuelActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun enquiryHistory() {
        showProgressDialog(this,false)
        val request = SmartFuelHistoryRequest(
            ""+PreferenceManager.getPhoneNo(this),
            ""+"456789",
            ""+PreferenceManager.getServerDateUtc(),
            ""+PreferenceManager.getRequestNo(),
            ""+PreferenceManager.getPhoneNo(this),
        )
        mViewModel?.getSmartFuelHistory(request)
    }

    private fun setupObserver() {
        mViewModel?.resultSmartFuelHistoryLD?.observe(this) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(this, responseModel.serverError.toString(), "m")
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success != null) {
                    if (responseModel.success.statuscode==200) {
                        inquiryHistorySuccess(responseModel.success)
                    }
                    else
                    {

                    }
                }
                else
                {

                }
            }
        }
    }

    private fun inquiryHistorySuccess(inquiryHistoryResponse: SmartFuelHistoryResponse) {
        inquiryHistoryResponse?.leadsHistory?.forEachIndexed { _, inquiryHistory ->
            run {
                inquiryHistory.leadDetails.forEachIndexed { _, historyDetails ->
                    run {
                        if (historyDetails.cardStatus.equals("Amount Disbursed")) {
                            activatedHistoryList.add(inquiryHistory)
                        }else  if (historyDetails.cardStatus.equals("Rejected")) {
                            rejectedHistoryList.add(inquiryHistory)
                        }else{
                            activatedHistoryList.add(inquiryHistory)
                        }
                    }
                }

            }
        }

        binding.tvTotalEnquiry.text = "${inquiryHistoryResponse?.leadsHistory?.size} Enquiries"
        setupViewPager()
    }


}