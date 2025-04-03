package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityMyTargetBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class MyTargetScreen : BaseActivity() {
    private lateinit var binding: ActivityMyTargetBinding
    private var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyTargetBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        //back button
        binding.ivBack.setOnClickListener {
            if(flag == true){
                flag = false
                binding.navTitle.setText(getString(R.string.target))
                binding.tvAddload.setText("20")
                binding.tvTs.setText("20")
                binding.tvBa.setText("20")
                binding.tvGp.setText("20")
                binding.tvVehicleTracking.setText("20")
                binding.tvVehicleVerify.setText("20")
                binding.tvVerifyDl.setText("20")
                binding.tvInsurance.setText("20")
                binding.tvFinance.setText("20")
                binding.tvSmartFuel.setText("20")
            }else{
                onBackPressed()
            }
        }

        binding.tvTargetAchieved.setOnClickListener{
            flag = true
            binding.navTitle.setText(getString(R.string.target_achieved))
            binding.tvAddload.setText("10/20")
            binding.tvTs.setText("10/20")
            binding.tvBa.setText("10/20")
            binding.tvGp.setText("10/20")
            binding.tvVehicleTracking.setText("10/20")
            binding.tvVehicleVerify.setText("10/20")
            binding.tvVerifyDl.setText("10/20")
            binding.tvInsurance.setText("10/20")
            binding.tvFinance.setText("10/20")
            binding.tvSmartFuel.setText("10/20")
        }

        binding.tvTargetPending.setOnClickListener{
            flag = true
            binding.navTitle.setText(getString(R.string.target_pending))
            binding.tvAddload.setText("15/20")
            binding.tvTs.setText("15/20")
            binding.tvBa.setText("15/20")
            binding.tvGp.setText("15/20")
            binding.tvVehicleTracking.setText("15/20")
            binding.tvVehicleVerify.setText("15/20")
            binding.tvVerifyDl.setText("15/20")
            binding.tvInsurance.setText("15/20")
            binding.tvFinance.setText("15/20")
            binding.tvSmartFuel.setText("15/20")
        }
    }

    override fun onBackPressed() {
        if(flag == true){
            flag = false
            binding.navTitle.setText(getString(R.string.target))
            binding.tvAddload.setText("20")
            binding.tvTs.setText("20")
            binding.tvBa.setText("20")
            binding.tvGp.setText("20")
            binding.tvVehicleTracking.setText("20")
            binding.tvVehicleVerify.setText("20")
            binding.tvVerifyDl.setText("20")
            binding.tvInsurance.setText("20")
            binding.tvFinance.setText("20")
            binding.tvSmartFuel.setText("20")
        }else{
            super.onBackPressed()
        }

    }
}