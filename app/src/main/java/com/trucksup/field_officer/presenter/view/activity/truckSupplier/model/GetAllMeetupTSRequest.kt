package com.trucksup.field_officer.presenter.view.activity.truckSupplier.model

data class GetAllMeetupTSRequest(
    val boID: Int,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String,
    val type: String
)