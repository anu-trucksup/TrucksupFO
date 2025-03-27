package com.trucksup.field_officer.presenter.view.activity.growth_partner

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
import com.trucksup.field_officer.databinding.GpPerformanceActivityBinding
import com.trucksup.field_officer.databinding.TsFollowupActivityBinding
import com.trucksup.field_officer.databinding.TsPerformanceActivityBinding
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.GPPerformanceAdapter
import com.trucksup.field_officer.presenter.view.adapter.TSScheduleFollowupAdapter
import com.trucksup.field_officer.presenter.view.fragment.ts.TSCompletedFragment
import com.trucksup.field_officer.presenter.view.fragment.ts.TSScheduledFragment
import com.trucksup.fieldofficer.adapter.FragmentAdapter

class GPPerformanceActivity : BaseActivity() {

    private lateinit var binding: GpPerformanceActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GpPerformanceActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        setRvList()

        setOnListeners()
    }

    private fun setRvList() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        binding.rv.apply {
            layoutManager =
                LinearLayoutManager(this@GPPerformanceActivity, RecyclerView.VERTICAL, false)
            adapter = GPPerformanceAdapter(this@GPPerformanceActivity, list)
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