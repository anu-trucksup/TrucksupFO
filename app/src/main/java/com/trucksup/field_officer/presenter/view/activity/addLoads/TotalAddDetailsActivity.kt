package com.trucksup.field_officer.presenter.view.activity.addLoads

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.databinding.ActivityTotaladdDetailsBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.TotalLoadsAdapter2

class TotalAddDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityTotaladdDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTotaladdDetailsBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        setRecycleViewList()

        setListeners()
    }

    private fun setRecycleViewList() {
        var list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        binding.rv.apply {
            adapter = TotalLoadsAdapter2(this@TotalAddDetailsActivity, list)
            layoutManager =
                LinearLayoutManager(this@TotalAddDetailsActivity, RecyclerView.VERTICAL, false)
            hasFixedSize()
        }
    }

    private fun setListeners() {
        //back
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

}