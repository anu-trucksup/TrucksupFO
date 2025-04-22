package com.trucksup.field_officer.presenter.view.activity.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.user.BoProfileDetails
import com.trucksup.field_officer.data.model.user.GetProfileRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileRequest
import com.trucksup.field_officer.databinding.ActivityMyProfileBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.AppVersionUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.CommonApplication
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.profile.vml.EditProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityMyProfileBinding
    private var mViewModel: EditProfileViewModel? = null
    private val profileImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]

        getProfile()
        setListener()
        setupObserver()
        setPinCode()
    }

    private fun setPinCode() {

        binding.eTPincode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.eTPincode.text.length == 6) {
                    showProgressDialog(this@EditProfileActivity, false)
                    val request = PinCodeRequest(
                        binding.eTPincode.text.toString(),
                        PreferenceManager.getRequestNo(),
                        PreferenceManager.getPhoneNo(this@EditProfileActivity)
                    )
                    mViewModel?.getCityStateByPin(PreferenceManager.getAuthToken(), request)

                } else {
                    setUI()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

    }

    private fun getProfile() {
        showProgressDialog(this, false)
        if (!PreferenceManager.getPhoneNo(this).isNullOrEmpty()) {
            val request = GetProfileRequest(
                requestDatetime = PreferenceManager.getServerDateUtc(),
                requestedBy = PreferenceManager.getPhoneNo(this),
                boUserid = PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0,
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

            if (binding.eTAlterMobile.text.toString().trim()
                    .isNotEmpty() && !isValidMobile(binding.eTAlterMobile.text.toString().trim())
            ) {
                binding.eTAlterMobile.requestFocus()
                binding.eTAlterMobile.error = getString(R.string.mobile_no_validation)
                return@setOnClickListener
            }
            if (binding.eTEmail.text.toString().trim()
                    .isNotEmpty() && !isValidEmail(binding.eTEmail.text.toString().trim())
            ) {
                binding.eTEmail.requestFocus()
                binding.eTEmail.error = resources.getString(R.string.valid_email)
                return@setOnClickListener
            }


            if (binding.eTPincode.text.toString().trim()
                    .isNotEmpty() && binding.eTPincode.text.length < 6
            ) {
                binding.eTPincode.requestFocus()
                binding.eTPincode.error = getString(R.string.PleaseEnterRightPincode)
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(
                    binding.eTAlterMobile?.text.toString().trim()
                ) && TextUtils.isEmpty(binding.eTAddress?.text.toString().trim()) &&
                TextUtils.isEmpty(binding.eTEmail?.text.toString().trim()) && TextUtils.isEmpty(
                    binding.eTPincode?.text.toString().trim()
                )
                && TextUtils.isEmpty(binding.editCity?.text.toString().trim())
            ) {
                val abx = AlertBoxDialog(
                    this@EditProfileActivity,
                    "Please fill atleast one field", "m"
                )
                abx.show()

                return@setOnClickListener
            } else {

            }
            showProgressDialog(this, false)
            val request = UpdateProfileRequest(
                address = binding.eTAddress.text.toString(),
                city = binding.editCity.text.toString(),
                email = binding.eTEmail.text.toString(),
                mobileNo = PreferenceManager.getPhoneNo(this),
                pincode = binding.eTPincode.text.toString(),
                profilephoto = "",
                requestDatetime = PreferenceManager.getServerDateUtc(),
                requestId = PreferenceManager.getRequestNo().toInt(),
                requestedBy = PreferenceManager.getPhoneNo(this),
                state = binding.editState.text.toString()
            )
            mViewModel?.updateProfile(PreferenceManager.getAuthToken(), request)
        }

        //close button
        binding.btnClose.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupObserver() {
        mViewModel?.resultGetProfileLD?.observe(this@EditProfileActivity) { responseModel ->     // login function observe
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
                    updateUI(responseModel.success.boProfileDetails)
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
                    responseModel.serverError.toString(), "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {
                    val abx = AlertBoxDialog(
                        this@EditProfileActivity,
                        responseModel.success.message.toString(),
                        "profile"
                    )
                    abx.show()
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

        mViewModel?.resultSCbyPinCodeLD?.observe(this@EditProfileActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@EditProfileActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statusCode == 200) {
                    if (responseModel.success.data.isNotEmpty()) {
                        pinData(
                            responseModel.success.data[0].district,
                            responseModel.success.data[0].stateName,
                        )
                    } else {
                        val abx = AlertBoxDialog(
                            this@EditProfileActivity,
                            responseModel.success.message,
                            "m"
                        )
                        abx.show()

                        setUI()

                        binding.eTPincode.setText("")
                    }
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

    private fun setUI() {
        binding.editCity.setText("")
        binding.editState.setText("")
    }

    private fun updateUI(boProfileDetails: BoProfileDetails?) {
        binding.tvUserName.text = boProfileDetails?.profilename ?: "-"
        binding.tvMobileNo.text = boProfileDetails?.mobilenumber ?: "-"
        binding.tvReferralCode.text = "Referral Code : " + boProfileDetails?.referralcode ?: "-"

        if (!boProfileDetails?.profilephoto.isNullOrEmpty()) {
            binding.imageButton.visibility = View.INVISIBLE

            Glide.with(this)
                .load(CommonApplication.imagePathUrl + boProfileDetails?.profilephoto + "&Position=1")
                .into(binding.ivProfile)
        }

    }

    private fun isValidEmail(target: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun pinData(city: String, state: String) {
        dismissProgressDialog()

        binding.editCity.setText(city)
        binding.editState.setText(state)

    }
}