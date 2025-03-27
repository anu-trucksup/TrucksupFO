package com.trucksup.field_officer.presenter.view.activity.vehicleVerify

import com.trucksup.field_officer.data.model.vehicle.GetVerifiedVehiclesResponse

interface VerificationController {

    fun onSuccess(response: GetVerifiedVehiclesResponse)
    fun onFailure(msg:String)

}