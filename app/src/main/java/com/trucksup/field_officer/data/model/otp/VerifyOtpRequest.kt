package com.trucksup.field_officer.data.model.otp

data class VerifyOtpRequest(
    val actionType: String,
    val appPkgName: String,
    val appSignatureKey: String,
    val appVersion: String,
    val deviceId: String,
    val deviceName: String,
    val imeiNumber1: String,
    val imeiNumber2: String,
    val mobileNumber: String,
    val modelName: String,
    val oStype: String,
    val otP_ID: String,
    val otp: String,
    val recipients: String,
    val requestType: String,
    val requestedBy: String,
    val useFor: String ,
    val userIP: String,
    val userMacAddress: String
)