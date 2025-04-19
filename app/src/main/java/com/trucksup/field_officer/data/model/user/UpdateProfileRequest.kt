package com.trucksup.field_officer.data.model.user

data class UpdateProfileRequest(
    val address: String,
    val city: String,
    val email: String,
    val mobileNo: String,
    val pincode: String,
    val profilephoto: String,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String,
    val state: String,
    val userId: Int
)