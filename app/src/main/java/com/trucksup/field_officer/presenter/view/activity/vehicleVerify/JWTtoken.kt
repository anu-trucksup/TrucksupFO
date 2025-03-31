package com.trucksup.field_officer.presenter.view.activity.vehicleVerify

import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse


interface JWTtoken {
    fun onTokenSuccess(response: GenerateJWTtokenResponse)
    fun onTokenFailure(msg:String)

}