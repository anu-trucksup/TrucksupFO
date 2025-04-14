package com.trucksup.field_officer.data.model.authModel

data class SignResponse(
    val message: String,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)