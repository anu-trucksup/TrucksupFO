package com.trucksup.field_officer.data.model.resister

data class ResisterResponse(
    val agent: Boolean,
    val builder: Boolean,
    val email: String,
    val fullName: String,
    val individual: Boolean,
    val mobileNumber: String,
    val password: String,
    val shineShopsRequest: ShineShopsRequest,
    val userType: String
)