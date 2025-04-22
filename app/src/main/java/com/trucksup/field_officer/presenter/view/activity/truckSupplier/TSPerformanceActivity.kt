package com.trucksup.field_officer.presenter.view.activity.truckSupplier

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.logistics.trucksup.activities.preferre.modle.GetMeetScheduleDetailsRequest
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.TsPerformanceActivityBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.ScheduleMeetTSRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetMeetScheduleDetailsResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.TSScheduleMeetingVM
import com.trucksup.field_officer.presenter.view.adapter.TSPerformanceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TSPerformanceActivity : BaseActivity() {

    private lateinit var binding: TsPerformanceActivityBinding
    private var mViewModel: TSScheduleMeetingVM? = null
    private var getTsdetails: java.util.ArrayList<GetMeetScheduleDetailsResponse.GetTsdetails> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TsPerformanceActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this)[TSScheduleMeetingVM::class.java]

        getTSMeetScheduleData()
        setupObserver()
        setOnListeners()
    }

    private fun setupObserver() {

        mViewModel?.resultsubmitTSScheduleMeetingLD?.observe(this@TSPerformanceActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@TSPerformanceActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.message != null) {
                    val abx = AlertBoxDialog(
                        this@TSPerformanceActivity,
                        responseModel.success.message,
                        "m"
                    )
                    abx.show()
                   /* updateData(
                        responseModel.success.message.toString(),
                        responseModel.success.message1
                    )*/

                } else {
                }
            }
        }

        mViewModel?.rresultGetTSScheduleMeetingDataLD?.observe(this) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()
                val abx = AlertBoxDialog(this, responseModel.serverError.toString(), "m")
                abx.show()
            } else {
                dismissProgressDialog()
                if (responseModel.success != null) {
                    if (responseModel.success.statuscode==200) {
                        getTSDetailsDataSuccess(responseModel.success)
                    }
                    else
                    {
                        val abx = AlertBoxDialog(
                            this@TSPerformanceActivity,
                            responseModel.success.message,
                            "finishActivity"
                        )
                        abx.show()

                    }
                }
                else
                {

                }
            }
        }
    }

    private fun getTSDetailsDataSuccess(tSDetailsGetResponse: GetMeetScheduleDetailsResponse) {
        tSDetailsGetResponse?.getTSDetails?.forEachIndexed { _, getTSDetailsData ->
            run {
                getTsdetails.add(getTSDetailsData)
            }
        }


        binding.rv.layoutManager = LinearLayoutManager(this)
        val adapter = TSPerformanceAdapter(this@TSPerformanceActivity, getTsdetails)

        adapter.setOnItemClickListener(object : TSPerformanceAdapter.OnItemClickListener {
            override fun onItemClick(selectedDate: String, selectedTime: String) {
                dataSubmit(selectedDate, selectedTime)
                //Toast.makeText(this@TSPerformanceActivity, "Clicked item at position $selectedTime", Toast.LENGTH_SHORT).show()
            }
        })
        binding.rv.adapter = adapter

        // Add search or filter input
        binding.etSearchFillter.addTextChangedListener {
            adapter.filter(it.toString())
        }

        //binding.tvTotalEnquiry.text = "${inquiryHistoryResponse?.leadsHistory?.size} Enquiries"
        //setupViewPager()
    }

    private fun getTSMeetScheduleData() {
        println("wwww=="+PreferenceManager.getUserData(this)?.boUserid)
        showProgressDialog(this,false)
        val request = GetMeetScheduleDetailsRequest(
            1,
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getServerDateUtc(),
            PreferenceManager.getUserData(this)?.city.toString(),
            PreferenceManager.getPhoneNo(this),
        )
        mViewModel?.getTSMeetScheduleData(request)
    }

    fun dataSubmit(selectedDate: String, selectedTime : String) {
        val request =
            ScheduleMeetTSRequest(
                PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0,
                "test",
                "12.33",
                "22.33",
                PreferenceManager.getPhoneNo(this),
                PreferenceManager.getProfileType(this),
                PreferenceManager.getServerDateUtc(),
                PreferenceManager.getRequestNo().toInt(),
                PreferenceManager.getPhoneNo(this),
                selectedDate,
                selectedTime,
            )


        showProgressDialog(this, false)
        request?.let { mViewModel?.SubmitTSScheduleMeetTSData(it) }
    }

    private fun setOnListeners() {
        //date picker
        binding.imgViewAll.setOnClickListener {
            startActivity(Intent(this, TSViewAllActivity::class.java))
        }

        //filter
        binding.imgFilter.setOnClickListener {
            DialogBoxes.setFilter(this@TSPerformanceActivity, "owner")
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun dateFilterDialog() {
        val builder = AlertDialog.Builder(this@TSPerformanceActivity)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(this@TSPerformanceActivity))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        //apply button
        binding.btnApply.setOnClickListener {
            dialog.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }

}