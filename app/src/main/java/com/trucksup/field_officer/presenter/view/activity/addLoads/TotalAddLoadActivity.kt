package com.trucksup.field_officer.presenter.view.activity.addLoads

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.databinding.ActivityTotalAddLoadBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.TotalLoadsAdapter

class TotalAddLoadActivity : BaseActivity() {

    private lateinit var binding: ActivityTotalAddLoadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTotalAddLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setlistofLoad()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setlistofLoad() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")

        binding.rvTotalLoads.apply {
            layoutManager = LinearLayoutManager(this@TotalAddLoadActivity)
            adapter = TotalLoadsAdapter(this@TotalAddLoadActivity,list)
            hasFixedSize()
        }
    }
}