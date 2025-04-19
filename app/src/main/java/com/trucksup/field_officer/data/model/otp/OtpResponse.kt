package com.trucksup.field_officer.data.model.otp

data class OtpResponse(
    val message: String,
    val mobileNumber: String,
    val otP_ID: String,
    val otpSessionEnd: String,
    val otpSessionStart: String,
    val status: Boolean,
    val statusCode: Int
)