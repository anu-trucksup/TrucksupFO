package com.trucksup.field_officer.presenter.view.activity.vehicleVerify

import com.trucksup.field_officer.data.model.subscription.GetPlansResponse


interface PlansCalculation {

    fun onSuccessGetPlans(response: GetPlansResponse)
    fun onFailureGetPlans(msg:String)

//    fun onSuccessCalculations(response: CalculationsResponse)
//    fun onFailureCalculations(msg: String)

}