package com.trucksup.field_officer.presenter.view.activity.miscellaneous.model

data class UpdateMiscLeadsResponse(
    val message: String,
    val miscDetails: Any,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)