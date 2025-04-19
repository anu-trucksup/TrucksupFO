package com.trucksup.field_officer.data.model.user

data class UpdateProfileResponse(
    val message: String,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)