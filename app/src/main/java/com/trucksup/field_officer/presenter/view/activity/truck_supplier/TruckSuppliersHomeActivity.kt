package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.TruckSuppliersHomeActivityBinding
import com.trucksup.field_officer.databinding.TsFollowupActivityBinding
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.TSHomeAdapter
import com.trucksup.field_officer.presenter.view.adapter.TSScheduleFollowupAdapter
import com.trucksup.field_officer.presenter.view.fragment.ts.TSCompletedFragment
import com.trucksup.field_officer.presenter.view.fragment.ts.TSScheduledFragment
import com.trucksup.fieldofficer.adapter.FragmentAdapter

class TruckSuppliersHomeActivity : BaseActivity() {

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
            layoutManager = LinearLayoutManager(this@TruckSuppliersHomeActivity, RecyclerView.VERTICAL, false)
            adapter = TSHomeAdapter(this@TruckSuppliersHomeActivity, list)
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
            DialogBoxes.setFilter(this@TruckSuppliersHomeActivity, "owner")
        }
    }

    private fun dateFilterDialog() {
        val builder = AlertDialog.Builder(this@TruckSuppliersHomeActivity)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(this@TruckSuppliersHomeActivity))
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