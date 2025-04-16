package com.trucksup.field_officer.presenter.view.activity.auth.logout

data class LogoutResponse(
    val message: String,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)