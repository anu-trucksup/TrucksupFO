package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.databinding.ActivityMyTeamsBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class MyTeamScreen : BaseActivity() {
    private lateinit var binding: ActivityMyTeamsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTeamsBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

    }
}