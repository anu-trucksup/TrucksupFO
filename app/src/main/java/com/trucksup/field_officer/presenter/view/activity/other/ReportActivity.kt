package com.trucksup.field_officer.presenter.view.activity.other

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.ActivityReportBinding
import com.trucksup.fieldofficer.adapter.TotalLoadsAddedAdap

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()

        setTotalLoadAddedList()

        setSubPlansList()
    }

    private fun setListeners() {
        //back button
        binding.backButton.setOnClickListener {
            finish()
        }

        //share button
        binding.btnShare.setOnClickListener {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
        }

        //excel button
        binding.btnExcel.setOnClickListener {
            Toast.makeText(this, "Excel", Toast.LENGTH_SHORT).show()
        }

        //calender button
        binding.btnCalender.setOnClickListener {
            Toast.makeText(this, "Calender", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setTotalLoadAddedList() {
        var list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        binding.rvTLD.apply {
            layoutManager = GridLayoutManager(this@ReportActivity, 3, RecyclerView.VERTICAL, false)
            adapter = TotalLoadsAddedAdap(this@ReportActivity, list)
            hasFixedSize()
        }
    }

    private fun setSubPlansList() {
        var list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        binding.rvSP.apply {
            layoutManager = GridLayoutManager(this@ReportActivity, 3, RecyclerView.VERTICAL, false)
            adapter = TotalLoadsAddedAdap(this@ReportActivity, list)
            hasFixedSize()
        }
    }

}