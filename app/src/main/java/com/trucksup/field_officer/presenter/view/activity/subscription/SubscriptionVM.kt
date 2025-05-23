package com.trucksup.field_officer.presenter.view.activity.subscription

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.logistics.trucksup.modle.PlanResponse
import com.trucksup.field_officer.data.model.image.ImageResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.view.activity.subscription.model.PlanRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultSubPlanList: MutableLiveData<ResponseModel<PlanResponse>> =
        MutableLiveData<ResponseModel<PlanResponse>>()
    val resultSubPlanListLD: LiveData<ResponseModel<PlanResponse>> =
        resultSubPlanList


    fun subscriptionPlanList(
        request: PlanRequest,
        key: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getSubscriptionPlanData(key,request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultSubPlanList.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultSubPlanList.postValue(ResponseModel(success = response.value))
                }
            }
        }


    }

    /* fun planList(
         tokenBody: PlanRequest,
         context: Context,
         myTripResults: PlanCantroler,
         key: String
     ) {
         var dataLiat = ArrayList<Broker>()
         var faq = ArrayList<OwnerFaq>()
         var dataAddOns = ArrayList<AddonsData>()
         var dataAddOnsTracking = ArrayList<AddonsData>()
         val requestQueue = Volley.newRequestQueue(context)
         var curntPlandataP: CurantPlan =
             CurantPlan("", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
         var curntPlandataW: CurantPlan =
             CurantPlan("", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
         var curntPlanDt: CurantPlanData = CurantPlanData(curntPlandataW, curntPlandataP)

         var `object`: JSONObject = JSONObject()
         val gson = Gson()
         val requestObject: String = gson.toJson(tokenBody)
         try {
             `object` = JSONObject(requestObject)
         } catch (e: JSONException) {
             // TODO Auto-generated catch block
             e.printStackTrace()
         }
         Log.e("Call request", ">> " + `object`)

         val jsonObejct: JsonObjectRequest = object : JsonObjectRequest(
             Method.POST,
             PreferenceManager.getServerUrl(context) + "TrucksUpAPIGateway/gateway/plans",
             `object`,
             com.android.volley.Response.Listener { response ->


                 Log.e("Plan Data", "== " + response.toString())
                 val gson = Gson()
                 var data: PlanResponse? =
                     gson.fromJson(response.toString(), PlanResponse::class.java)

                 Log.e("Call create ", response.toString())
                 if (data?.status == 200) {

                     if (data?.addons != null && data.addons.size > 0) {
                         dataAddOns = data?.addons as ArrayList<AddonsData>

                         Log.e("Addon data", "Size====" + dataAddOns.size)
                     }
                     if (data?.vehicleverification != null && data.vehicleverification.size > 0) {
                         dataAddOnsTracking = data?.vehicleverification as ArrayList<AddonsData>

                         Log.e("Addon data", "Size====" + dataAddOns.size)
                     }

                     if (data?.currentPlanDetails != null) {
                         if (data?.currentPlanDetails?.previousPlan != null) {
                             curntPlandataP = data?.currentPlanDetails?.previousPlan!!
                         }

                         if (data?.currentPlanDetails?.welcomePlan != null) {
                             curntPlandataW = data?.currentPlanDetails?.welcomePlan!!
                         }

                         curntPlanDt = CurantPlanData(curntPlandataW, curntPlandataP)
                     }

                     if (PreferenceManager.getRoal(context) == context.resources.getString(R.string.owner)) {

                         if (data?.owner != null) {
                             dataLiat = data?.owner as ArrayList<Broker>
                             Log.e("Owner data", "=========== true")
                             Log.e("Addon data", "Size====" + dataAddOns.size)
                             myTripResults.planData(
                                 dataLiat,
                                 dataAddOns,
                                 data.ownerFaq as ArrayList<OwnerFaq>,
                                 curntPlanDt,
                                 dataAddOnsTracking
                             )
                         }
                     } else {
                         if (data?.broker != null) {
                             Log.e("Broker data", "=========== true")
                             dataLiat = data?.broker as ArrayList<Broker>
                             Log.e("Addon data", "Size====" + dataAddOns.size)
                             myTripResults.planData(
                                 dataLiat,
                                 dataAddOns,
                                 data.brokerFaq as ArrayList<OwnerFaq>,
                                 curntPlanDt,
                                 dataAddOnsTracking
                             )
                         }
                     }


                 } else {
                     myTripResults.planError("Some thing Server Error")
                 }

             },
             VollyRequests.volleyError("PaymentGatewayV2/api/Product/GetProductBycategory", context)
         ) {
             @Throws(AuthFailureError::class)
             override fun getHeaders(): Map<String, String> {
                 val params: MutableMap<String, String> = HashMap()
                 params["Authorization"] = key
                 return params
             }

             override fun parseNetworkResponse(response: NetworkResponse?): com.android.volley.Response<JSONObject>? {
                 if (response != null) {
                     mStatusCode = response.statusCode
                 }

                 return super.parseNetworkResponse(response)
             }
         }
         requestQueue.add(jsonObejct);
     }*/
}