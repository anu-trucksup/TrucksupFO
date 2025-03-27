package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.databinding.ActivityTotalAddLoadBinding
import com.trucksup.field_officer.databinding.ActivityTotalDownloadBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.ServicesMainAdapter
import com.trucksup.field_officer.presenter.view.adapter.TotalLoadsAdapter

class TotalDownloadsActivity : BaseActivity() {

    private lateinit var binding: ActivityTotalDownloadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTotalDownloadBinding.inflate(layoutInflater)
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
            layoutManager = LinearLayoutManager(this@TotalDownloadsActivity)
            adapter = TotalLoadsAdapter(this@TotalDownloadsActivity,list)
            hasFixedSize()
        }
    }
}