package com.trucksup.field_officer.data.model.otp

data class OTPResponse(
    val AppSignatureKey: String,
    val ErrorMsg: Any,
    val IsSuccess: Boolean
)