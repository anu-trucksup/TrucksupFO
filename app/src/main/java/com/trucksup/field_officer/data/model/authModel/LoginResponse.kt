package com.trucksup.field_officer.data.model.authModel

data class LoginResponse(
    val loginDetails: LoginDetails,
    val message: String,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)