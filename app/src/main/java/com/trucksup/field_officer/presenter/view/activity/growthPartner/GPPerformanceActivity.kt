package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.databinding.GpPerformanceActivityBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.Utils
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

@AndroidEntryPoint
class GPPerformanceActivity : BaseActivity() {

    private lateinit var adapter: GPPerformanceAdapter
    private lateinit var binding: GpPerformanceActivityBinding
    private var mViewModel: GPScheduleMeetingVM? = null
    private var getGpdetails: java.util.ArrayList<GetAllGPDetailsResponse.GetGPdetails> =
        arrayListOf()


    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

                if (responseModel.success?.statuscode == 200) {
                    if (responseModel.success?.message != null) {
                        val abx = AlertBoxDialog(
                            this@GPPerformanceActivity,
                            responseModel.success.message,
                            "m"
                        )
                        abx.show()

                    }
                } else {
                    if (responseModel.success?.statuscode == 201) {
                        if (responseModel.success.date != null && responseModel.success.time != null) {
                            Utils.scheduleDialog(
                                this, responseModel.success.date, responseModel.success.time,
                                responseModel.success.message
                            ){
                                if(it){
                                    adapter.dateFilterDialog( "Test")
                                }
                            }
                        } else {
                            val abx = AlertBoxDialog(
                                this@GPPerformanceActivity,
                                responseModel.success.message,
                                "m"
                            )
                            abx.show()
                        }

                    } else {
                        val abx = AlertBoxDialog(
                            this@GPPerformanceActivity,
                            responseModel.success?.message ?: "",
                            "m"
                        )
                        abx.show()
                    }
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
        adapter = GPPerformanceAdapter(this@GPPerformanceActivity, getGpdetails)

        adapter.setOnItemClickListener(object : GPPerformanceAdapter.OnItemClickListener {
            override fun onItemClick(
                ownerName: String,
                selectedDate: String,
                selectedTime: String,
            ) {
                dataSubmit(ownerName, selectedDate, selectedTime)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

}