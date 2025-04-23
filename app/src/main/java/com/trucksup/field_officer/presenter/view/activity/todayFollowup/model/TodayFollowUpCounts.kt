package com.trucksup.field_officer.presenter.view.activity.todayFollowup.model

data class TodayFollowUpCounts(
    val boId: Int,
    val completed: String,
    val plannedFollowUp: String,
    val scheduled: String,
    val total: String
)