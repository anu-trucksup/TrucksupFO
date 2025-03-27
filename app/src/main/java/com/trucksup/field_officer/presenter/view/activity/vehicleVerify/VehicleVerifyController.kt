package com.trucksup.field_officer.presenter.view.activity.vehicleVerify


import com.logistics.trucksup.modle.VerifyVehicleResponse

interface VehicleVerifyController {

    fun onVerifySuccess(response: VerifyVehicleResponse)
    fun onVerifyFailure(msg:String)

}