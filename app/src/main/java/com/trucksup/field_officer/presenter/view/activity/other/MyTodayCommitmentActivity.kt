package com.trucksup.field_officer.presenter.view.activity.other

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityTodayCommintmentBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.profile.vml.MyTargetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTodayCommitmentActivity : BaseActivity() {
    private lateinit var binding: ActivityTodayCommintmentBinding
    private var flag: Boolean = false
    private var mViewModel: MyTargetViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTodayCommintmentBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[MyTargetViewModel::class.java]

        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
            if (flag == true) {
                flag = false
                binding.navTitle.setText(getString(R.string.target))
                /* binding.tvAddload.setText("20")
                 binding.tvTs.setText("20")
                 binding.tvBa.setText("20")
                 binding.tvGp.setText("20")
                 binding.tvVehicleTracking.setText("20")
                 binding.tvVehicleVerify.setText("20")
                 binding.tvVerifyDl.setText("20")
                 binding.tvInsurance.setText("20")
                 binding.tvFinance.setText("20")
                 binding.tvSmartFuel.setText("20")*/
            } else {
                onBackPressed()
            }
        }

        binding.tvTargetAchieved.setOnClickListener {

        }

    }

}