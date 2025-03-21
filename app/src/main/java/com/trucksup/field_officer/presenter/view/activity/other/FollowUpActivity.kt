package com.trucksup.field_officer.presenter.view.activity.other

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import com.trucksup.field_officer.databinding.ActivityFollowUpBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.HomeFeaturesAdapter

class FollowUpActivity : BaseActivity() {
    private lateinit var binding: ActivityFollowUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFollowUpBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        setSecondRvList()
    }

    private fun setSecondRvList() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        binding.rvSecondServices.apply {
            layoutManager = GridLayoutManager(this@FollowUpActivity, 3)
            adapter = HomeFeaturesAdapter(this@FollowUpActivity, list)
            hasFixedSize()
        }
    }
}