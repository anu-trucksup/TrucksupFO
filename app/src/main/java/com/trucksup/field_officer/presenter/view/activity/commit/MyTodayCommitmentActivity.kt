package com.trucksup.field_officer.presenter.view.activity.commit

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.home.HomeCountRequest
import com.trucksup.field_officer.data.model.home.HomeCountResponse
import com.trucksup.field_officer.databinding.ActivityTodayCommitmentBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.commit.vml.CommitmentViewModel
import com.trucksup.field_officer.presenter.view.activity.profile.vml.MyTargetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyTodayCommitmentActivity : BaseActivity() {
    private lateinit var binding: ActivityTodayCommitmentBinding
    private var flag: Boolean = false
    private var mViewModel: CommitmentViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTodayCommitmentBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[CommitmentViewModel::class.java]

        showProgressDialog(this, false)
        val request = HomeCountRequest(
            PreferenceManager.getServerDateUtc(),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0
        )
        mViewModel?.getTodayCommitList(request)

        setClickListeners()

        setupObserver()

    }

    private fun setClickListeners() {

        binding.btnSubmit.setOnClickListener {
            val intent = Intent(this@MyTodayCommitmentActivity, PreviousCommitmentActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }

        binding.tvPrevious.setOnClickListener {
            val intent = Intent(this@MyTodayCommitmentActivity, PreviousCommitmentActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }


        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
            if (flag == true) {
                flag = false
                binding.navTitle.text = getString(R.string.target)
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
    }

    private fun setupObserver() {
        mViewModel?.resultGetTodayCommitLD?.observe(this@MyTodayCommitmentActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@MyTodayCommitmentActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {
                    setItemList(responseModel.success)
                } else {
                    val abx =
                        AlertBoxDialog(
                            this@MyTodayCommitmentActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }

        mViewModel?.resultAddTodayCommitLD?.observe(this@MyTodayCommitmentActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@MyTodayCommitmentActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {
                    setItemList(responseModel.success)
                } else {
                    val abx =
                        AlertBoxDialog(
                            this@MyTodayCommitmentActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }
    }

    private fun setItemList(success: HomeCountResponse) {

    }

}