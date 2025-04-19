package com.trucksup.field_officer.data.model.authModel

data class LoginRequest(
    val androidVersion: String,
    val appVersion: String,
    val deviceid: String,
    val mobilenumber: String,
    val password: String,
    val profilename: String,
    val profilephoto: String,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String,
    val latitude: String,
    val longitude:String,
    val confirmPassword:String
)