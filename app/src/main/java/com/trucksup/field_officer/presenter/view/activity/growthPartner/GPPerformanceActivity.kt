package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.GpPerformanceActivityBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.ScheduleMeetingGPRequest
import com.trucksup.field_officer.presenter.view.activity.growthPartner.vml.GPScheduleMeetingVM
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.ScheduleMeetTSRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.TSScheduleMeetingVM
import com.trucksup.field_officer.presenter.view.adapter.GPPerformanceAdapter
import com.trucksup.field_officer.presenter.view.adapter.TSPerformanceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GPPerformanceActivity : BaseActivity() {

    private lateinit var binding: GpPerformanceActivityBinding
    private var mViewModel: GPScheduleMeetingVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GpPerformanceActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[GPScheduleMeetingVM::class.java]

        setRvList()
        setupObserver()
        setOnListeners()
    }

    private fun setRvList() {
        val list = ArrayList<String>()
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
        binding.rv.adapter = adapter
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
    }
    fun dataSubmit(selectedDate: String, selectedTime : String) {
        val request =
            ScheduleMeetingGPRequest(
                1,
                "test",
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
        request?.let { mViewModel?.onScheduleMeetingGP(it) }
    }
    private fun setOnListeners() {

        binding.imgViewAll.setOnClickListener {
            startActivity(Intent(this, GPViewAllActivity::class.java))
        }

        //filter
        binding.imgFilter.setOnClickListener {
            DialogBoxes.setFilter(this@GPPerformanceActivity, "owner")
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun dateFilterDialog() {
        val builder = AlertDialog.Builder(this@GPPerformanceActivity)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(this@GPPerformanceActivity))
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