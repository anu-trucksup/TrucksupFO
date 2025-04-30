package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.app.AlertDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.databinding.AttendDialogLayoutBinding
import com.trucksup.field_officer.databinding.GpPerformanceActivityBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.dialog.OnFilterValueInputListener
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.GetAllGPDetailsResponse
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.ScheduleMeetingGPRequest
import com.trucksup.field_officer.presenter.view.activity.growthPartner.vml.GPScheduleMeetingVM
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllTSDetailsRequest
import com.trucksup.field_officer.presenter.view.adapter.GPPerformanceAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class GPPerformanceActivity : BaseActivity() {

    private lateinit var binding: GpPerformanceActivityBinding
    private var mViewModel: GPScheduleMeetingVM? = null
    private var getGpdetails: java.util.ArrayList<GetAllGPDetailsResponse.GetGPdetails> =
        arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GpPerformanceActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[GPScheduleMeetingVM::class.java]



        getAllGPDetails()
        setupObserver()
        setOnListeners()
    }

    private fun setRvList() {
        /*val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        binding.rv.layoutManager = LinearLayoutManager(this)
        val adapter = GPPerformanceAdapter(this@GPPerformanceActivity, list)

        adapter.setOnItemClickListener(object : GPPerformanceAdapter.OnItemClickListener {
            override fun onItemClick(selectedDate: String, selectedTime: String) {
                dataSubmit(selectedDate, selectedTime)
            }
        })
        binding.rv.adapter = adapter*/
    }

    private fun getAllGPDetails() {
        showProgressDialog(this, false)
        val request = GetAllTSDetailsRequest(
            PreferenceManager.getRequestNo().toInt(),
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getServerDateUtc(),
            "Ghaziabad",
            /*PreferenceManager.getUserData(this)?.city.toString(),*/
            /*PreferenceManager.getPhoneNo(this)*/"8881236353"
        )
        mViewModel?.getGpdetails(request)
    }

    private fun setupObserver() {

        mViewModel?.onScheduleMeetingGPResponseLD?.observe(this@GPPerformanceActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@GPPerformanceActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.message != null) {
                    val abx = AlertBoxDialog(
                        this@GPPerformanceActivity,
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

        mViewModel?.rresultGetGPScheduleMeetingDataLD?.observe(this) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()
                val abx = AlertBoxDialog(this, responseModel.serverError.toString(), "m")
                abx.show()
            } else {
                dismissProgressDialog()
                if (responseModel.success != null) {
                    getGpdetails.clear()
                    if (responseModel.success.statuscode == 200) {
                        getGPDetailsDataSuccess(responseModel.success)
                    } else {
                        val abx = AlertBoxDialog(
                            this@GPPerformanceActivity,
                            responseModel.success.message,
                            "finishActivity"
                        )
                        abx.show()

                    }
                } else {
                    val abx = AlertBoxDialog(
                        this@GPPerformanceActivity,
                        responseModel.success?.message ?: "",
                        "finishActivity"
                    )
                    abx.show()

                }
            }
        }
    }


    private fun getGPDetailsDataSuccess(gpDetailsGetResponse: GetAllGPDetailsResponse) {
        if (gpDetailsGetResponse.getGPDetails != null &&
            gpDetailsGetResponse.getGPDetails.size > 0
        ) {
            binding.noData.visibility = View.GONE
            gpDetailsGetResponse.getGPDetails?.forEachIndexed { _, getBADetailsData ->
                run {
                    getGpdetails.add(getBADetailsData)
                }
            }
        } else {
            binding.noData.visibility = View.VISIBLE
        }



        println("Dsdksd==" + getGpdetails.size)

        binding.rv.layoutManager = LinearLayoutManager(this)
        val adapter = GPPerformanceAdapter(this@GPPerformanceActivity, getGpdetails)

        adapter.setOnItemClickListener(object : GPPerformanceAdapter.OnItemClickListener {
            override fun onItemClick(
                ownerName: String,
                selectedDate: String,
                selectedTime: String,
            ) {
                //dataSubmit(ownerName, selectedDate, selectedTime)
                rescheduleDialog()
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

    fun dataSubmit(ownerName: String, selectedDate: String, selectedTime: String) {
        val request =
            ScheduleMeetingGPRequest(
                1,
                ownerName,
                "12.33",
                "22.33",
                "8527257606",
                PreferenceManager.getProfileType(this),
                PreferenceManager.getServerDateUtc(),
                654,
                "8527257606",
                selectedDate,
                selectedTime,
            )
        showProgressDialog(this, false)
        request.let { mViewModel?.onScheduleMeetingGP(it) }
    }

    private fun setOnListeners() {

        binding.imgViewAll.setOnClickListener {
            startActivity(Intent(this, GPViewAllActivity::class.java))
        }

        //filter
        binding.imgFilter.setOnClickListener {
            binding.imgFilter.setOnClickListener {
                DialogBoxes.setFilters(this, "owner", object :
                    OnFilterValueInputListener {
                    override fun onInput(kycStatus: String, visitType: String) {
                        setupObserver()
                        binding.llCancel.visibility = View.VISIBLE
                    }
                })
            }
            //DialogBoxes.setFilter(this@GPPerformanceActivity, "owner")
        }

        binding.llCancel.setOnClickListener {
            setupObserver()
            binding.llCancel.visibility = View.GONE
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    //test
    private fun rescheduleDialog() {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(this@GPPerformanceActivity)
        val binding =
            AttendDialogLayoutBinding.inflate(LayoutInflater.from(this@GPPerformanceActivity))
        builder.setView(binding.root)
        dialog = builder.create()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)

        binding.message.setText("The meeting is already scheduled with the TS name.")
        binding.confirm.setText("Reschedule")
        val calendar = Calendar.getInstance()
        val getCurrentDate = SimpleDateFormat("dd-MMM-yy")
        val getCurrentTime = SimpleDateFormat("hh:mm a")
        val currentDate = getCurrentDate.format(calendar.time)
        val currentTime = getCurrentTime.format(Date()).toString()
        val formattedTime = currentTime.replace("am", "AM").replace("pm", "PM");

        binding.tvDate.setText("Date: " + currentDate)
        binding.tvTime.setText("Time: " + formattedTime)

        //ok button
        binding.confirm.setOnClickListener {
        }

        //ok button
        binding.cancle.setOnClickListener {

        }

        dialog?.show()
    }
    //test

}