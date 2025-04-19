package com.trucksup.field_officer.data.model.user

data class GetProfileResponse(
    val boProfileDetails: BoProfileDetails,
    val message: String,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)