package com.trucksup.field_officer.data.model.authModel

data class ForgetRequest(
    val androidVersion: String,
    val appVersion: String,
    val deviceid: String,
    val mobilenumber: String,
    val password: String,
    val profilename: String,
    val profilephoto: String,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String
)