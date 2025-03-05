package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.trucksup.field_officer.databinding.ActivityTotalAddLoadBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.adapter.ServicesMainAdapter

class TotalAddLoadScreen : BaseActivity() {

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

        binding.rvTotalLoads.apply {
            layoutManager = LinearLayoutManager(this@TotalAddLoadScreen)
            adapter = ServicesMainAdapter(this@TotalAddLoadScreen)
            hasFixedSize()
        }
    }
}