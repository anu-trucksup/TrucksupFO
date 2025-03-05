package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import com.trucksup.field_officer.databinding.ActivityMyEarningBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.MyEarningAdapter

class MyEarningActivity : BaseActivity() {

    private lateinit var binding: ActivityMyEarningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyEarningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setlistofEarn()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setlistofEarn() {

        binding.rvTotalEarn.apply {
            layoutManager = GridLayoutManager(this@MyEarningActivity,2)
            adapter = MyEarningAdapter(this@MyEarningActivity)
            hasFixedSize()
        }
    }
}