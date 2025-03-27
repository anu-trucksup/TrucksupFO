package com.trucksup.field_officer.presenter.view.activity.trackingAndVerification

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.logistics.trucksup.modle.VehicleTrack
import com.logistics.trucksup.modle.VehicleTrackingDetailsData
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler.TrackingDetailsDataCantroler
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler.TrackingPlanCantroler
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler.VehicalListClickCantroler

import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityVehicalTrackingScreenBinding
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.common.ProgressDailogBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter.VehicalTrackingDataAdaptor
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.CurrentTrackingPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.CurrentVerificationPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.DataTrackingPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.TrackingPlanListRequest

class VehicalTrackingScreen : BaseActivity(), TrackingPlanCantroler, TrackingDetailsDataCantroler,
    VehicalListClickCantroler {
    var progressDailogBox: ProgressDailogBox? = null
    private lateinit var binding: ActivityVehicalTrackingScreenBinding
    var driverMobile: String = ""
    var maxDay: Int = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehical_tracking_screen)
        progressDailogBox = ProgressDailogBox(this)


    }

    override fun onResume() {
        super.onResume()
        //createBill("d")
    }


   /* fun createBill(type: String) {
        var errorType: String = ""
        var request: PaymentGetWayTokenRequest = PaymentGetWayTokenRequest(
            PreferenceManager.getAccesKey(this),
            PreferenceManager.getAccesPassword(this),
            PreferenceManager.getAccesUserAgaint(this),
            PreferenceManager.getAccesUserName(this),
            PreferenceManager.getAccesUserInssur(this)
        )
        Log.e(" voley main url ", PreferenceManager.getServerUrl(this))

        val requestQueue = Volley.newRequestQueue(this)
        var `object`: JSONObject = JSONObject()
        val gson = Gson()
        val requestObject: String = gson.toJson(request)
        try {
            `object` = JSONObject(requestObject)
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        Log.e(" voley req ", `object`.toString())
        progressDailogBox?.show()
        var jsonObejct: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            PreferenceManager.getServerUrl(this) + "JwtAuth/api/Auth/GenerateJWTtoken",
            `object`,
            Response.Listener { response ->
                val gson = Gson()
                var data: PaymentGetWayTokenResponse? =
                    gson.fromJson(response.toString(), PaymentGetWayTokenResponse::class.java)
//                Log.e("The user name of voley ", data?.profileDetails?.profileName.toString())
                Log.e("The Response of voley token ", response.toString())

                if (VollyRequests.mStatusCode == 200) {

                    if (type == "d") {
                        getData("Bearer " + data?.accessToken.toString())
                    } else if (type == "r") {
                        sendMassage("Bearer " + data?.accessToken.toString())
                    } else {
                        getPlanData("Bearer " + data?.accessToken.toString())
                    }

                } else {
                    progressDailogBox?.dismiss()
                    // myTripResults.myTripError("Some thing server error")
                }


            },
            Response.ErrorListener { error ->
                // Handle the error
                progressDailogBox?.dismiss()
                if (error is NetworkError) {
                    // Handle network error
                    Log.e("Volley Error", "Network Error: ${error.message}")
                    errorType = "Network Error"
                    LoggerMessage.tostPrint(errorType.toString(), this)
                    val data: ErrorModel = ErrorModel(
                        "" + errorType,
                        "" + PreferenceManager.getPhoneNo(this),
                        "" + PreferenceManager.getUserData(this)?.profileName,
                        "" + DeviceInfoUtils.getDeviceModel(this),
                        "API",
                        "JwtAuth/api/Auth/GenerateJWTtoken"
                    )
                    ErrorStore().StoreError(data)
                } else if (error is ServerError) {
                    // Handle server error
                    Log.e("Volley Error", "Server Error: ${error.message}")
                    errorType = "Server Error"
                    LoggerMessage.tostPrint(errorType.toString(), this)
                    val data: ErrorModel = ErrorModel(
                        "" + errorType,
                        "" + PreferenceManager.getPhoneNo(this),
                        "" + PreferenceManager.getUserData(this)?.profileName,
                        "" + DeviceInfoUtils.getDeviceModel(this),
                        "API",
                        "JwtAuth/api/Auth/GenerateJWTtoken"
                    )
                    ErrorStore().StoreError(data)
                } else if (error is AuthFailureError) {
                    // Handle authentication failure error
                    Log.e("Volley Error", "Auth Failure Error: ${error.message}")
                    errorType = "Auth Failure Error"
                    val data: ErrorModel = ErrorModel(
                        "" + errorType,
                        "" + PreferenceManager.getPhoneNo(this),
                        "" + PreferenceManager.getUserData(this)?.profileName,
                        "" + DeviceInfoUtils.getDeviceModel(this),
                        "API",
                        "JwtAuth/api/Auth/GenerateJWTtoken"
                    )
                    ErrorStore().StoreError(data)
                    LoggerMessage.tostPrint(errorType.toString(), this)
                } else if (error is ParseError) {
                    // Handle parse error
                    Log.e("Volley Error", "Parse Error: ${error.message}")
                    errorType = "Parse Error"
                    LoggerMessage.tostPrint(errorType.toString(), this)
                    val data: ErrorModel = ErrorModel(
                        "" + errorType,
                        "" + PreferenceManager.getPhoneNo(this),
                        "" + PreferenceManager.getUserData(this)?.profileName,
                        "" + DeviceInfoUtils.getDeviceModel(this),
                        "API",
                        "JwtAuth/api/Auth/GenerateJWTtoken"
                    )
                    ErrorStore().StoreError(data)
                } else if (error is NoConnectionError) {
                    // Handle no connection error
                    Log.e("Volley Error", "No Connection Error: ${error.message}")
                    errorType = "No Connection Error"
                    LoggerMessage.tostPrint(errorType.toString(), this)
                    val data: ErrorModel = ErrorModel(
                        "" + errorType,
                        "" + PreferenceManager.getPhoneNo(this),
                        "" + PreferenceManager.getUserData(this)?.profileName,
                        "" + DeviceInfoUtils.getDeviceModel(this),
                        "API",
                        "JwtAuth/api/Auth/GenerateJWTtoken"
                    )
                    ErrorStore().StoreError(data)
                } else if (error is TimeoutError) {
                    // Handle timeout error
                    Log.e("Volley Error", "Timeout Error: ${error.message}")
                    errorType = "Timeout Error"
                    LoggerMessage.tostPrint(errorType.toString(), this)
                    val data: ErrorModel = ErrorModel(
                        "" + errorType,
                        "" + PreferenceManager.getPhoneNo(this),
                        "" + PreferenceManager.getUserData(this)?.profileName,
                        "" + DeviceInfoUtils.getDeviceModel(this),
                        "API",
                        "JwtAuth/api/Auth/GenerateJWTtoken"
                    )
                    ErrorStore().StoreError(data)
                } else {
                    // Handle other error types
                    errorType = "Generic Error"
                    Log.e("Volley Error", "Generic Error: ${error.message}")
                    LoggerMessage.tostPrint(errorType.toString(), this)
                    val data: ErrorModel = ErrorModel(
                        "" + errorType,
                        "" + PreferenceManager.getPhoneNo(this),
                        "" + PreferenceManager.getUserData(this)?.profileName,
                        "" + DeviceInfoUtils.getDeviceModel(this),
                        "API",
                        "JwtAuth/api/Auth/GenerateJWTtoken"
                    )
                    ErrorStore().StoreError(data)
                }
//                 LoggerMessage.LogErrorMsg("Error", "" + response.message.toString())

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["x-api-key"] =
                    PreferenceManager.getAccesHeader(this@VehicalTrackingScreen).toString()
                params["Content-Type"] = "application/json"
                return params
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject>? {
                if (response != null) {
                    VollyRequests.mStatusCode = response.statusCode
                }

                return super.parseNetworkResponse(response)
            }

        }
        jsonObejct.retryPolicy = DefaultRetryPolicy(
            18000, // Timeout duration in milliseconds
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(jsonObejct);
    }*/

    fun getData(key: String) {
        progressDailogBox?.show()
       // MyResponse().GetVehicleTrackingDetails("", key, this, this)
    }

    fun getPlanData(key: String) {
        var request: TrackingPlanListRequest = TrackingPlanListRequest(
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this),
            "tracking"
        )

       // MyResponse().getTrackingPlanList(key, request, this, this)
    }

    fun addMoreDays(v: View) {
       // createBill("p")
    }

    fun addMoreTruck(v: View) {
        if (binding.addTruck.tag == "y") {
            var intent: Intent = Intent(this, AddTrackingVehical::class.java)
            intent.putExtra("day", maxDay)
            startActivity(intent)
        } else {
            val abx = MyAlartBox(this, resources.getString(R.string.noTrackingPlanMessage), "m")
            abx.show()

        }
    }

    fun setTrackingData(VehicleTrack: ArrayList<VehicleTrack>) {

        var adaptor = VehicalTrackingDataAdaptor(this!!, VehicleTrack)

        binding.vehicalList?.layoutManager = LinearLayoutManager(this!!)

        binding.vehicalList?.adapter = adaptor
        adaptor.notifyDataSetChanged()

        if (adaptor.itemCount == 0) {
            binding.noData?.visibility = View.VISIBLE
        } else {
            binding.noData?.visibility = View.GONE
        }
    }

    fun openHistory(v: View) {
        if (binding.viewHistory.tag == "y") {
            var intent: Intent = Intent(this, VehicalTrackingHistory::class.java)
            startActivity(intent)
        }
    }

    override fun TrackingPlanList(
        data: ArrayList<DataTrackingPlan>,
        dataAdon: ArrayList<DataTrackingPlan>
    ) {
        progressDailogBox?.dismiss()
        val parameters = Bundle().apply {
            this.putString("mobile", PreferenceManager.getPhoneNo(this@VehicalTrackingScreen))
            this.putString("plan", "tracking")
        }
        //   FirebaseAnalytics().CreateCustomEventFullData("tracking_planview",parameters)
        val bottomSheetFragment = VehicalTrackingPlan(data, 1, "p")
        bottomSheetFragment.show(supportFragmentManager, "trackingPlanFrangment")

    }

    override fun TrackingCountData(
        price: String,
        data: CurrentTrackingPlan,
        vData: CurrentVerificationPlan
    ) {

    }


    override fun TrackingDetailsData(
        data: VehicleTrackingDetailsData
    ) {
        progressDailogBox?.dismiss()
        binding.main.visibility = View.VISIBLE

        if (data.vehicleTrackList != null) {

            binding.noData.visibility = View.GONE
            setTrackingData(data.vehicleTrackList as ArrayList<VehicleTrack>)



            if (data.vehicleTrackingHistory.size == 0) {
                binding.historyCard.setCardBackgroundColor(getColor(R.color.card_dis));
                binding.viewHistory.setTextColor(getColor(R.color.optinal_text_color))
                binding.historyCard.strokeColor = getColor(R.color.card_dis)
                binding.viewHistory.tag = "n"


            } else {
                binding.historyCard.setCardBackgroundColor(getColor(R.color.blue_tr60));
                binding.viewHistory.setTextColor(getColor(R.color.white))
                binding.historyCard.strokeColor = getColor(R.color.blue)
                binding.viewHistory.tag = "y"

            }


            if (data.currentPlan == null || data.currentPlan.size == 0) {

//                binding.activCard.setCardBackgroundColor(getColor(R.color.card_dis));
//                binding.planText.setTextColor(getColor(R.color.optinal_text_color))
//                binding.activCard.strokeColor=getColor(R.color.card_dis)
                var at: String = resources.getString(R.string.blanceTrackingDay)
                binding.planText.text = at + " 0"
            } else {
                if (data.currentPlan[0].totalQty == 0) {
//                    binding.activCard.setCardBackgroundColor(getColor(R.color.card_dis));
//                    binding.planText.setTextColor(getColor(R.color.optinal_text_color))
//                    binding.activCard.strokeColor=getColor(R.color.card_dis)
                    var at: String = resources.getString(R.string.blanceTrackingDay)
                    binding.planText.text = at + " 0"
                    binding.addTruck.tag = "n"
                    binding.planText.tag = "n"

                } else {
                    //   binding.activCard.setCardBackgroundColor(getColor(R.color.white));
                    //  binding.planText.setTextColor(getColor(R.color.blue))
                    //  binding.activCard.strokeColor=getColor(R.color.blue)


                    binding.addTruck.tag = "y"
                    binding.planText.tag = "y"
                    var at: String = resources.getString(R.string.blanceTrackingDay)
                    binding.planText.text = at + " " + data.currentPlan[0].totalQty

                    if (data.currentPlan[0].totalQty != null && !TextUtils.isEmpty(data.currentPlan[0].totalQty.toString())) {

                        //var dd: String = data.remainingCounts.filter { it.isDigit() }
                        maxDay = data.currentPlan[0].totalQty
                    }
                }

            }


        } else {

            binding.noData.visibility = View.VISIBLE
            binding.historyCard.setCardBackgroundColor(getColor(R.color.card_dis));
            binding.viewHistory.setTextColor(getColor(R.color.optinal_text_color))
            binding.historyCard.strokeColor = getColor(R.color.card_dis)

//            binding.activCard.setCardBackgroundColor(getColor(R.color.card_dis));
//            binding.planText.setTextColor(getColor(R.color.optinal_text_color))
//            binding.activCard.strokeColor=getColor(R.color.card_dis)

            var at: String = resources.getString(R.string.blanceTrackingDay)
            binding.planText.text = at + " " + data.currentPlan[0].totalQty

            //  binding.planText.text=resources.getString(R.string.active_plan)

            binding.viewHistory.tag = "n"
            binding.planText.tag = "n"
            binding.addTruck.tag = "n"
        }


    }

    override fun resentConsetData(message: String) {
        progressDailogBox?.dismiss()
        //AddTripBox(this, message, "rc").show()
    }


    override fun error(error: String) {


        binding.historyCard.setCardBackgroundColor(getColor(R.color.card_dis));
        binding.viewHistory.setTextColor(getColor(R.color.optinal_text_color))
        binding.historyCard.strokeColor = getColor(R.color.card_dis)

//        binding.activCard.setCardBackgroundColor(getColor(R.color.card_dis));
//        binding.planText.setTextColor(getColor(R.color.optinal_text_color))
//        binding.activCard.strokeColor=getColor(R.color.card_dis)

        //  binding.planText.text=resources.getString(R.string.active_plan)

        binding.viewHistory.tag = "n"
        binding.planText.tag = "n"
        binding.addTruck.tag = "n"
        progressDailogBox?.dismiss()

    }


    fun refresh(v: View) {
        //createBill("d")
    }

    fun backScreen(v: View) {
        finish()
    }

    fun openTranHist(v: View) {

        /*  var intent:Intent = Intent(this, TransactionHistory::class.java)
          startActivity(intent)*/
    }

    override fun reSentConsent(mo: String) {
        driverMobile = mo
       // createBill("r")
    }

    fun sendMassage(key: String) {
       /* var request: ResendConsentRequest = ResendConsentRequest(
            PreferenceManager.getServerDateUtc(""),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this),
            driverMobile
        )
        MyResponse().resentConsnt(key, request, this, this)*/
    }


}