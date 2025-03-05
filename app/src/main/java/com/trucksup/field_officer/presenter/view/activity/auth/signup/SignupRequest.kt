package com.trucksup.field_officer.presenter.view.activity.auth.signup

data class SignupRequest(
    val agent: Boolean,
    val builder: Boolean,
    val email: String,
    val fullName: String,
    val individual: Boolean,
    val mobileNumber: String,
    val password: String
)