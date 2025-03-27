package com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler

import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.CurrentTrackingPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.CurrentVerificationPlan
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.DataTrackingPlan

interface TrackingPlanCantroler {

    fun TrackingPlanList(data:ArrayList<DataTrackingPlan>, dataAdon: ArrayList<DataTrackingPlan>)
    fun TrackingCountData(price:String, data: CurrentTrackingPlan, vData: CurrentVerificationPlan)
    fun error(error:String)
}