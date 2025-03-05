package com.trucksup.field_officer.presenter.view.activity.financeInsurance

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.insurance.FinanceHisAdap
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
import com.trucksup.field_officer.databinding.ActivityFinanceHistoryBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.fragment.ba.ActiveBAFragment
import com.trucksup.fieldofficer.adapter.FragmentAdapter

class FinanceHistoryActivity : BaseActivity(), EnquiryHistoryController {

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

        //completed button
        binding.tabRejected.setOnClickListener {
            binding.viewPager2.setCurrentItem(1, true)
        }
    }

    private fun setupViewPager() {

        try {
            val adapter = FragmentAdapter(this)
            val fragment1 = ActiveBAFragment()
            val fragment2 = ActiveBAFragment()
            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)
            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled = false

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.tabActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BusinessAssociatesNewActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        );
                        binding.tabInActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BusinessAssociatesNewActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.txtActiveBA.setTextColor(resources.getColor(R.color.white))
                        binding.txtInActiveBA.setTextColor(resources.getColor(R.color.blue))


                        /*binding.vLine1.setBackgroundColor(resources.getColor(R.color.blue))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.unselect_tab))*/
                        //binding.vLine3.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                    } else if (position == 1) {

                        binding.tabInActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BusinessAssociatesNewActivity,
                                R.drawable.ba_tab_unselected_background
                            )
                        );
                        binding.tabActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BusinessAssociatesNewActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.txtActiveBA.setTextColor(resources.getColor(R.color.blue))
                        binding.txtInActiveBA.setTextColor(resources.getColor(R.color.white))


                        /*binding.vLine1.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.blue))*/
                    } else {
                        binding.tabInActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BusinessAssociatesNewActivity,
                                R.drawable.tab_selected_background
                            )
                        );
                        binding.tabActiveBA.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                this@BusinessAssociatesNewActivity,
                                R.drawable.tab_unselected_background
                            )
                        );
                        binding.txtActiveBA.setTextColor(resources.getColor(R.color.white))
                        binding.txtInActiveBA.setTextColor(resources.getColor(R.color.blue))


                        /*binding.vLine1.setBackgroundColor(resources.getColor(R.color.unselect_tab))
                        binding.vLine2.setBackgroundColor(resources.getColor(R.color.unselect_tab))*/
                    }
                }
            })
        } catch (e: Exception) {

        }

    }

    private fun setRvList(list2: ArrayList<InquiryHistoryResponse.InquiryHistory>) {
        binding.rv.apply {
            layoutManager =
                LinearLayoutManager(this@FinanceHistoryActivity, RecyclerView.VERTICAL, false)
            adapter = FinanceHisAdap(this@FinanceHistoryActivity, list2)
            hasFixedSize()
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

    override fun inquiryHistorySuccess(inquiryHistoryResponse: InquiryHistoryResponse) {
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
    }

}