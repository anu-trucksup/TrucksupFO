package com.trucksup.field_officer.presenter.view.activity.trackingAndVerification

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.logistics.trucksup.modle.Milestone
import com.logistics.trucksup.modle.VehicleTrack
import com.logistics.trucksup.modle.VehicleTrackingDetailsData
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityVehicalTripBinding
import com.trucksup.field_officer.presenter.common.DateHelp
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.common.ProgressDailogBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler.ChangeNumberDateCantroler
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler.DriverDataChangeCantroler
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler.TrackingDetailsDataCantroler
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.MyResponse
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.TripPlanBox
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.adapter.VehicalTripAdaptor
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.controller.TrackingPlanCantroler
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.controller.TripPlanBoxCantroler
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.CurrentTrackingPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.CurrentVerificationPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.DataTrackingPlan
import org.json.JSONException
import org.json.JSONObject
import java.util.Locale

class VehicalTrip : BaseActivity(), TrackingDetailsDataCantroler, ChangeNumberDateCantroler,
    TrackingPlanCantroler,
    DriverDataChangeCantroler, TripPlanBoxCantroler {
    private lateinit var binding: ActivityVehicalTripBinding
    var tripId: String = ""
    var progressDailogBox: ProgressDailogBox? = null
    var tripData: VehicleTrack? = null
    var dayCount: Int = 0
    var type: String = "t"
    var endDateNew: String = ""
    var updatedDate: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehical_trip)
        progressDailogBox = ProgressDailogBox(this)

        val gson = Gson()
        tripData = gson.fromJson(intent.getStringExtra("data"), VehicleTrack::class.java)
        type = intent.getStringExtra("type").toString()
        tripId = tripData?.tripId.toString()

        if (type == "h") {
            binding.buttonLayout.visibility = View.GONE
        } else {
            binding.buttonLayout.visibility = View.VISIBLE
        }

        setData()
       // createBill("gd")
    }


    fun setData() {
        binding.date.text = tripData?.tripStartDate


        if (tripData?.truckNumber != null) {
            binding.vehicalNo.setText(tripData?.truckNumber)
        }
        if (tripData?.toLocation != null) {
            binding.to.setText(tripData?.toLocation)
        }

        if (tripData?.fromLocation != null && !TextUtils.isEmpty(
                tripData?.fromLocation.toString().trim()
            )
        ) {
            binding.fromToLayout.visibility = View.VISIBLE

            binding.from.setText(tripData?.fromLocation)
        } else {
            binding.fromToLayout.visibility = View.GONE
        }

        if (tripData?.tripStartDate != null) {
            binding.startDate.setText(tripData?.tripStartDate)
        }
        if (tripData?.tripEndDate != null) {
            binding.endDate.setText(tripData?.tripEndDate)
        }
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


                    if (type == "eu") {
                        changeNumber("Bearer " + data?.accessToken.toString())
                    } else if (type == "p") {
                        getPlanData("Bearer " + data?.accessToken.toString())
                    } else {
                        getData("Bearer " + data?.accessToken.toString())
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
                params["x-api-key"] = PreferenceManager.getAccesHeader(this@VehicalTrip).toString()
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


    fun setTrackingData(Milestone: ArrayList<Milestone>) {

        var adaptor: VehicalTripAdaptor = VehicalTripAdaptor(this!!, Milestone)

        binding.dataList.layoutManager = LinearLayoutManager(this!!)

        binding.dataList.adapter = adaptor
        adaptor.notifyDataSetChanged()
    }


    fun getData(key: String) {
        progressDailogBox?.show()
        MyResponse().GetVehicleTrackingDetails(tripId, key, this, this)
    }

    override fun TrackingDetailsData(
        data: VehicleTrackingDetailsData
    ) {
        progressDailogBox?.dismiss()

        if (data.milestones != null) {
            setTrackingData(data.milestones as ArrayList<Milestone>)
        }

        if (data.currentPlan != null) {
            if (data.currentPlan.size > 0) {
                dayCount = data.currentPlan[0].totalQty
            }
        }


    }


    override fun TrackingCountData(
        price: String,
        data: CurrentTrackingPlan,
        vData: CurrentVerificationPlan
    ) {
       // TODO("Not yet implemented")
    }


    override fun error(error: String) {
        progressDailogBox?.dismiss()
    }


    fun openMap(v: View) {
        if (tripData?.publicLink != null && !TextUtils.isEmpty(tripData?.publicLink)) {
           /* var web: Intent = Intent(this, WebScreen::class.java)
            web.putExtra("url", tripData?.publicLink)
            startActivity(web)*/
        } else {
            LoggerMessage.onSNACK(binding.from, "No Data Found", this)
        }
    }

    fun changeMobileNo(v: View) {

       /* val bottomSheetFragment = TrackingChangeMobileno(tripData!!, this)
        bottomSheetFragment.show(supportFragmentManager, "noChangeFrangment")*/
    }

    fun changeEndDate(v: View) {

        if (dayCount > 0) {
//            val bottomSheetFragment = UpdateEndDate(tripData!!, this,binding.endDate.text.toString())
//            bottomSheetFragment.show(supportFragmentManager, "updateEndDate")

            getDate()
        } else {
//            var abx: MyAlartBox =
//                MyAlartBox(this, resources.getString(R.string.noTrackingPlanMessage), "m")
//            abx?.show()
//

            TripPlanBox(this, resources.getString(R.string.noTrackingPlanMessage), this).show()
        }
    }

    override fun dataSubmited(message: String) {

        //   AddTripBox(this, message, "atr").show()

        var abx: MyAlartBox = MyAlartBox(this, message, "cl")
        abx?.show()
    }

    override fun hideKeybord() {


    }

    fun onBackTrip(v: View) {
        finish()

    }


    fun shareApp(v: View) {
        var path: Uri? = null

        val imgBitmap: Bitmap =
            PreferenceManager.drawableToBitmap(this?.getDrawable(R.drawable.share_en)!!)!!     // BitmapFactory.decodeResource(context.getResources(), R.drawable.whatsapp_trucks_logo)
        path = PreferenceManager.getImageUri(this, imgBitmap)

        var St: String = resources.getString(R.string.vehicle_number)
        var lt: String = resources.getString(R.string.linkForTrack)
        var shareDt: String =
            St + " : " + tripData?.truckNumber + "\n \n" + lt + "\n" + tripData?.publicLink
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "" + shareDt
            )
            putExtra(Intent.EXTRA_STREAM, path);
            type = "image/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, "TrucksUp")
        startActivity(shareIntent)
    }

    fun getDate() {
        val calendar = Calendar.getInstance()
        var mCalendarMin = java.util.Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this!!, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix


                updatedDate = "$formattedDate"
                endDateNew = "$formattedDate"
//                cardDate?.strokeColor= mContext?.getColor(R.color.blue)!!

               // createBill("eu")

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )


        var sd = binding.endDate.text.toString()


        val miniDay = PreferenceManager.getDateFormet("dd", "dd-MM-yyyy", sd + "").toInt()
        val miniMonth =
            PreferenceManager.getDateFormet("MM", "dd-MM-yyyy", sd + "").toString().toInt()
        val miniYear =
            PreferenceManager.getDateFormet("yyyy", "dd-MM-yyyy", sd + "").toString().toInt()
        mCalendarMin.set(miniYear, miniMonth - 1, miniDay + 1)

        // Show the DatePicker dialog
        val mCalendar = java.util.Calendar.getInstance()
        var cd = PreferenceManager.postLoadWindowDate(180, "dd-MM-yyyy")
        val maxDay = PreferenceManager.getDateFormet("dd", "dd-MM-yyyy", cd + "").toInt()
        val maxMonth =
            PreferenceManager.getDateFormet("MM", "dd-MM-yyyy", cd + "").toString().toInt()
        val maxYear =
            PreferenceManager.getDateFormet("yyyy", "dd-MM-yyyy", cd + "").toString().toInt()
        mCalendar.set(maxYear, maxMonth - 1, maxDay)
        datePickerDialog.datePicker.setMinDate(mCalendarMin.timeInMillis);
        datePickerDialog.datePicker.setMaxDate(mCalendar.timeInMillis)
        datePickerDialog.show()
    }

    fun changeNumber(key: String) {
        progressDailogBox?.show()
        var ED = PreferenceManager.getDateFormet(
            "yyyy-MM-dd'T'HH:mm:ss",
            "dd-MM-yyyy",
            updatedDate.toString() + ""
        ).toString()

      /*  var req: DriverDataChangeRequest = DriverDataChangeRequest(
            tripData?.tripId.toString(), ED,
            PreferenceManager.getPhoneNo(this!!),
            PreferenceManager.getRequestNo(), "", "enddate",
            PreferenceManager.getPhoneNo(this!!)
        )
        MyResponse().driverDataChange(key, req, this, this!!)*/
    }


    override fun changeDone(message: String, code: Int) {
        progressDailogBox?.dismiss()
        if (code == 200) {
            binding.endDate.text = updatedDate
            var abx: MyAlartBox = MyAlartBox(this, message, "cl")
            abx?.show()
        } else {

            //TripPlanBox(this, message, this).show()
        }

    }

    override fun buyPlan() {
       // createBill("p")
    }

   /* fun getPlanData(key: String) {
        var request: TrackingPlanListRequest = TrackingPlanListRequest(
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this),
            "tracking"
        )

        MyResponse().getTrackingPlanList(key, request, this, this)
    }*/

    override fun resentConsetData(message: String) {

    }

    override fun TrackingPlanList(
        data: ArrayList<DataTrackingPlan>,
        dataAdon: ArrayList<DataTrackingPlan>
    ) {


        progressDailogBox?.dismiss()

        if (dayCount == 0) {

            val day = 1

            val bottomSheetFragment = VehicalTrackingPlan(data, day, "t")
            bottomSheetFragment.show(supportFragmentManager, "trackingPlanFrangment")
        } else {
            var dayCountDate: String = DateHelp().countDayBetweenDate(
                binding.endDate.text.toString(),
                endDateNew,
                "dd-MM-yyyy"
            )
            var day: Int = dayCountDate.toString().toInt() - dayCount

            val bottomSheetFragment = VehicalTrackingPlan(data, day, "t")
            bottomSheetFragment.show(supportFragmentManager, "trackingPlanFrangment")
        }
    }


}