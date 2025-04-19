package com.trucksup.field_officer.data.model.otp

data class OtpRequest(
    val actionType: String,
    val appPkgName: String,
    val appSignatureKey: String,
    val appVersion: String,
    val deviceId: String,
    val mobileNumber: String,
    val modelName: String,
    val oStype: String,
    val recipients: String,
    val requestType: String,
    val requestedBy: String,
    val unicode: Boolean,
    val useFor: String,
    val variables: String
)