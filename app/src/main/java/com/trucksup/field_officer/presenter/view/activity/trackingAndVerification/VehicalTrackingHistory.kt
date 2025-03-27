package com.trucksup.field_officer.presenter.view.activity.trackingAndVerification

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.logistics.trucksup.modle.TrackingHistory
import com.logistics.trucksup.modle.VehicleTrackingDetailsData
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityVehicalTrackingHistoryBinding
import com.trucksup.field_officer.presenter.common.ProgressDailogBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler.TrackingDetailsDataCantroler
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.MyResponse
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter.VehicalTrackingHistoryAdaptor
import org.json.JSONException
import org.json.JSONObject

class VehicalTrackingHistory : BaseActivity(), TrackingDetailsDataCantroler {
    var progressDailogBox: ProgressDailogBox? = null
    private lateinit var binding: ActivityVehicalTrackingHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehical_tracking_history)
        progressDailogBox = ProgressDailogBox(this)


        //createBill()

    }

    fun setTrackingData(data: ArrayList<TrackingHistory>) {

        var adaptor: VehicalTrackingHistoryAdaptor = VehicalTrackingHistoryAdaptor(this, data)

        binding.dataList?.layoutManager = LinearLayoutManager(this!!)

        binding.dataList?.adapter = adaptor
        adaptor.notifyDataSetChanged()

        if (adaptor.itemCount == 0) {
            binding.noData.visibility = View.VISIBLE
        } else {
            binding.noData.visibility = View.GONE
        }
    }

    fun onBackScreen(v: View) {
        finish()
    }

    override fun TrackingDetailsData(
        data: VehicleTrackingDetailsData
    ) {
        progressDailogBox?.dismiss()
        if (data != null) {
            if (data?.vehicleTrackingHistory != null) {

                setTrackingData(data?.vehicleTrackingHistory as ArrayList<TrackingHistory>)
            } else {
                binding.noData.visibility = View.VISIBLE
            }
        }
//        setTrackingData()

    }

    override fun resentConsetData(message: String) {
        TODO("Not yet implemented")
    }

    /*fun createBill() {
        var errorType:String=""
        var request: PaymentGetWayTokenRequest = PaymentGetWayTokenRequest(
            PreferenceManager.getAccesKey(this),
            PreferenceManager.getAccesPassword(this),
            PreferenceManager.getAccesUserAgaint(this),
            PreferenceManager.getAccesUserName(this),
            PreferenceManager.getAccesUserInssur(this))
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
            Method.POST, PreferenceManager.getServerUrl(this)+"JwtAuth/api/Auth/GenerateJWTtoken", `object`,
            Response.Listener { response ->
                val gson = Gson()
                var data: PaymentGetWayTokenResponse? =
                    gson.fromJson(response.toString(), PaymentGetWayTokenResponse::class.java)
//                Log.e("The user name of voley ", data?.profileDetails?.profileName.toString())
                Log.e("The Response of voley token ", response.toString())

                if (VollyRequests.mStatusCode == 200) {
                    getData("Bearer " + data?.accessToken.toString())


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
                    errorType="Network Error"
                    LoggerMessage.tostPrint(errorType.toString(),this)
                    val data: ErrorModel = ErrorModel(""+errorType,""+ PreferenceManager.getPhoneNo(this),""+ PreferenceManager.getUserData(this)?.profileName,""+ DeviceInfoUtils.getDeviceModel(this),"API","JwtAuth/api/Auth/GenerateJWTtoken")
                    ErrorStore().StoreError(data)
                } else if (error is ServerError) {
                    // Handle server error
                    Log.e("Volley Error", "Server Error: ${error.message}")
                    errorType="Server Error"
                    LoggerMessage.tostPrint(errorType.toString(),this)
                    val data: ErrorModel = ErrorModel(""+errorType,""+ PreferenceManager.getPhoneNo(this),""+ PreferenceManager.getUserData(this)?.profileName,""+ DeviceInfoUtils.getDeviceModel(this),"API","JwtAuth/api/Auth/GenerateJWTtoken")
                    ErrorStore().StoreError(data)
                } else if (error is AuthFailureError) {
                    // Handle authentication failure error
                    Log.e("Volley Error", "Auth Failure Error: ${error.message}")
                    errorType="Auth Failure Error"
                    val data: ErrorModel = ErrorModel(""+errorType,""+ PreferenceManager.getPhoneNo(this),""+ PreferenceManager.getUserData(this)?.profileName,""+ DeviceInfoUtils.getDeviceModel(this),"API","JwtAuth/api/Auth/GenerateJWTtoken")
                    ErrorStore().StoreError(data)
                    LoggerMessage.tostPrint(errorType.toString(),this)
                } else if (error is ParseError) {
                    // Handle parse error
                    Log.e("Volley Error", "Parse Error: ${error.message}")
                    errorType="Parse Error"
                    LoggerMessage.tostPrint(errorType.toString(),this)
                    val data: ErrorModel = ErrorModel(""+errorType,""+ PreferenceManager.getPhoneNo(this),""+ PreferenceManager.getUserData(this)?.profileName,""+ DeviceInfoUtils.getDeviceModel(this),"API","JwtAuth/api/Auth/GenerateJWTtoken")
                    ErrorStore().StoreError(data)
                } else if (error is NoConnectionError) {
                    // Handle no connection error
                    Log.e("Volley Error", "No Connection Error: ${error.message}")
                    errorType="No Connection Error"
                    LoggerMessage.tostPrint(errorType.toString(),this)
                    val data: ErrorModel = ErrorModel(""+errorType,""+ PreferenceManager.getPhoneNo(this),""+ PreferenceManager.getUserData(this)?.profileName,""+ DeviceInfoUtils.getDeviceModel(this),"API","JwtAuth/api/Auth/GenerateJWTtoken")
                    ErrorStore().StoreError(data)
                } else if (error is TimeoutError) {
                    // Handle timeout error
                    Log.e("Volley Error", "Timeout Error: ${error.message}")
                    errorType="Timeout Error"
                    LoggerMessage.tostPrint(errorType.toString(),this)
                    val data: ErrorModel = ErrorModel(""+errorType,""+ PreferenceManager.getPhoneNo(this),""+ PreferenceManager.getUserData(this)?.profileName,""+ DeviceInfoUtils.getDeviceModel(this),"API","JwtAuth/api/Auth/GenerateJWTtoken")
                    ErrorStore().StoreError(data)
                } else {
                    // Handle other error types
                    errorType="Generic Error"
                    Log.e("Volley Error", "Generic Error: ${error.message}")
                    LoggerMessage.tostPrint(errorType.toString(),this)
                    val data: ErrorModel = ErrorModel(""+errorType,""+ PreferenceManager.getPhoneNo(this),""+ PreferenceManager.getUserData(this)?.profileName,""+ DeviceInfoUtils.getDeviceModel(this),"API","JwtAuth/api/Auth/GenerateJWTtoken")
                    ErrorStore().StoreError(data)
                }
//                 LoggerMessage.LogErrorMsg("Error", "" + response.message.toString())

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["x-api-key"] = PreferenceManager.getAccesHeader(this@VehicalTrackingHistory).toString()
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
        //MyResponse().GetVehicleTrackingDetails("", key, this, this)
    }


    override fun error(error: String) {
        progressDailogBox?.dismiss()
    }

    fun backScreen(v: View) {
        finish()
    }


}