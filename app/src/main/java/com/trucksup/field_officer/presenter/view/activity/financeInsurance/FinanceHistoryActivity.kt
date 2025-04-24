package com.trucksup.field_officer.presenter.view.activity.financeInsurance

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
class FinanceHistoryActivity : BaseActivity() {

    private var activehistoryList: ArrayList<InquiryHistoryResponse.InquiryHistory> = arrayListOf()
    private var completehistoryList: ArrayList<InquiryHistoryResponse.InquiryHistory> = arrayListOf()
    private var rejectedhistoryList: ArrayList<InquiryHistoryResponse.InquiryHistory> = arrayListOf()
    private lateinit var fragment1: HistoryFnIsFragment
    private lateinit var fragment2: HistoryFnIsFragment
    private lateinit var fragment3: HistoryFnIsFragment
    private lateinit var binding: ActivityFinanceHistoryBinding
    private var historyType: String? = ""
    private var mViewModel: FinanceHistoryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finance_history)
        mViewModel = ViewModelProvider(this)[FinanceHistoryViewModel::class.java]
        historyType = intent.getStringExtra("HISTORY_TYPE")


        if (historyType == "Finance") {
            binding.appBarTitle.text = getText(R.string.finance_previous_enquiry)
            binding.tvAddNew.text = getText(R.string.add_new_finance)
        } else if (historyType == "Insurance") {
            binding.appBarTitle.text = getText(R.string.insurance_previous_enquiry)
            binding.tvAddNew.text = getText(R.string.add_new_insurance)
        }

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
           fragment1 = HistoryFnIsFragment("active",activehistoryList)
           fragment2 = HistoryFnIsFragment("complete",completehistoryList)
           fragment3 = HistoryFnIsFragment("reject",rejectedhistoryList)
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
                                this@FinanceHistoryActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        );
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@FinanceHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@FinanceHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.white))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.blue))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.blue))


                    } else if (position == 1) {

                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@FinanceHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@FinanceHistoryActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        )
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@FinanceHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.blue))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.white))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.blue))

                    } else if (position == 2) {
                        binding.tabActive.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@FinanceHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.tabCompleted.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@FinanceHistoryActivity,
                                R.drawable.tab_selected_background
                            )
                        )
                        binding.tabRejected.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@FinanceHistoryActivity,
                                R.drawable.tab_unselected_background
                            )
                        )
                        binding.txtActive.setTextColor(resources.getColor(R.color.blue))
                        binding.txtCompleted.setTextColor(resources.getColor(R.color.blue))
                        binding.txtRejected.setTextColor(resources.getColor(R.color.white))

                    }
                }
            })
        } catch (_: Exception) { }

    }

    fun addNewEnquiry(view: View) {
        if (historyType == "Finance") {
            val intent = Intent(this, FinanceActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, InsuranceActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun enquiryHistory() {
        LoadingUtils.showDialog(this, false)
        val request = InquiryHistoryRequest(
            requestId = PreferenceManager.getRequestNo(),
            requestedBy = PreferenceManager.getPhoneNo(this),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            mobilenumber = PreferenceManager.getPhoneNo(this),
            referralCode = PreferenceManager.getUserData(this)?.referralcode?:"",
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
                var enquiryFlag=0 //0=active,1=completed,2=rejected
                inquiryHistory.historyDetails.forEachIndexed { _, historyDetails ->
                    run {
                        if (historyDetails.status.equals("Amount Disbursed")) {
//                            completehistoryList.add(inquiryHistory)
                            enquiryFlag=1
                            return@run
                        }else  if (historyDetails.status.equals("Rejected")) {
//                            rejectedhistoryList.add(inquiryHistory)
                            enquiryFlag=2
                            return@run
                        }else{
//                            activehistoryList.add(inquiryHistory)
                            enquiryFlag=0
                        }
                    }
                }

                if (enquiryFlag==0)
                {
                    activehistoryList.add(inquiryHistory)
                }
                else if (enquiryFlag==1)
                {
                    completehistoryList.add(inquiryHistory)
                }
                else if (enquiryFlag==2)
                {
                    rejectedhistoryList.add(inquiryHistory)
                }

            }
        }

        binding.tvTotalEnquiry.text = "${inquiryHistoryResponse?.inquiryHistory?.size} Enquiries"
        setupViewPager()

    }


}