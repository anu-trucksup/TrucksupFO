package com.trucksup.field_officer.presenter.view.activity.todayFollowup.model

data class FollowUpRequest(
    val boID: Int,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String
)