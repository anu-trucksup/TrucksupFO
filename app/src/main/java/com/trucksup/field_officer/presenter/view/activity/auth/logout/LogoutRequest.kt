package com.trucksup.field_officer.presenter.view.activity.auth.logout

data class LogoutRequest(
    val boid: Int,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String
)