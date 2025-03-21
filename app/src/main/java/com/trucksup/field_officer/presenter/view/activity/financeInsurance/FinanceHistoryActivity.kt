package com.trucksup.field_officer.presenter.view.activity.financeInsurance

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityFinanceHistoryBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.fieldofficer.adapter.FragmentAdapter

class FinanceHistoryActivity : BaseActivity()/*, EnquiryHistoryController*/ {

    private lateinit var binding: ActivityFinanceHistoryBinding
    private var historyType: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finance_history)

        historyType = intent.getStringExtra("HISTORY_TYPE")

        if (historyType == "Finance") {
            binding.appBarTitle.text = getText(R.string.finance_previous_enquiry)
            binding.tvAddNew.text = getText(R.string.add_new_finance)
        } else if (historyType == "Insurance") {
            binding.appBarTitle.text = getText(R.string.insurance_previous_enquiry)
            binding.tvAddNew.text = getText(R.string.add_new_insurance)
        }

        enquiryHistory()

        setListener()
        setupViewPager()
    }

    private fun setListener() {

        //back button
        binding.btnBack.setOnClickListener {
            finish()
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
            val fragment1 = HistoryFnIsFragment()
            val fragment2 = HistoryFnIsFragment()
            val fragment3 = HistoryFnIsFragment()
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
        } catch (e: Exception) {

        }

    }


    fun addNewEnquery(view: View) {
        if (historyType == "Finance") {
            val intent = Intent(this, FinanceActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, InsuranceActivity::class.java)
            startActivity(intent)
        }

    }

    private fun enquiryHistory() {
        /* LoadingUtils.showDialog(this, false)
         var request = InquiryHistoryRequest(
             requestId = PreferenceManager.getRequestNo(),
             requestedBy = PreferenceManager.getPhoneNo(this),
             requestDatetime = PreferenceManager.getServerDateUtc(""),
             mobilenumber = PreferenceManager.getPhoneNo(this),
             referralCode = PreferenceManager.getUserData(this)?.salesCode?:"",
             historyType = historyType!!
         )
         MyResponse().inquiryHistory(request, this, this)*/
    }

   /* override fun inquiryHistorySuccess(inquiryHistoryResponse: InquiryHistoryResponse) {
        dismissProgressDialog()
        if (inquiryHistoryResponse.inquiryHistory.isNullOrEmpty()) {
            binding.rv.visibility = View.GONE
            binding.noData.visibility = View.VISIBLE
        } else {
            binding.rv.visibility = View.VISIBLE
            binding.noData.visibility = View.GONE
            setRvList(inquiryHistoryResponse.inquiryHistory)
        }
    }

    override fun inquiryHistoryFailure(msg: String) {
        dismissProgressDialog()
    }*/

}