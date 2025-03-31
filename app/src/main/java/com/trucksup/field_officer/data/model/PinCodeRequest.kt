package com.trucksup.field_officer.data.model

data class PinCodeRequest(
    val pinCode: String,
    val requestId: String,
    val requestedBy: String
)