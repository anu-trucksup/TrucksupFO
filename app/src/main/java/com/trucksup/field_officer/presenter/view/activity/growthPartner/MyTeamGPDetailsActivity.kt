package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.ActivityMyTeamGpdetailsBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.MyTeamGPDetailsAdapter

class MyTeamGPDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityMyTeamGpdetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyTeamGpdetailsBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        setRvList()

    }

    private fun setRvList() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")

        binding.rvFinance.apply {
            layoutManager = LinearLayoutManager(this@MyTeamGPDetailsActivity, RecyclerView.VERTICAL, false)
            adapter = MyTeamGPDetailsAdapter(this@MyTeamGPDetailsActivity, list)
            hasFixedSize()
        }
    }

    private fun setOnListeners() {

       /* binding.imgViewAll.setOnClickListener {
            startActivity(Intent(this, GPViewAllActivity::class.java))
        }

        //filter
        binding.imgFilter.setOnClickListener {
            DialogBoxes.setFilter(this@GPPerformanceActivity, "owner")
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }*/
    }
}