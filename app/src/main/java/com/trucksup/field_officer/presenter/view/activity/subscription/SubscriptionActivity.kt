package com.trucksup.field_officer.presenter.view.activity.subscription

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.logistics.trucksup.modle.AddonsData
import com.logistics.trucksup.modle.Broker
import com.logistics.trucksup.modle.BrokerPlan
import com.logistics.trucksup.modle.CurantPlanData
import com.logistics.trucksup.modle.OwnerFaq
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivitySubscriptionBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.other.TokenViewModel
import com.trucksup.field_officer.presenter.view.activity.subscription.model.MyPlanData
import com.trucksup.field_officer.presenter.view.activity.subscription.model.NavigationSubData
import com.trucksup.field_officer.presenter.view.activity.subscription.model.PlanRequest
import com.trucksup.field_officer.presenter.view.activity.subscription.model.payBillResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionActivity : BaseActivity(), PaySubscribtion, PlanCantroler {
    private lateinit var binding: ActivitySubscriptionBinding

    private var mViewModel: SubscriptionViewModel? = null
    private var mTokenViewModel: TokenViewModel? = null
    private var handler: Handler? = null
    private var brokerData: Broker? = null
    private var planStatus: String? = ""
    private val addOnsData = ArrayList<AddonsData>()
    private val addOnsDataTacking = ArrayList<AddonsData>()
    private var addonDataType: String = "addon"
    private var isExpire: Boolean = false
    private var loadCount: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription)

        mViewModel = ViewModelProvider(this)[SubscriptionViewModel::class.java]

        setupObserver()

        setPlanList(arrayListOf())
    }

    private fun setupObserver() {
        binding.ivBack.setOnClickListener {
          onBackPressed()
        }

        mViewModel?.resultSubPlanListLD?.observe(this@SubscriptionActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@SubscriptionActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.message != null) {
                    /* val abx = FinaceSubmitBox(
                         this, responseModel.success.message,
                         responseModel.success.message1, "cl"
                     )
                     abx.show()*/

                } else {

                }
            }
        }

        mViewModel?.imgUploadResultLD?.observe(this@SubscriptionActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@SubscriptionActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.imagekey != null) {


                } else {

                }
            }
        }
    }

    private fun setPlanList(list: ArrayList<Broker>) {

        val s: Int = list.size - 1
        for (i in 0..s) {
            if (list[i].currentPlan.toString().toLowerCase().equals("y")) {
                val upData = Broker(
                    list[i].actualAmount,
                    list[i].planList,
                    list[i].createdAt,
                    list[i].createdBy,
                    list[i].currentPlan,
                    list[i].description,
                    list[i].disabledPlan,
                    list[i].gstPercent,
                    list[i].headerId,
                    list[i].id,
                    list[i].isVisible,
                    list[i].mrp,
                    list[i].planAmount,
                    list[i].recommended,
                    list[i].sellingPrice,
                    list[i].subType,
                    list[i].subscriptionName,
                    list[i].tags,
                    list[i].userType,
                    list[i].validityDays,
                    true,
                    list[i].freebies,
                    list[i].addLoadinPkg,
                    list[i].perDayAmount,
                    list[i].discount
                )
                list.set(i, upData)
                break
            } else {
                if (list.get(i).recommended.toString()
                        .toLowerCase() == "y" && list.get(i).currentPlan.toString()
                        .toLowerCase() == "n" && list.get(i).isClick == false
                ) {
                    val upData: Broker = Broker(
                        list.get(i).actualAmount,
                        list.get(i).planList,
                        list.get(i).createdAt,
                        list.get(i).createdBy,
                        list.get(i).currentPlan,
                        list.get(i).description,
                        list.get(i).disabledPlan,
                        list.get(i).gstPercent,
                        list.get(i).headerId,
                        list.get(i).id,
                        list.get(i).isVisible,
                        list.get(i).mrp,
                        list.get(i).planAmount,
                        list.get(i).recommended,
                        list.get(i).sellingPrice,
                        list.get(i).subType,
                        list.get(i).subscriptionName,
                        list.get(i).tags,
                        list.get(i).userType,
                        list.get(i).validityDays,
                        true,
                        list.get(i).freebies,
                        list.get(i).addLoadinPkg,
                        list.get(i).perDayAmount,
                        list.get(i).discount
                    )
                    list.set(i, upData)
                    break
                }
            }


        }

        //  Collections.reverse(mainList);

        val subsAdapter = SubscriptionAdapterTest(this, list!!, this)

        binding.planView.layoutManager = GridLayoutManager(this, 2)

        binding.planView.adapter = subsAdapter
        subsAdapter.notifyDataSetChanged()
    }


    private fun setPlanDetails(data: ArrayList<BrokerPlan>) {

        val plandetailsAdapter = PlanDetailsAdapter(this, data)
        binding.plandetailsList.layoutManager = LinearLayoutManager(this)
        binding.plandetailsList.adapter = plandetailsAdapter
        plandetailsAdapter.notifyDataSetChanged()
    }

    @SuppressLint("SuspiciousIndentation")
    override fun subClick(data: Broker) {
        brokerData = data

        LoggerMessage.LogErrorMsg("Disable plan", "====== " + data.disabledPlan)


        if (data != null && data.discount != null && !data.discount.isEmpty() && data.discount?.get(
                0
            )?.message != null && !TextUtils.isEmpty(data.discount.get(0).message)
        ) {
            binding.planMsg.visibility = View.VISIBLE

            if (PreferenceManager.getLanguage(this) == "en") {
                binding.planMsg.text = data.discount.get(0).message.toString()
            } else {
                binding.planMsg.text = data.discount.get(0).messagehindi.toString()
            }
        } else {
            binding.planMsg.visibility = View.GONE
        }

        binding.plandiscrib.text =
            data.subscriptionName + " " + resources.getString(R.string.planBenefits)
        Log.e(
            "is Active Plan",
            "======= yes\n plan status == " + planStatus.toString() + " Plan is " + brokerData?.currentPlan
        )

        if (brokerData?.currentPlan.toString().toLowerCase().equals("y")) {

            if (planStatus.toString().toLowerCase() == "active") {


                if (data.subscriptionName.toString().toLowerCase().contains("welcome")) {
                    binding.planExLayout.visibility = View.GONE
                } else {

                    if (data.disabledPlan == "disabled") {

                        binding.planExLayout.visibility = View.VISIBLE

                    } else {
                        binding.planExLayout.visibility = View.VISIBLE

                    }

                }


            } else if (planStatus.toString().toLowerCase() == "expired") {
                if (data.subscriptionName.toString().toLowerCase().contains("welcome")) {
                    binding.planExLayout.visibility = View.GONE

                } else {
                    binding.planExLayout.visibility = View.VISIBLE

                    //  binding.upgradIang.visibility = View.GONE
                    binding.planExpText.text = resources.getString(R.string.membershipExpired)
                    binding.planExpText.setTextColor(resources.getColor(R.color.text_red))
                    binding.planExLayout.visibility = View.VISIBLE
                }
            } else {
                if (data.subscriptionName.toString().toLowerCase().contains("welcome")) {
                    binding.planExLayout.visibility = View.GONE

                } else {
                    binding.planExLayout.visibility = View.VISIBLE

                    //  binding.upgradIang.visibility = View.GONE
                    binding.planExpText.text = resources.getString(R.string.membershipExpired)
                    binding.planExpText.setTextColor(resources.getColor(R.color.text_red))
                    ///   binding.planExLayout.visibility=View.VISIBLE
                }
            }
        } else {


            if (data.subscriptionName.toString().toLowerCase().contains("welcome")) {
                binding.planExLayout.visibility = View.GONE

            } else {


                if (data.disabledPlan == "disabled") {
                    binding.planExLayout.visibility = View.VISIBLE

                } else {

                    if (planStatus.toString().toLowerCase() == "active" || planStatus.toString()
                            .toLowerCase() == "expired"
                    ) {

                        LoggerMessage.LogErrorMsg("Plan is", " active or expired")
                        if (data.subscriptionName.toString().toLowerCase().contains("welcome")) {
                            binding.planExLayout.visibility = View.GONE
                        } else {

                            if (TextUtils.isEmpty(binding.planExpText.text)) {
                                binding.planExLayout.visibility = View.GONE
                            } else {
                                binding.planExLayout.visibility = View.VISIBLE
                            }
                        }
                    } else {
                        LoggerMessage.LogErrorMsg("Plan is", " not active or not expired")
                        binding.planExLayout.visibility = View.GONE
                    }
                }
                // binding.planExLayout.visibility=View.GONE
            }



            if (data.freebies.size > 0) {
                var listD = ArrayList<AddonsData>()
                listD = data.freebies as ArrayList<AddonsData>
                addOnsData.clear()

                val s: Int = listD.size - 1
                for (i in 0..s) {

                    addonDataType = "free"
                    val d: AddonsData = AddonsData(
                        listD.get(i).actualPrice,
                        listD.get(i).cgstPercent,
                        listD.get(i).createdBy,
                        listD.get(i).gstInclude,
                        listD.get(i).gstPercent,
                        listD.get(i).imageurl,
                        listD.get(i).isAddon,
                        listD.get(i).isIncluded,
                        listD.get(i).matrixName,
                        listD.get(i).mid,
                        listD.get(i).moduleName,
                        listD.get(i).mrp,
                        listD.get(i).perUnit,
                        listD.get(i).pkgUnit,
                        listD.get(i).planId,
                        listD.get(i).sellingPrice,
                        listD.get(i).sgstPercent,
                        listD.get(i).subType,
                        listD.get(i).validityDays,
                        0,
                        "free",
                        listD.get(i).notes,
                        listD.get(i).unitText
                    )
                    addOnsData.add(d)
                }


            } else {
                addonDataType = "addon"
            }

        }

        setPlanDetails(data.planList as ArrayList<BrokerPlan>)


    }

    /*fun createBill() {
        val errorType: String? = ""
        val request: PaymentGetWayTokenRequest = PaymentGetWayTokenRequest(
            PreferenceManager.getAccesKey(this),
            PreferenceManager.getAccesPassword(this),
            PreferenceManager.getAccesUserAgaint(this),
            PreferenceManager.getAccesUserName(this),
            PreferenceManager.getAccesUserInssur(this)
        )
        Log.e(" voley main url ", PreferenceManager.getServerUrl(this))

        //val loadDetails: LoadDetails?= LoadDetails(0,0,0,PreferenceManager.getPhoneNo(context))

        val images = ArrayList<BannerImages>()
        val requestQueue = Volley.newRequestQueue(this)
        val `object`: JSONObject = JSONObject()
        val gson = Gson()
        val requestObject: String = gson.toJson(request)
        try {
            `object` = JSONObject(requestObject)
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        Log.e(" voley req ", `object`.toString())
        val jsonObejct: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            PreferenceManager.getServerUrl(this) + "JwtAuth/api/Auth/GenerateJWTtoken",
            `object`,
            Response.Listener { response ->
                val gson = Gson()
                val data: PaymentGetWayTokenResponse? =
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
            // VollyRequests.volleyError("JwtAuth/api/Auth/GenerateJWTtoken", this)

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
                params["x-api-key"] = PreferenceManager.getAccesHeader(this@Subscription).toString()

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
        val request = PlanRequest(
            "TUPlatform",
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getPhoneNo(this),
            "" + PreferenceManager.getServerDateUtc(),
            PreferenceManager.getLanguage(this)
        )

        mViewModel?.subscriptionPlanList(request, key)
    }


    override fun planData(
        data: ArrayList<Broker>,
        dataAddOns: ArrayList<AddonsData>,
        faqData: ArrayList<OwnerFaq>,
        currentPlanDetails: CurantPlanData,
        vehicleverification: ArrayList<AddonsData>
    ) {

        binding.nestedScrollView.visibility = View.VISIBLE
        // binding.hindBt.visibility = View.VISIBLE

        val listD = dataAddOns
        val listT = vehicleverification
        addOnsData.clear()
        addOnsDataTacking.clear()
        if (currentPlanDetails != null) {
            if (currentPlanDetails.previousPlan != null && TextUtils.isEmpty(currentPlanDetails.previousPlan?.planName) == false) {

                planStatus = currentPlanDetails.previousPlan?.planStatus.toString()
                Log.e("Update date", "Date ===" + currentPlanDetails.previousPlan?.upGradeDate)
                var remaningLoad: String = ""

                if (currentPlanDetails.previousPlan?.subsPendingLoads != null) {
                    remaningLoad = currentPlanDetails.previousPlan?.subsPendingLoads!!
                }
                var tl = 0
                var tl1 = 0
                if (currentPlanDetails.previousPlan?.subsPendingLoads != null) {
                    tl = currentPlanDetails.previousPlan?.subsPendingLoads.toString().toInt()
                }
                if (currentPlanDetails.previousPlan?.freeLoadCount != null) {
                    tl1 = currentPlanDetails.previousPlan?.freeLoadCount.toString().toInt()
                }

                val tt = tl + tl1

                remaningLoad = tt.toString()


                if (currentPlanDetails.previousPlan?.planStatus.toString().toLowerCase() == "active"
                ) {
                    binding.planExpText.text = currentPlanDetails.previousPlan?.text2

                    binding.planExLayout.visibility = View.VISIBLE
                    isExpire = false

                    loadCount = currentPlanDetails.previousPlan?.subsPendingLoads

                    val subData = NavigationSubData(
                        true,
                        currentPlanDetails.previousPlan?.planName!!,
                        currentPlanDetails.previousPlan?.endDate!!,
                        remaningLoad
                    )
                    val jsonStringSub = Gson().toJson(subData)
                    PreferenceManager.setSubData(jsonStringSub, this)

                } else if (currentPlanDetails.previousPlan?.planStatus.toString()
                        .toLowerCase() == "expired"
                ) {
                    isExpire = true
                    binding.planExpText.text = "Membership Expired"
                    binding.planExpText.setTextColor(resources.getColor(R.color.text_red))
                    binding.planExLayout.visibility = View.VISIBLE


                    val subData: NavigationSubData = NavigationSubData(
                        true,
                        currentPlanDetails.welcomePlan?.planName!!,
                        currentPlanDetails.welcomePlan?.endDate!!,
                        currentPlanDetails.welcomePlan!!.freeLoadCount
                    )
                    val jsonStringSub = Gson().toJson(subData)
                    PreferenceManager.setSubData(jsonStringSub, this)


                } else {
                    isExpire = false

                    binding.planExLayout.visibility = View.GONE

                }
            } else {
                var remaningLoad = ""
                planStatus = currentPlanDetails.welcomePlan?.planStatus.toString()

                if (currentPlanDetails.welcomePlan?.subsPendingLoads != null) {
                    remaningLoad = currentPlanDetails.welcomePlan?.subsPendingLoads!!
                }
                var tl = 0
                var tl1 = 0
                if (currentPlanDetails.welcomePlan?.subsPendingLoads != null) {
                    tl = currentPlanDetails.welcomePlan?.subsPendingLoads.toString().toInt()
                }
                if (currentPlanDetails.welcomePlan?.freeLoadCount != null) {
                    tl1 = currentPlanDetails.welcomePlan?.freeLoadCount.toString().toInt()
                }

                val tt: Int = tl + tl1

                remaningLoad = tt.toString()
                val subData = NavigationSubData(
                    true,
                    currentPlanDetails.welcomePlan?.planName!!,
                    currentPlanDetails.welcomePlan?.endDate!!,
                    remaningLoad
                )
                val jsonStringSub = Gson().toJson(subData)
                PreferenceManager.setSubData(jsonStringSub, this)
                binding.planExLayout.visibility = View.GONE
            }
        } else {
//            binding.upgreadBtLayout.visibility = View.GONE
//            binding.upgradIangCard.visibility = View.GONE
//            binding.activCard?.visibility=View.VISIBLE
//            binding.disableContinu?.visibility=View.GONE
        }

        val s: Int = listD.size - 1
        for (i in 0..s) {

            val d = AddonsData(
                listD.get(i).actualPrice,
                listD.get(i).cgstPercent,
                listD.get(i).createdBy,
                listD.get(i).gstInclude,
                listD.get(i).gstPercent,
                listD.get(i).imageurl,
                listD.get(i).isAddon,
                listD.get(i).isIncluded,
                listD.get(i).matrixName,
                listD.get(i).mid,
                listD.get(i).moduleName,
                listD.get(i).mrp,
                listD.get(i).perUnit,
                listD.get(i).pkgUnit,
                listD.get(i).planId,
                listD.get(i).sellingPrice,
                listD.get(i).sgstPercent,
                listD.get(i).subType,
                listD.get(i).validityDays,
                0,
                "addon",
                listD.get(i).notes,
                listD.get(i).unitText
            )
            addOnsData.add(d)
        }

        // addOnsDataTacking.clear()

        LoggerMessage.LogErrorMsg("Data Size", "Addon Data Size====== " + listT.size)
        val t: Int = listT.size - 1
        for (i in 0..t) {

            val d = AddonsData(
                listT.get(i).actualPrice,
                listT.get(i).cgstPercent,
                listT.get(i).createdBy,
                listT.get(i).gstInclude,
                listT.get(i).gstPercent,
                listT.get(i).imageurl,
                listT.get(i).isAddon,
                listT.get(i).isIncluded,
                listT.get(i).matrixName,
                listT.get(i).mid,
                listT.get(i).moduleName,
                listT.get(i).mrp,
                listT.get(i).perUnit,
                listT.get(i).pkgUnit,
                listT.get(i).planId,
                listT.get(i).sellingPrice,
                listT.get(i).sgstPercent,
                listT.get(i).subType,
                listT.get(i).validityDays,
                0,
                "addon",
                listT.get(i).notes,
                listT.get(i).unitText
            )
            addOnsDataTacking.add(d)
        }


        dismissProgressDialog()

        setPlanList(data)
        setFaqList(faqData)

    }

    private fun setFaqList(faqData: ArrayList<OwnerFaq>) {
        val adapter = PlanFaqAdaptor(this, faqData)

        binding.faqsList.layoutManager = LinearLayoutManager(this)
        binding.faqsList.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun myPlanData(data: ArrayList<MyPlanData>, key: String) {
    }

    override fun planBill(data: payBillResponse) {
    }

    override fun planError(error: String) {
        dismissProgressDialog()
        LoggerMessage.onSNACK(binding.planView, "" + error, this)
    }


    override fun renewPlan(data: MyPlanData, position: Int) {
        renew()
    }

   /* override fun onResume() {
        super.onResume()
        showProgressDialog(this, false)
        // createBill()
    }*/

    private fun renew() {
        binding.nestedScrollView.fullScroll(View.FOCUS_UP);
        binding.nestedScrollView.scrollTo(0, binding.nestedScrollView.bottom)

    }


    fun openTranHist(v: View) {

        /*  val intent: Intent = Intent(this, TransactionHistory::class.java)
          startActivity(intent)*/
    }


}