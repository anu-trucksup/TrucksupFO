package com.trucksup.field_officer.data.model.otp

data class OtpRequest(
    val actionType: String,
    val appSignatureKey: String,
    val networkId: String,
    val otp: Int,
    val recipients: String,
    val requestType: String,
    val requestedBy: String,
    val unicode: Boolean,
    val variables: String,
    val stringOtp:String,
    val deviceId:String
)