package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.data.model.user.BoProfileDetails
import com.trucksup.field_officer.data.model.user.GetProfileRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileRequest
import com.trucksup.field_officer.databinding.ActivityMyProfileBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.AppVersionUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.profile.vml.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityMyProfileBinding
    private var mViewModel: EditProfileViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]

        getProfile()
        setListener()
        setupObserver()
    }

    private fun getProfile() {
        if (!PreferenceManager.getPhoneNo(this).isNullOrEmpty()) {
            val request = GetProfileRequest(
                requestDatetime = PreferenceManager.getServerDateUtc(),
                requestedBy = PreferenceManager.getPhoneNo(this),
                userId = PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0,
                mobileNo = PreferenceManager.getPhoneNo(this),
                requestId = PreferenceManager.getRequestNo().toInt()
            )
            mViewModel?.getProfile(PreferenceManager.getAuthToken(), request)
        }

    }

    private fun setListener() {

        binding.version.text = "Version ${AppVersionUtils.getAppVersionName(this)}"

        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        //submit button
        binding.btnSubmit.setOnClickListener {

            val request = UpdateProfileRequest(
                address = binding.editAddress.text.toString(),
                city = binding.editCity.text.toString(),
                email = binding.editEmail.text.toString(),
                mobileNo = binding.editPhone.text.toString(),
                pincode = binding.editPincode.text.toString(),
                profilephoto = "",
                requestDatetime = "",
                requestId = 0,
                requestedBy = "",
                state = "",
                userId = 0
            )
            mViewModel?.updateProfile(PreferenceManager.getAuthToken(), request)
        }

        //close button
        binding.btnClose.setOnClickListener {
            onBackPressed()
        }

        //camera or photo button
        binding.ivProfile.setOnClickListener {
            Toast.makeText(this@EditProfileActivity, "Edit Profile", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupObserver() {
        mViewModel?.resultGetProfileLD?.observe(this@EditProfileActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@EditProfileActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {
                    updateUI(responseModel.success?.boProfileDetails)
                   } else {
                       val abx = AlertBoxDialog(
                           this@EditProfileActivity,
                           responseModel.success?.message.toString(),
                           "m"
                       )
                       abx.show()
                   }

            }
        }

        mViewModel?.resultUpdateProfileLD?.observe(this@EditProfileActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@EditProfileActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {

                } else {
                    val abx = AlertBoxDialog(
                        this@EditProfileActivity,
                        responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }

            }
        }
    }

    private fun updateUI(boProfileDetails: BoProfileDetails?) {
        binding.tvUserName.text = boProfileDetails?.profilename
        binding.tvMobileNo.text = boProfileDetails?.mobilenumber
        binding.tvReferralCode.text = boProfileDetails?.referralcode

        //binding.ivProfile.setimage = boProfileDetails?.referralcode
    }
}