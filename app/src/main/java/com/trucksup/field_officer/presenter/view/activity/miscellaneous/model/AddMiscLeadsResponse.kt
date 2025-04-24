package com.trucksup.field_officer.presenter.view.activity.miscellaneous.model

data class AddMiscLeadsResponse(
    val message: String,
    val miscDetails: MiscDetails,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)