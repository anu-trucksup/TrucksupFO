package com.trucksup.field_officer.presenter.view.activity.auth.logout

data class LogoutRequest(
    val isLogOut: Boolean,
    val mobilenumber: String,
    val requestId: String,
    val requestNumber: String
)