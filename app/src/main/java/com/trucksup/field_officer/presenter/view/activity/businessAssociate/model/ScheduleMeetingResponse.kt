package com.trucksup.field_officer.presenter.view.activity.businessAssociate.model

data class ScheduleMeetingResponse(
    val message: String,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val date: String,
    val time: String,
    val statuscode: Int
)