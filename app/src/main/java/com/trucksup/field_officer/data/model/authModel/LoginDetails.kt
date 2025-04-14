package com.trucksup.field_officer.data.model.authModel

data class LoginDetails(
    val boUserid: String,
    val city: String,
    val email: String,
    val org: String,
    val password: String,
    val pincode: String,
    val profilename: String,
    val profilephoto: String,
    val referralcode: String,
    val state: String,
    val token: String
)