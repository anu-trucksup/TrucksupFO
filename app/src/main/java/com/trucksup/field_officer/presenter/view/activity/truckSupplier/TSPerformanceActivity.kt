package com.trucksup.field_officer.presenter.view.activity.truckSupplier

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.TsPerformanceActivityBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.chipData
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.LoanDataSubmitRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.ScheduleMeetTSRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.TSScheduleMeetingVM
import com.trucksup.field_officer.presenter.view.adapter.TSPerformanceAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TSPerformanceActivity : BaseActivity() {

    private lateinit var binding: TsPerformanceActivityBinding
    private var mViewModel: TSScheduleMeetingVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TsPerformanceActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this)[TSScheduleMeetingVM::class.java]


        setupObserver()
        setOnListeners()
        setRvList()
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
    }

    private fun setRvList() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        binding.rv.layoutManager = LinearLayoutManager(this)
        val adapter = TSPerformanceAdapter(this@TSPerformanceActivity, list)
       /* binding.rv.apply {
            layoutManager = LinearLayoutManager(this@TSPerformanceActivity, RecyclerView.VERTICAL, false)
            adapter = TSPerformanceAdapter(
                this@TSPerformanceActivity,
                list)
        }*/

        adapter.setOnItemClickListener(object : TSPerformanceAdapter.OnItemClickListener {
            override fun onItemClick(selectedDate: String, selectedTime: String) {
                dataSubmit(selectedDate, selectedTime)
                //Toast.makeText(this@TSPerformanceActivity, "Clicked item at position $selectedTime", Toast.LENGTH_SHORT).show()
            }
        })
        binding.rv.adapter = adapter
    }

    fun dataSubmit(selectedDate: String, selectedTime : String) {
        val request =
            ScheduleMeetTSRequest(
                1,
                "test",
                "12.33",
                "22.33",
                "8527257606",
                PreferenceManager.getProfileType(this),
                PreferenceManager.getServerDateUtc(),
                1,
                "8527257606",
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