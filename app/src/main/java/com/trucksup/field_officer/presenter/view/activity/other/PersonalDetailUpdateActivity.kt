package com.trucksup.field_officer.presenter.view.activity.other

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityPersonalDetailUpdateBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class PersonalDetailUpdateActivity : BaseActivity() {
    private lateinit var binding: ActivityPersonalDetailUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_detail_update)
        val view = binding.root
        setContentView(view)
    }
}