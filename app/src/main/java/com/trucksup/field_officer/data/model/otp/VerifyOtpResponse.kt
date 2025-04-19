package com.trucksup.field_officer.data.model.otp

data class VerifyOtpResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean,
    val statusCode: Int
)