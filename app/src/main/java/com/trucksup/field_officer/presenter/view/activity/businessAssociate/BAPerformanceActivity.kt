package com.trucksup.field_officer.presenter.view.activity.businessAssociate

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.BaPerformanceActivityBinding
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.BAPerformanceAdapter

class BAPerformanceActivity : BaseActivity() {

    private lateinit var binding: BaPerformanceActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BaPerformanceActivityBinding.inflate(layoutInflater)
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
            layoutManager = LinearLayoutManager(this@BAPerformanceActivity, RecyclerView.VERTICAL, false)
            adapter = BAPerformanceAdapter(this@BAPerformanceActivity, list)
            hasFixedSize()
        }
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