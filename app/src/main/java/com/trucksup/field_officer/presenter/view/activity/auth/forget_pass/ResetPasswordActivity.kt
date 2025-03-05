package com.trucksup.field_officer.presenter.view.activity.auth.forget_pass

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.CheckUserProfileResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.databinding.ActivityResetPasswordBinding
import com.trucksup.field_officer.presenter.common.Utils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity(), View.OnClickListener {
    var mBinding: ActivityResetPasswordBinding? = null
    private var mViewModel: ResetPasswordViewModel? = null
    var isEmailType: Boolean = true
    private var isSecrectOption = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)
        Utils.setStatusBarColorAndIcons(this)
        //click Listener
        mBinding?.topView?.ivBack?.setOnClickListener(this)
        mBinding?.otpUpBtn?.setOnClickListener(this)
        mBinding?.tvVerify?.setOnClickListener(this)

        mViewModel = ViewModelProvider(this).get(
            ResetPasswordViewModel::class.java
        )
        // mBinding?.setViewModel(mViewModel);
        setupObserver()

    }


    private fun setupObserver() {
        mViewModel!!.resultResetLD.observe(this@ResetPasswordActivity) { responseModel ->
            if (responseModel.networkError != null) {
                dismissProgressDialog()
                Utils.showToastDialog(
                    "networkError",
                    this, "Ok"
                )
            } else if (responseModel.serverResponseError != null) {
                dismissProgressDialog()

                if (responseModel.serverResponseError == "in.co.ksquaretech.backend.account.email.not.exist") {
                    Utils.showToastDialog(
                        "User not exist with this email.",
                        this, "Ok"
                    )
                } else {
                    Utils.showToastDialog(
                        responseModel.serverResponseError,
                        this, "Ok"
                    )
                }
            } else if (responseModel.genericError != null) {
                dismissProgressDialog()
                Utils.showToastDialog(
                    "genericError",
                    this, "Ok"
                )
            } else {
                dismissProgressDialog()

                val intent = Intent(
                    this@ResetPasswordActivity,
                    CreatePasswordActivity::class.java
                )

                intent.putExtra("phoneNo", mBinding?.phoneNoTxt?.text.toString())
                intent.putExtra("countryCode", "+91")

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
        }

        mViewModel!!.checkUserProfileLD.observe(
            this@ResetPasswordActivity
        ) { responseModel: ResponseModel<CheckUserProfileResponse> ->
            if (responseModel.networkError != null) {
                dismissProgressDialog()
                Utils.showToastDialog(
                    "Network Error",
                    this,
                    "OK"
                )
            } else if (responseModel.serverResponseError != null) {
                dismissProgressDialog()
                Utils.showToastDialog(
                    "ServerResponseError",
                    this,
                    "OK"
                )
            } else if (responseModel.genericError != null) {
                dismissProgressDialog()
                Utils.showToastDialog(
                    "genericError",
                    this,
                    "Ok"
                )
            } else {
                dismissProgressDialog()
                if (responseModel.success!!.payload != null) {
                    if (!isEmailType && mBinding?.phoneNoTxt?.text.toString() != null && mBinding?.phoneNoTxt?.text.toString().length > 0) {
                        showProgressDialog()

                        if (isSecrectOption) {
                            showProgressDialog()

                            // mViewModel.forgotPassword("", mBinding?.phoneNoTxt.getText().toString(), mBinding?.countyCodePicker.getSelectedCountryCodeWithPlus());
                            val intent =
                                Intent(
                                    this@ResetPasswordActivity,
                                    CreatePasswordActivity::class.java
                                )
                            intent.putExtra("isValidateQuestion", true)

                            intent.putExtra("phoneNo", mBinding?.phoneNoTxt?.text.toString())
                            intent.putExtra("countryCode", "+91")

                            dismissProgressDialog()
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()
                        } else {
                            showProgressDialog()
                            mViewModel!!.forgotPassword(
                                "",
                                mBinding?.phoneNoTxt?.text.toString(),
                                "+91"
                            )
                        }
                    } else {
                    }
                } else {
                    dismissProgressDialog()
                    //  Utils.showToastDialog(mViewModel.getLabel("com.skiptq.backend.invalid.user"),this, mViewModel.getLabel(AppConstant.AppLabelName.okButton));
                    Utils.showToastDialog(
                        "Success",
                        this,
                        "Ok"
                    )
                }
            }
        }
    }


    override fun onClick(view: View) {
        if (view.id == R.id.iv_back) {
            onBackPressed()
        } else if (view.id == R.id.tv_verify) {
            val intent = Intent(this@ResetPasswordActivity, CreatePasswordActivity::class.java)
            intent.putExtra("isValidateQuestion", true)
            intent.putExtra("phoneNo", mBinding?.phoneNoTxt?.text.toString())
            intent.putExtra("countryCode", "+91")

            dismissProgressDialog()
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        } else if (view.id == R.id.otp_up_btn) {
            mBinding?.otpLayout?.visibility = View.VISIBLE
            /* val intent = Intent(this@ResetPasswordActivity, CreatePasswordActivity::class.java)
             intent.putExtra("isValidateQuestion", true)
             intent.putExtra("phoneNo", mBinding?.phoneNoTxt?.text.toString())
             intent.putExtra("countryCode", "+91")

             dismissProgressDialog()
             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
             startActivity(intent)*/
            if (!isEmailType && mBinding?.phoneNoTxt?.text.toString() != null && mBinding?.phoneNoTxt?.text.toString().length > 0) {
                showProgressDialog()
                isSecrectOption = false
                // mViewModel.checkUserProfile("", mBinding?.phoneNoTxt.getText().toString(), "+91");
                mViewModel!!.forgotPassword("", mBinding?.phoneNoTxt?.text.toString(), "+91")

            } else {
                /*  if (isEmailType) {
                      mBinding?.emailTxt?.background =
                          getDrawable(R.drawable.error_red_background_view)
                      mBinding?.emailErrorText?.visibility = View.VISIBLE

                      //                        Utils.showToastDialog(mViewModel.getLabel(AppConstant.AppLabelName.enterEmail),this);
                  } else {
                      mBinding?.phoneTextLayout?.background =
                          getDrawable(R.drawable.error_red_background_view)
                      mBinding?.mobileErrorText?.visibility = View.VISIBLE

                      //                        Utils.showToastDialog(mViewModel.getLabel(AppConstant.AppLabelName.enterMobileNo),this);
                  }*/
            }
        }
    }
}