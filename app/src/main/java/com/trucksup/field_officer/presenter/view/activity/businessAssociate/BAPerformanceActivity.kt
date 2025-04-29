package com.trucksup.field_officer.presenter.view.activity.businessAssociate

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.trucksup.field_officer.databinding.BaPerformanceActivityBinding
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.dialog.OnFilterValueInputListener
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllBADetailsResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.ScheduleMeetingBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAScheduleMeetingVM
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllTSDetailsRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllTSDetailsResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.ScheduleMeetTSRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.TSScheduleMeetingVM
import com.trucksup.field_officer.presenter.view.adapter.BAPerformanceAdapter
import com.trucksup.field_officer.presenter.view.adapter.TSPerformanceAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.jar.Manifest

@AndroidEntryPoint
class BAPerformanceActivity : BaseActivity() {

    private lateinit var binding: BaPerformanceActivityBinding
    private var mViewModel: BAScheduleMeetingVM? = null
    private var getBadetails: java.util.ArrayList<GetAllBADetailsResponse.GetBAdetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BaPerformanceActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this)[BAScheduleMeetingVM::class.java]


        getAllBADetails()
        setupObserver()
        setOnListeners()
    }

    private fun setupObserver() {
        mViewModel?.onScheduleMeetingBAResponseLD?.observe(this@BAPerformanceActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()
                val abx = AlertBoxDialog(
                    this@BAPerformanceActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.message != null) {
                    val abx = AlertBoxDialog(
                        this@BAPerformanceActivity,
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

        mViewModel?.rresultGetBAScheduleMeetingDataLD?.observe(this) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()
                val abx = AlertBoxDialog(this, responseModel.serverError.toString(), "m")
                abx.show()
            } else {
                dismissProgressDialog()
                if (responseModel.success != null) {
                    getBadetails.clear()
                    if (responseModel.success.statuscode==200) {
                        getBADetailsDataSuccess(responseModel.success)
                    }
                    else
                    {
                        val abx = AlertBoxDialog(
                            this@BAPerformanceActivity,
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

    private fun getBADetailsDataSuccess(baDetailsGetResponse: GetAllBADetailsResponse) {
        if(baDetailsGetResponse.getBADetails != null &&
            baDetailsGetResponse.getBADetails.size > 0){
            binding.noData.visibility = View.GONE
            println("Dsdksd=="+baDetailsGetResponse.getBADetails.size)
            baDetailsGetResponse.getBADetails?.forEachIndexed { _, getBADetailsData ->
                run {
                    getBadetails.add(getBADetailsData)
                }
            }
        }else{
            binding.noData.visibility = View.VISIBLE
        }



        binding.rv.layoutManager = LinearLayoutManager(this)
        val adapter = BAPerformanceAdapter(this@BAPerformanceActivity, getBadetails)

        adapter.setOnItemClickListener(object : BAPerformanceAdapter.OnItemClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemClick(ownerName: String, selectedDate: String, selectedTime: String) {
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

    private fun getAllBADetails() {
        showProgressDialog(this,false)
        val request = GetAllTSDetailsRequest(
            PreferenceManager.getRequestNo().toInt(),
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getServerDateUtc(),
            "Ghaziabad",
            /*PreferenceManager.getUserData(this)?.city.toString(),*/
            /*PreferenceManager.getPhoneNo(this)*/"8881236353"
        )
        mViewModel?.getAllBADetails(request)
    }

    private fun setRvList() {
        /*val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        binding.rv.layoutManager = LinearLayoutManager(this)
        val adapter = BAPerformanceAdapter(this@BAPerformanceActivity, list)
        adapter.setOnItemClickListener(object : BAPerformanceAdapter.OnItemClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemClick(selectedDate: String, selectedTime: String) {
                dataSubmit(selectedDate, selectedTime)
                //Toast.makeText(this@TSPerformanceActivity, "Clicked item at position $selectedTime", Toast.LENGTH_SHORT).show()
            }
        })
        binding.rv.adapter = adapter*/
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dataSubmit(ownerName: String, selectedDate: String, selectedTime : String) {
        val firstApiFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("21-04-2025" , firstApiFormat)
        val request =
            ScheduleMeetingBARequest(
                PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0,
                ownerName,
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
        request?.let { mViewModel?.onScheduleMeetingBA(it) }
    }

    private fun setOnListeners() {
        binding.imgViewAll.setOnClickListener {
            startActivity(Intent(this, BAStatusViewAllActivity::class.java))
        }

        //filter
        binding.imgFilter.setOnClickListener {
            DialogBoxes.setFilters(this, "owner", object :
                OnFilterValueInputListener {
                override fun onInput(kycStatus: String, visitType: String) {
                    setupObserver()
                    binding.llCancel.visibility = View.VISIBLE
                }
            })
            //DialogBoxes.setFilter(this@BAPerformanceActivity, "owner")
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.llCancel.setOnClickListener{
            setupObserver()
            binding.llCancel.visibility = View.GONE
        }
    }

    private fun dateFilterDialog() {
        val builder = AlertDialog.Builder(this@BAPerformanceActivity)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(this@BAPerformanceActivity))
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