package com.trucksup.field_officer.data.model.home

data class HomeCountResponse(
    val homeMenuItems: HomeMenuItems,
    val message: String,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)