package com.trucksup.field_officer.presenter.view.activity.vehicleVerify

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.logistics.trucksup.activities.vehicleVerify.VehicleDetailsActivity
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter.VerifiedVehicleHistory
import com.logistics.trucksup.modle.VerifyVehicleResponse
import com.logistics.trucksup.modle.requestModle.GetPlansRequest
import com.logistics.trucksup.modle.requestModle.GetVerifiedVehiclesRequest
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.data.model.subscription.GetPlansResponse
import com.trucksup.field_officer.data.model.vehicle.GetVerifiedVehiclesResponse
import com.trucksup.field_officer.databinding.ActivityVehicleVerifyBinding

import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.VerifyVehicleRequest

class VehicleVerifyActivity : BaseActivity(), JWTtoken, VerificationController,
    VehicleVerifyController, PlansCalculation {

    private lateinit var binding: ActivityVehicleVerifyBinding

    private var direction: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_verify)

        setListeners()
    }

    override fun onResume() {
        super.onResume()
        direction = 0
        getToken()
        binding.etVehicleNumber.getText().clear()
    }

    fun getToken() {
//        progressDailogBox?.show()
        LoadingUtils.showDialog(this,false)

        var request = GenerateJWTtokenRequest(
            username = PreferenceManager.getAccesUserName(this),
            password = PreferenceManager.getAccesPassword(this),
            apiSecreteKey = PreferenceManager.getAccesKey(this),
            userAgent = PreferenceManager.getAccesUserAgaint(this),
            issuer = PreferenceManager.getAccesUserInssur(this)
        )
        //MyResponse().generateJWTtoken(request, this, this)
    }


    private fun setListeners() {
        //verify button
        binding.btnVerify.setOnClickListener {
            if (binding.etVehicleNumber.getText().toString().trim().isNullOrEmpty()) {
                LoggerMessage.onSNACK(
                    binding.etVehicleNumber,
                    resources.getString(R.string.enterTruckNo),
                    this
                )
            } else {
                direction = 1
                getToken()
            }
        }

        //transaction history
        binding.btnTransactionHis.setOnClickListener {
           /* val intent = Intent(this, TransactionHistory::class.java)
            startActivity(intent)*/
        }

        //add more vehicle button
        binding.btnAddMoreVehicles.setOnClickListener {
            direction = 2
            getToken()
        }
    }


    fun backScreen(view: View) {
        finish()
    }

    override fun onSuccess(response: GetVerifiedVehiclesResponse) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
//        if (response.planDetails != null) {
//            if (response.planDetails.count == 0) {
//                binding.cardActivePlan.apply {
//                    setStrokeColor(resources.getColor(R.color.unselect_color))
//                    setCardBackgroundColor(resources.getColor(R.color.unselect_color))
//                }
//                binding.tvActivePlan.setTextColor(resources.getColor(R.color.unsellect_text_color))
//                if (PreferenceManager.getLanguage(this).toString() == "en") {
//                    binding.tvActivePlan.text = "Active Plan"
//                } else {
//                    binding.tvActivePlan.text = "एक्टिव प्लान"
//                }
//            }
//            else
//            {
//                binding.cardActivePlan.apply {
//                    setStrokeColor(resources.getColor(R.color.blue))
//                    setCardBackgroundColor(resources.getColor(R.color.white))
//                }
//                binding.tvActivePlan.setTextColor(resources.getColor(R.color.blue))
//                if (PreferenceManager.getLanguage(this).toString() == "en") {
//                    binding.tvActivePlan.text = "Active Plan\n" + response.planDetails.countLeft
//                } else {
//                    binding.tvActivePlan.text = "एक्टिव प्लान\n" + response.planDetails.countLeft
//                }
//            }
//        }
//        else
//        {
//            binding.cardActivePlan.apply {
//                setStrokeColor(resources.getColor(R.color.unselect_color))
//                setCardBackgroundColor(resources.getColor(R.color.unselect_color))
//            }
//            binding.tvActivePlan.setTextColor(resources.getColor(R.color.unsellect_text_color))
//            if (PreferenceManager.getLanguage(this).toString() == "en") {
//                binding.tvActivePlan.text = "Active Plan"
//            } else {
//                binding.tvActivePlan.text = "एक्टिव प्लान"
//            }
//        }

        binding.balanceVVerification.text=resources.getString(R.string.balance_vehicle_verification)+" ${response.planDetails.count}"

        if (response.vehicleDetail.isNullOrEmpty()) {
            binding.btnViewHistory.setBackgroundDrawable(resources.getDrawable(R.drawable.view_history_background))
            binding.btnViewHistory.setTextColor(resources.getColor(R.color.unsellect_text_color))
        } else {
            binding.btnViewHistory.setBackgroundDrawable(resources.getDrawable(R.drawable.view_history_background2))
            binding.btnViewHistory.setTextColor(resources.getColor(R.color.white))

            //view history button
            binding.btnViewHistory.setOnClickListener {
                val intent = Intent(this, VehicleVerifyHistoryActivity::class.java)
                startActivity(intent)
            }
        }

        if (response.vehicleDetail.isNullOrEmpty()) {
            binding.rvVerifiedVehicle.visibility = View.GONE
            binding.noDataFoundLayout.visibility = View.VISIBLE
        } else {
            binding.rvVerifiedVehicle.visibility = View.VISIBLE
            binding.noDataFoundLayout.visibility = View.GONE
            binding.rvVerifiedVehicle.apply {
                layoutManager =
                    LinearLayoutManager(this@VehicleVerifyActivity, RecyclerView.VERTICAL, false)
                adapter = VerifiedVehicleHistory(this@VehicleVerifyActivity, response.vehicleDetail)
                hasFixedSize()
            }
        }

    }

    override fun onFailure(meg: String) {

        LoadingUtils.hideDialog()
    }

    override fun onTokenSuccess(response: GenerateJWTtokenResponse) {
        if (direction == 0) {
            var request = GetVerifiedVehiclesRequest(
                requestId = PreferenceManager.getRequestNo(),
                requestDatetime = PreferenceManager.getServerDateUtc(""),
                requestedBy = PreferenceManager.getPhoneNo(this),
                mobileNumber = PreferenceManager.getPhoneNo(this),
                limit = 3
            )
            MyResponse().getVerifiedVehicles(response.accessToken, request, this, this)
        } else if (direction == 1) {
            val rnds1 = (0..10).random()
            val rnds2 = (0..10).random()
            val request = VerifyVehicleRequest(
                mobileNumber = PreferenceManager.getPhoneNo(this),
                rnds1.toString(),
                rnds2.toString(),
                PreferenceManager.getServerDateUtc(""),
                PreferenceManager.getRequestNo(),
                binding.etVehicleNumber.getText().toString(),false
            )
            MyResponse().verifyVehicle(response.accessToken, request, this, this)
        } else if (direction == 2) {
            var request = GetPlansRequest(
                mobileNumber = PreferenceManager.getPhoneNo(this),
                requestId = PreferenceManager.getRequestNo(),
                requestedBy = PreferenceManager.getPhoneNo(this),
                vasServiceType = "verification"
            )
            MyResponse().getPlans(response.accessToken, request, this, this)
        }
        direction = 0

    }

    override fun onTokenFailure(msg: String) {

        LoadingUtils.hideDialog()
    }

    override fun onVerifySuccess(response: VerifyVehicleResponse) {

        LoadingUtils.hideDialog()
        val jsonString = Gson().toJson(response)
        val intent = Intent(this, VehicleDetailsActivity::class.java)
        intent.putExtra("DATA", jsonString)
        startActivity(intent)
    }

    override fun onVerifyFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        binding.etVehicleNumber.getText().clear()
    }

    override fun onSuccessGetPlans(response: GetPlansResponse) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        val bottomSheetFragment = VerticalVerificationPlan(response.data)
        bottomSheetFragment.show(supportFragmentManager, "verificationPlanFragment")
    }

    override fun onFailureGetPlans(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
    }

}