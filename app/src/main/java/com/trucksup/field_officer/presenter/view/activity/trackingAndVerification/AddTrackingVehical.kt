package com.trucksup.field_officer.presenter.view.activity.trackingAndVerification

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Rect
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityAddTrackingVehicalBinding
import com.trucksup.field_officer.presenter.common.ProgressDailogBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.utils.PreferenceManager.getCurantDateWithFormate
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.Locale
import java.util.regex.Pattern

class AddTrackingVehical : BaseActivity() {
    private lateinit var binding: ActivityAddTrackingVehicalBinding
    var progressDailogBox: ProgressDailogBox? = null
    private var isMasked = false
    var isNumberVerified: Boolean = false
    var isMask: Boolean = true

    var countDownTimer: CountDownTimer? = null
    var isTimer: Boolean = false
    var appSignatureHelper: String? = ""
    var maxDay: Int = 0;
    var otpCode: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        // appSignatureHelper = SignatureHelper(this).appSignature.toString()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_tracking_vehical)
        maxDay = intent.getIntExtra("day", 0)
        progressDailogBox = ProgressDailogBox(this)
        inst()

    }


    /* fun createBill() {
         var errorType:String=""
         var request: PaymentGetWayTokenRequest = PaymentGetWayTokenRequest(PreferenceManager.getAccesKey(this),PreferenceManager.getAccesPassword(this),PreferenceManager.getAccesUserAgaint(this),PreferenceManager.getAccesUserName(this),PreferenceManager.getAccesUserInssur(this))
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

                     formsubmit("Bearer " + data?.accessToken.toString())

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
                 params["x-api-key"] = PreferenceManager.getAccesHeader(this@AddTrackingVehical).toString()
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


    fun inst() {

        binding.startDate.text = getCurantDateWithFormate("dd-MM-yyyy")

        binding.mobileNumber.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

                if (isMasked) {
                    // Remove mask
                    binding.mobileNumber.transformationMethod = null
                    binding.mobileNumber.setSelection(binding.mobileNumber.text.length)
                    isMasked = false

                }

            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                val password = binding.mobileNumber.text.toString()
                if (password.length == 10) {

                    val confirmPassword = binding.mobileNumberCon.text.toString()
                    if (password == confirmPassword) {
                        binding.verifiedIcon?.visibility = View.VISIBLE
                        isNumberVerified = true
                        binding.mobileNumberCon.setTextColor(resources.getColor(R.color.green))
                    } else {

                        binding.verifiedIcon?.visibility = View.GONE
                        isNumberVerified = false
                        binding.mobileNumberCon.setTextColor(resources.getColor(R.color.red))

                    }


                } else {
                    binding.verifiedIcon?.visibility = View.GONE
                    isNumberVerified = false
                    binding.mobileNumberCon.setTextColor(resources.getColor(R.color.red))
                }
            }
        })


        binding.mobileNumberCon.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // maskNumber()

                if (!isMasked) {
                    // Remove mask
                    binding.mobileNumber.transformationMethod = AsteriskTransformationMethod()
                    isMasked = true

                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val confirmPassword = binding.mobileNumberCon.text.toString()

                if (confirmPassword.length == 10) {

                    val password = binding.mobileNumber.text.toString()

                    if (password == confirmPassword) {
                        binding.verifiedIcon?.visibility = View.VISIBLE
                        isNumberVerified = true
                        binding.mobileNumberCon.setTextColor(resources.getColor(R.color.green))
                    } else {


                        binding.verifiedIcon?.visibility = View.GONE
                        isNumberVerified = false
                        binding.mobileNumberCon.setTextColor(resources.getColor(R.color.red))
                       /* ErrorHelp().showErrorbox(
                            this@AddTrackingVehical!!,
                            resources.getString(R.string.mobileNoMatch),
                            binding.mobileNumberCon!!
                        )*/
                    }
                } else {
                    LoggerMessage.LogErrorMsg("Mobile", "Leanth < 10")

                    binding.verifiedIcon?.visibility = View.GONE
                    isNumberVerified = false
                    binding.mobileNumberCon.setTextColor(resources.getColor(R.color.red))

                }
            }
        })


    }


/*
    override fun fromCity(
        value: String,
        valueState: String,
        id: String,
        type: String,
        valueHi: String,
        valueStateHi: String
    ) {
        progressDailogBox?.dismiss()
        binding.from.setText(value)

    }

    override fun toCity(
        value: String,
        valueState: String,
        id: String,
        type: String,
        valueHi: String,
        valueStateHi: String
    ) {
        progressDailogBox?.dismiss()
        binding.to.setText(value)
    }

    override fun cityState(
        cityEng: String,
        cityHi: String,
        stateEn: String,
        stateHi: String,
        id: String,
        type: String
    ) {
        TODO("Not yet implemented")
    }

    override fun translate(text: String, state: String, TranslateText: String, type: String) {
        TODO("Not yet implemented")
    }

    override fun TranslateError(error: String) {
        TODO("Not yet implemented")
    }*/
/*
    fun getFrom(v: View) {
        progressDailogBox?.show()
        var masterAPI: MasterAPI = MasterAPI()
        masterAPI.getGoodleAPI(this, binding.from!!, "f", "f", "", "")
    }

    fun getTo(v: View) {
        progressDailogBox?.show()
        var masterAPI: MasterAPI = MasterAPI()
        masterAPI.getGoodleAPI(this, binding.to!!, "t", "t", "", "")
    }*/


    fun getDate(v: View) {
        var calendar = Calendar.getInstance()
        var mCalendar = java.util.Calendar.getInstance()
        var mCalendarMin = java.util.Calendar.getInstance()
        var type: String = "s"

        var id: Int = v.id

        if (id == R.id.endDate) {
            type = "e"

            if (TextUtils.isEmpty(binding.startDate.text)) {

                LoggerMessage.onSNACK(
                    binding.from,
                    resources.getString(R.string.enterStartDate),
                    this
                )

                return
            }
        }
        if (id == R.id.startDate) {
            type = "s"


        }

        val datePickerDialog = DatePickerDialog(
            this, { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix

                if (type == "e") {
                    binding.endDate.text = "$formattedDate"
                } else {
                    binding.startDate.text = "$formattedDate"
                }

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog

        var cd = ""
        cd = PreferenceManager.postLoadWindowDate(90, "dd-MM-yyyy")
//    if (type=="e") {
        var sd = binding.startDate.text.toString()

//        LoggerMessage.LogErrorMsg("date From ","Day === "+PreferenceManager.getNextDateFromADate(maxDay,"dd-MM-yyyy",sd)+" From date "+sd)
//        LoggerMessage.LogErrorMsg("Max day","day ========= "+maxDay)
//        if (maxDay > 0) {
//            cd = PreferenceManager.getNextDateFromADate(maxDay,"dd-MM-yyyy",sd)
//        } else {
//            cd = PreferenceManager.postLoadWindowDate(90, "dd-MM-yyyy")
//        }
//    }
//    else{
//        cd = PreferenceManager.postLoadWindowDate(90, "dd-MM-yyyy")
//    }

        val maxDay = PreferenceManager.getDateFormet("dd", "dd-MM-yyyy", cd + "").toInt()
        val maxMonth =
            PreferenceManager.getDateFormet("MM", "dd-MM-yyyy", cd + "").toString().toInt()
        val maxYear =
            PreferenceManager.getDateFormet("yyyy", "dd-MM-yyyy", cd + "").toString().toInt()
        mCalendar.set(maxYear, maxMonth - 1, maxDay)


        if (type == "e") {


            var sd = binding.startDate.text.toString()


            val maxDay = PreferenceManager.getDateFormet("dd", "dd-MM-yyyy", sd + "").toInt()
            val maxMonth =
                PreferenceManager.getDateFormet("MM", "dd-MM-yyyy", sd + "").toString().toInt()
            val maxYear =
                PreferenceManager.getDateFormet("yyyy", "dd-MM-yyyy", sd + "").toString().toInt()
            mCalendarMin.set(maxYear, maxMonth - 1, maxDay)

            datePickerDialog.datePicker.setMinDate(mCalendarMin.timeInMillis);
        } else {


            datePickerDialog.datePicker.setMinDate(System.currentTimeMillis() - 1000);
        }

        datePickerDialog.datePicker.setMaxDate(mCalendar.timeInMillis)
        datePickerDialog.show()
    }


   /* fun getToken() {
        progressDailogBox?.show()
        var authData: OtpAuthData =
            PreferenceManager.stringToAuthNew(PreferenceManager.getAuthOtpNew(this))
        var request: PaymentGetWayTokenRequest = PaymentGetWayTokenRequest(
            authData.SKey,
            authData.password,
            "TrucksupOtpAudience",
            authData.userName,
            "TrucksupOtpIssuer"
        )
        Log.e(" voley main url ", PreferenceManager.getServerUrl(this))
        //var loadDetails: LoadDetails?= LoadDetails(0,0,0,PreferenceManager.getPhoneNo(context))

        var images = ArrayList<BannerImages>()
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
        var jsonObejct: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            PreferenceManager.getServerUrl(this) + "JwtAuth/api/Auth/GenerateJWTtoken",
            `object`,
            com.android.volley.Response.Listener { response ->
                val gson = Gson()
                var data: PaymentGetWayTokenResponse? =
                    gson.fromJson(response.toString(), PaymentGetWayTokenResponse::class.java)
//                Log.e("The user name of voley ", data?.profileDetails?.profileName.toString())
                Log.e("The Response of voley token ", response.toString())

                if (VollyRequests.mStatusCode == 200) {


                    // getData("Bearer "+data?.accessToken.toString())


                } else {
                    progressDailogBox?.dismiss()
                    // myTripResults.myTripError("Some thing server error")
                }


            },
            VollyRequests.volleyError("JwtAuth/api/Auth/GenerateJWTtoken", this)
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = java.util.HashMap()
                params["x-api-key"] = authData.headerKey
                params["Content-Type"] = "application/json"
                return params
            }

            override fun parseNetworkResponse(response: NetworkResponse?): com.android.volley.Response<JSONObject>? {
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
    }


    fun deleteCountry(phone: String): String {
        val phoneInstance = PhoneNumberUtil.getInstance()
        try {
            val phoneNumber = phoneInstance.parse(phone, null)
            return phoneNumber?.nationalNumber?.toString() ?: phone
        } catch (_: Exception) {
        }
        return phone
    }
*/

    fun summitData(v: View) {


      /*  if (validation()) {
            var datD: String = DateHelp().countDayBetweenDate(
                binding.startDate.text.toString(),
                binding.endDate.text.toString(),
                "dd-MM-yyyy"
            )
            var DifDate: Int = datD.toInt()

            if (DifDate > maxDay) {
                var abx: MyAlartBox =
                    MyAlartBox(this, resources.getString(R.string.addTripNoPlanMess), "m")
                abx?.show()
            } else {
                createBill()
            }


        }*/


    }

   /* fun verifyNumber(v: View) {

        if (binding.mobileNumber.tag == "y") {
            if (TextUtils.isEmpty(binding.mobileNumber.text)) {

                LoggerMessage.onSNACK(
                    binding.from,
                    resources.getString(R.string.enter_mobile_no),
                    this
                )

                return
            }
            if (binding.mobileNumber.text.length < 10) {
                binding.mobileNumber.error = resources.getString(R.string.enter_right_number_v)
                return
            }
            if (isValidPhoneNumber(binding.mobileNumber?.text.toString()) == false) {

                LoggerMessage.onSNACK(
                    binding.from,
                    resources.getString(R.string.enter_right_number_v),
                    this
                )

                return
            }

            getToken()
        }


    }*/

    fun formsubmit(key: String) {

        var ED = PreferenceManager.getDateFormet(
            "yyyy-MM-dd'T'HH:mm:ss",
            "dd-MM-yyyy",
            binding.endDate.text.toString() + ""
        ).toString()

        var SD = PreferenceManager.getDateFormet(
            "yyyy-MM-dd'T'HH:mm:ss",
            "dd-MM-yyyy",
            binding.startDate.text.toString() + ""
        ).toString()
       /* var payLoad: Payloads = Payloads(
            binding.mobileNumber.text.toString(),
            ED,
            SD,
            "0,0",
            binding.to.text.toString(),
            "0,0",
            binding.from.text.toString(),
            binding.truckNumber.text.toString()
        )
        var req: SubmitTripRequest = SubmitTripRequest(
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getPhoneNo(this),
            binding.mobileNumber.text.toString(),
            payLoad
        )
        MyResponse().submitTrip(key, req, this, this)*/


    }

    fun validation(): Boolean {


        if (TextUtils.isEmpty(binding.mobileNumber.text)) {

            LoggerMessage.onSNACK(binding.from, resources.getString(R.string.enterDriverNo), this)

            return false
        }

        if (isNumberVerified == false) {


            LoggerMessage.onSNACK(binding.from, resources.getString(R.string.mobileNoMatch), this)
            return false
        }

        if (TextUtils.isEmpty(binding.truckNumber.text)) {


            LoggerMessage.onSNACK(binding.from, resources.getString(R.string.inputTruckNo), this)
            return false
        }
        if (binding.truckNumber.text.length < 8) {
            LoggerMessage.onSNACK(
                binding.truckNumber,
                resources.getString(R.string.enterCommercialVehical),
                this
            )
            return false
        }

        if (TextUtils.isEmpty(binding.startDate.text)) {

            LoggerMessage.onSNACK(binding.from, resources.getString(R.string.enterStartDate), this)

            return false
        }

        if (TextUtils.isEmpty(binding.endDate.text)) {

            LoggerMessage.onSNACK(binding.from, resources.getString(R.string.enterEndDate), this)

            return false
        }





        return true
    }

  /*  override fun deleteTruck(po: Int) {

    }

    override fun dataSubmited(message: String, message1: String) {
        progressDailogBox?.dismiss()
        AddTripBox(this, message, "atr").show()

        val parameters = Bundle().apply {
            this.putString("mobile", PreferenceManager.getPhoneNo(this@AddTrackingVehical!!))
            this.putString("truck", binding.truckNumber.text.toString() + "")
        }
        FirebaseAnalytics().CreateCustomEventFullData("add_tracking", parameters)

    }

    override fun dataError(error: String) {
        progressDailogBox?.dismiss()
    }*/

    fun hideKeyboard() {
        if (isKeyboardOpened()) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } else {
            Log.e("Keyboard", "Not open")
        }
    }

    fun Activity.isKeyboardOpened(): Boolean {
        val r = Rect()

        val activityRoot = getActivityRoot()
        val visibleThreshold = dip(100)

        activityRoot.getWindowVisibleDisplayFrame(r)

        val heightDiff = activityRoot.rootView.height - r.height()

        return heightDiff > visibleThreshold;
    }

    fun Activity.getActivityRoot(): View {
        return (findViewById<ViewGroup>(android.R.id.content)).getChildAt(0);
    }

    fun dip(value: Int): Int {
        return (value * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun isValidPhoneNumber(phone: String): Boolean {
//        var INDIAN_MOBILE_NUMBER_PATTERN="^[6-9]\\d{9}$"
//        val pattern = Pattern.compile(INDIAN_MOBILE_NUMBER_PATTERN)
//        val matcher = pattern.matcher(phone)
//        return matcher.matches()

        val p = Pattern.compile("([6,7,8,9][0-9]{0,9})")
        val m = p.matcher(phone)
        return (m.find() && m.group() == phone)
    }

    @SuppressLint("SuspiciousIndentation")
//    private fun checkVehicleNumber(vehicleNumber: String): Boolean {
//        var tt :String=vehicleNumber.toString().trim()
//
//      var tn:String =tt.substring(0,4)
//        val regex = "^[A-Z]{2}[ -]?[0-9]{1}[ -]?[0-9|A-Z]{1}$"
//        return tn.matches(regex.toRegex())
//    }

    fun backScreen(v: View) {
        finish()
    }



    class AsteriskTransformationMethod : PasswordTransformationMethod() {
        override fun getTransformation(
            source: CharSequence,
            view: android.view.View
        ): CharSequence {
            return object : CharSequence {
                override val length: Int
                    get() = source.length

                override fun get(index: Int): Char {
                    return 'X'
                }

                override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
                    return "#".repeat(endIndex - startIndex)
                }
            }
        }
    }
}