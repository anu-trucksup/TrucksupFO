package com.trucksup.field_officer.presenter.view.activity.businessAssociate

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.ScheduleMeetingBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAScheduleMeetingVM
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BaPerformanceActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this)[BAScheduleMeetingVM::class.java]

        setupObserver()
        setRvList()
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
    }

    private fun setRvList() {
        val list = ArrayList<String>()
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
        binding.rv.adapter = adapter
    }




    @RequiresApi(Build.VERSION_CODES.O)
    fun dataSubmit(selectedDate: String, selectedTime : String) {
        val firstApiFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse("21-04-2025" , firstApiFormat)
        val request =
            ScheduleMeetingBARequest(
                1,
                "test",
                "12.33",
                "22.33",
                "8527257606",
                PreferenceManager.getProfileType(this),
                PreferenceManager.getServerDateUtc(),
                576,
                "8527257606",
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
            DialogBoxes.setFilter(this@BAPerformanceActivity, "owner")
        }
        binding.ivBack.setOnClickListener {
            onBackPressed()
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