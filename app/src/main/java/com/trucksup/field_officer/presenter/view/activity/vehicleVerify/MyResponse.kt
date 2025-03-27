package com.trucksup.field_officer.presenter.view.activity.vehicleVerify

import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.VehicalTrip

class MyResponse {
    fun getVerifiedVehicles(
        accessToken: String,
        request: Any,
        vehicleVerifyActivity: VehicleVerifyActivity,
        vehicleVerifyActivity1: VehicleVerifyActivity
    ) {
        //TODO("Not yet implemented")
    }

    fun getPlans(
        accessToken: String,
        request: Any,
        vehicleVerifyActivity: VehicleVerifyActivity,
        vehicleVerifyActivity1: VehicleVerifyActivity
    ) {

    }

    fun verifyVehicle(
        accessToken: String,
        request: Any,
        vehicleVerifyActivity: VehicleVerifyActivity,
        vehicleVerifyActivity1: VehicleVerifyActivity
    ) {

    }

    fun generateJWTtoken(
        request: GenerateJWTtokenRequest,
        vehicleVerifyHistoryActivity: VehicleVerifyHistoryActivity,
        vehicleVerifyHistoryActivity1: VehicleVerifyHistoryActivity
    ) {

    }

    fun getTrackingPlanList(
        key: String,
        request: Any,
        paymentCheckoutNewActivity: PaymentCheckoutNewActivity,
        paymentCheckoutNewActivity1: PaymentCheckoutNewActivity
    ) {

    }

    fun GetVehicleTrackingDetails(
        tripId: String,
        key: String,
        vehicalTrip: VehicalTrip,
        vehicalTrip1: VehicalTrip
    ) {

    }

    fun getTrackingCount(
        key: String,
        request: TrackingCountRequeat,
        paymentCheckoutNewActivity: PaymentCheckoutNewActivity,
        paymentCheckoutNewActivity1: PaymentCheckoutNewActivity
    ) {

    }

}
