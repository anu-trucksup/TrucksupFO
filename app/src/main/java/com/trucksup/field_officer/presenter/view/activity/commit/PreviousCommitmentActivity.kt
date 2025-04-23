package com.trucksup.field_officer.presenter.view.activity.commit

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.databinding.ActivityTotalAddLoadBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.TotalLoadsAdapter

class PreviousCommitmentActivity : BaseActivity() {

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
            layoutManager = LinearLayoutManager(this@PreviousCommitmentActivity)
            adapter = AllCommitAdapter(this@PreviousCommitmentActivity,list)
            hasFixedSize()
        }
    }
}