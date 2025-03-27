package com.logistics.trucksup.activities.trackingAndVerification

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityTrackingCheckOutBinding
import com.trucksup.field_officer.presenter.common.ProgressDailogBox
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler.TrackingPlanCantroler
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler.TrackingPlanCountCantroler

import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.MyResponse
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.TrackingCountRequeat
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.VasPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter.VehicaleTackingPlanAdaptor
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.CurrentTrackingPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.CurrentVerificationPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.DataTrackingPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.TrackingPlanListRequest
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.regex.Matcher
import java.util.regex.Pattern


class TrackingCheckOut : AppCompatActivity(), TrackingPlanCantroler, TrackingPlanCountCantroler {

    private lateinit var binding: ActivityTrackingCheckOutBinding
    var progressDailogBox: ProgressDailogBox? = null
    var coutnList = ArrayList<VasPlan>()
    var planName: String = ""
    var countDay: String = ""
    var planId: String = ""
    var amount: String = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tracking_check_out)
        progressDailogBox = ProgressDailogBox(this)
        val gson = Gson()
        var dataString: String = intent.getStringExtra("data").toString()
        planName = intent.getStringExtra("pn").toString()
        countDay = intent.getStringExtra("cd").toString()
        amount = intent.getStringExtra("am").toString()
        val type: Type = object : TypeToken<ArrayList<VasPlan>?>() {}.type
        coutnList = gson.fromJson(dataString, type)

        planId = coutnList[0].vasId


        Log.e("Amount", "amount >>>>>> " + amount)

        binding.planName.text = planName

        var rs: String = resources.getString(R.string.rs)
        binding.amount.text = rs + " " + amount
        if (PreferenceManager.getLanguage(this) == "en") {
            binding.loadCount.text = "Plan for " + countDay + " days"
        } else {

            binding.loadCount.text = countDay + " दिनों के लिए प्लान"
        }
        // createBill("d")

    }

    /*fun createBill(type: String) {
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

                    if (type=="c")
                    {
                        calculateData("Bearer " + data?.accessToken.toString())
                    }
                     else {
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
                params["x-api-key"] = PreferenceManager.getAccesHeader(this@TrackingCheckOut).toString()
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

    fun getPlanData(key: String) {
        var request: TrackingPlanListRequest = TrackingPlanListRequest(
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this),
            "tracking"
        )

        // MyResponse().getTrackingPlanList(key,request,this,this)
    }

    fun calculateData(key: String) {
        var request: TrackingCountRequeat = TrackingCountRequeat(
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this),
            "tracking",
            coutnList
        )

        // MyResponse().getTrackingCount(key,request,this,this)
    }

    override fun TrackingPlanList(
        data: ArrayList<DataTrackingPlan>,
        dataAdon: ArrayList<DataTrackingPlan>
    ) {
        progressDailogBox?.dismiss()
        setData(dataAdon)

        // createBill("c")
    }

    override fun TrackingCountData(
        price: String,
        data: CurrentTrackingPlan,
        vData: CurrentVerificationPlan
    ) {

        progressDailogBox?.dismiss()
        //  binding.amount.text=price;
        binding.totleAmount.text = price

        if (data.remainingCounts != null && !TextUtils.isEmpty(data.remainingCounts.trim())) {
            binding.plansDes.visibility = View.VISIBLE
            binding.plansDes.text = data.remainingCounts

        } else {

            binding.plansDes.visibility = View.GONE
        }


    }

    override fun error(error: String) {
        progressDailogBox?.dismiss()
    }

    fun setData(planListData: ArrayList<DataTrackingPlan>) {

        for (dt in planListData) {
            Log.e("Plan Data", "Data ===  " + dt)
        }
        var adaptor = VehicaleTackingPlanAdaptor(
            this!!,
            planListData,
            this as com.trucksup.field_officer.presenter.view.activity.vehicleVerify.controller.TrackingPlanCountCantroler
        )

        binding.extraPlan?.layoutManager = LinearLayoutManager(this!!)

        binding.extraPlan?.adapter = adaptor
        adaptor.notifyDataSetChanged()
    }

    override fun ClickAdd(rs: Int, day: String, data: VasPlan) {
        var isHave: Boolean = false
        var s: Int = coutnList.size - 1
        for (i in 0..s) {


            if (coutnList[i].vasId.toString().toInt() == data.vasId.toString().toInt()) {
                coutnList.set(i, data)
                isHave = true
//                Log.e("Data id","Id=="+data.id+"\n list id=="+checkAddon[i].id)
//                Log.e("data size","Data size=="+checkAddon.size+"\ndata add "+data+"\n main data "+checkAddon)
                break
            }

        }

        if (isHave == false) {
            coutnList.add(data)
        }
        //createBill("c")

    }

    override fun ClickMines(rs: Int, day: String, data: VasPlan) {
        var isHave: Boolean = false
        if (coutnList.size == 0) {

            coutnList.add(data)
            calculateData()
            // Log.e("data size","Data size=="+0+"\ndata add "+data+"\n main data "+checkAddon)
        } else {

            var s: Int = coutnList.size - 1
            for (i in 0..s) {


                if (coutnList[i].vasId == data.vasId) {
                    coutnList.set(i, data)
                    isHave = true
//                    Log.e("Data id","Id=="+data.id+"\n list id=="+checkAddon[i].id)
//                    Log.e("data size","Data size=="+checkAddon.size+"\ndata add "+data+"\n main data "+checkAddon)
                    break
                }

            }
        }
        if (isHave == false) {
            coutnList.add(data)
        }
        // createBill("c")

    }

    override fun calculateData() {

    }

    fun payAmount(v: View) {

        var gst: String = ""
        if (!TextUtils.isEmpty(binding.gst.text)) {
            if (isValidGSTIN(binding.gst.text.toString().trim())) {
                gst = binding.gst.text.toString()
            } else {

                LoggerMessage.onSNACK(binding.gst, resources.getString(R.string.validGst), this)
                return
            }

        }

//        try {
//            for (i in 0..coutnList.size-1) {
//                if (coutnList[i].qty == 0) {
//                    coutnList.removeAt(i)
//                }
//            }
//        }
//        catch (e:Exception)
//        {
//        }

        try {
            for (i in coutnList.size - 1 downTo 0) {
                if (coutnList[i].qty == 0) {
                    coutnList.removeAt(i)
                }
            }
        } catch (e: Exception) {
        }

        val jsonStringAddons = Gson().toJson(coutnList)

        val parameters = Bundle().apply {
            this.putString("mobile", PreferenceManager.getPhoneNo(this@TrackingCheckOut!!))
            this.putString("plan", "tracking")
        }
        //FirebaseAnalytics().CreateCustomEventFullData("tracking_plan_order",parameters)
       /* var intent: Intent = Intent(this, TrackingMainCheckOut::class.java)
        intent.putExtra("addons", jsonStringAddons)
        intent.putExtra("plan", planId)
        intent.putExtra("gst", gst)

        startActivity(intent)
        finish()*/
    }


    private fun isValidGSTIN(gstin: String): Boolean {
        val gstinPattern = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$"
        val pattern: Pattern = Pattern.compile(gstinPattern)
        val matcher: Matcher = pattern.matcher(gstin)
        return matcher.matches()
    }

    fun adjustFontScale(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }

    fun backScreen(v: View) {
        finish()
    }
}