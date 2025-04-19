package com.trucksup.field_officer.data.model.user

data class GetProfileRequest(
    val mobileNo: String,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String,
    val userId: Int
)