package com.trucksup.field_officer.presenter.view.activity.auth.forget

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityResetPasswordBinding
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.common.Utils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : BaseActivity(), View.OnClickListener {
    var mBinding: ActivityResetPasswordBinding? = null
    private var mViewModel: ResetPasswordViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)

        //click Listener
        mBinding?.topView?.ivBack?.setOnClickListener(this)
        mBinding?.otpUpBtn?.setOnClickListener(this)
        mBinding?.tvVerify?.setOnClickListener(this)

        mViewModel = ViewModelProvider(this).get(ResetPasswordViewModel::class.java)
        // mBinding?.setViewModel(mViewModel);

        setupObserver()

    }

    private fun setupObserver() {
        mViewModel!!.resultResetLD.observe(this@ResetPasswordActivity) { responseModel ->
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@ResetPasswordActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            }  else {
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

            if (TextUtils.isEmpty(mBinding?.phoneNoTxt?.text.toString().trim())) {
                mBinding?.phoneNoTxt?.setError("Enter Mobile Number")
                mBinding?.phoneNoTxt?.requestFocus()
                return
            }

            if (isValidMobile(mBinding?.phoneNoTxt?.text.toString())) {
                //showProgressDialog()
                /*mBinding?.otpUpBtn?.isEnabled = true
                mBinding?.otpUpBtn?.setBackgroundDrawable(resources.getDrawable(R.drawable.background_1_1))
                mBinding?.otpUpBtn?.setTextColor(Color.parseColor("#FFFFFF"))*/

                mBinding?.otpUpBtn?.isEnabled = false
                mBinding?.otpUpBtn?.setBackgroundColor(Color.parseColor("#C2C2C2"))
                mBinding?.otpUpBtn?.setTextColor(Color.parseColor("#6A6A6A"))

                mBinding?.otpLayout?.visibility = View.VISIBLE
                // mViewModel.checkUserProfile("", mBinding?.phoneNoTxt.getText().toString(), "+91");
               // mViewModel!!.forgotPassword("", mBinding?.phoneNoTxt?.text.toString(), "+91")

            } else {
                LoggerMessage.onSNACK(
                    mBinding!!.phoneNoTxt,
                    "Enter valid mobile number.",
                    applicationContext
                )
            }
        }
    }
}