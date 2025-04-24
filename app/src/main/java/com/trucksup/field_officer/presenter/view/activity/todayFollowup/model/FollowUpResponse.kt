package com.trucksup.field_officer.presenter.view.activity.todayFollowup.model

data class FollowUpResponse(
    val meetsCounts: MeetsCounts,
    val message: String,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int,
    val todayFollowUpCounts: TodayFollowUpCounts
)