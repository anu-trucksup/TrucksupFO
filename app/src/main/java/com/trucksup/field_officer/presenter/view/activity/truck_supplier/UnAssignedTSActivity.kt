package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.TruckSuppliersHomeActivityBinding
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.TSHomeAdapter

class UnAssignedTSActivity : BaseActivity() {

    private lateinit var binding: TruckSuppliersHomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TruckSuppliersHomeActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        setListener()
        setRvList()
        setOnListeners()
    }

    private fun setListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setRvList() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@UnAssignedTSActivity, RecyclerView.VERTICAL, false)
            adapter = TSHomeAdapter(this@UnAssignedTSActivity, list)
            hasFixedSize()
        }
    }

    private fun setOnListeners() {
        //date picker
        binding.imgCalender.setOnClickListener {
            dateFilterDialog()
        }

        //filter
        binding.imgFilter.setOnClickListener {
            DialogBoxes.setFilter(this@UnAssignedTSActivity, "owner")
        }
    }

    private fun dateFilterDialog() {
        val builder = AlertDialog.Builder(this@UnAssignedTSActivity)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(this@UnAssignedTSActivity))
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