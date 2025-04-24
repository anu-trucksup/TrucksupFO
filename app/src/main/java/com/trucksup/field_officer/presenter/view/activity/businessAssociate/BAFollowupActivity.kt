package com.trucksup.field_officer.presenter.view.activity.businessAssociate

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.BaFollowupActivityBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.BoVisitDetail
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllMeetUpBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllMeetupBAResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAFollowUpViewModel
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAScheduleMeetingVM
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllMeetupTSRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetMeetScheduleDetailsResponse
import com.trucksup.field_officer.presenter.view.fragment.ba.BACompletedFragment
import com.trucksup.field_officer.presenter.view.fragment.ba.BAScheduledFragment
import com.trucksup.field_officer.presenter.view.adapter.FragmentAdapter
import com.trucksup.field_officer.presenter.view.adapter.TSPerformanceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BAFollowupActivity : BaseActivity() {

    private lateinit var binding: BaFollowupActivityBinding
    private var mViewModel: BAFollowUpViewModel? = null

    private var getAllBAMeetsList: java.util.ArrayList<BoVisitDetail> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BaFollowupActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[BAFollowUpViewModel::class.java]

        //showProgressDialog(this, false)
        /*val request = GetAllMeetUpBARequest(
            requestId = PreferenceManager.getRequestNo().toInt(),
            requestedBy = PreferenceManager.getPhoneNo(this),
            requestDatetime = PreferenceManager.getServerDateUtc(),
            boID = PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0,
            type = "Scheduled"
        )
        mViewModel?.getAllMeetupBA(PreferenceManager.getAuthToken(), request)

        setupObserver()*/

        setupViewPager()
        setListener()
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        //scheduled button
        binding.tabSchedule.setOnClickListener {
            binding.viewPager2.setCurrentItem(0, true)
        }

        //completed button
        binding.tabCompleted.setOnClickListener {
            binding.viewPager2.setCurrentItem(1, true)
        }
    }

    private fun setupObserver() {
        mViewModel?.getAllMeetUpBAResponseLD?.observe(this@BAFollowupActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@BAFollowupActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {
                    getAllMeetupBAResponse(responseModel.success)
                    // setItemList(responseModel.success)
                } else {
                    val abx =
                        AlertBoxDialog(
                            this@BAFollowupActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }
    }

    private fun getAllMeetupBAResponse(getAllMeetupBAResponse: GetAllMeetupBAResponse) {
        getAllMeetupBAResponse?.boVisitDetails?.forEachIndexed { _, getTSDetailsData ->
            run {
                getAllBAMeetsList.add(getTSDetailsData)
            }
        }



        setupViewPager()
        println("Data=="+getAllBAMeetsList.size)

        /*binding.rv.layoutManager = LinearLayoutManager(this)
        val adapter = TSPerformanceAdapter(this@TSPerformanceActivity, getTsdetails)

        adapter.setOnItemClickListener(object : TSPerformanceAdapter.OnItemClickListener {
            override fun onItemClick(ownerName: String, selectedDate: String, selectedTime: String) {
                dataSubmit(ownerName, selectedDate, selectedTime)
            }
        })
        binding.rv.adapter = adapter

        // Add search or filter input
        binding.etSearchFillter.addTextChangedListener {
            adapter.filter(it.toString())
        }*/

        //binding.tvTotalEnquiry.text = "${inquiryHistoryResponse?.leadsHistory?.size} Enquiries"
        //setupViewPager()
    }

    private fun setupViewPager() {

        try {
            val adapter = FragmentAdapter(this)
            val fragment1 = BAScheduledFragment()
            val fragment2 = BACompletedFragment()
            adapter.addFragment(fragment1)
            adapter.addFragment(fragment2)

            binding.viewPager2.adapter = adapter
            binding.viewPager2.isSaveEnabled = false

            binding.viewPager2.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        binding.tabSchedule.setBackgroundDrawable(ContextCompat.getDrawable(this@BAFollowupActivity, R.drawable.ba_tab_unselected_background));
                        binding.tabCompleted.setBackgroundDrawable(ContextCompat.getDrawable(this@BAFollowupActivity, R.drawable.tab_selected_background));
                        binding.txtSchedule.setTextColor(resources.getColor(R.color.white))
                        binding.txtComplete.setTextColor(resources.getColor(R.color.blue))

                    } else if (position == 1) {

                        binding.tabCompleted.setBackgroundDrawable(ContextCompat.getDrawable(this@BAFollowupActivity, R.drawable.ba_tab_unselected_background));
                        binding.tabSchedule.setBackgroundDrawable(ContextCompat.getDrawable(this@BAFollowupActivity, R.drawable.tab_selected_background));
                        binding.txtSchedule.setTextColor(resources.getColor(R.color.blue))
                        binding.txtComplete.setTextColor(resources.getColor(R.color.white))

                    }
                }
            })
        } catch (e: Exception) {

        }

    }

}