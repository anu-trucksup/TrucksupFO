package com.trucksup.field_officer.presenter.view.activity.miscellaneous.model

data class GetMiscLeadRequest(
    val boID: Int,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String,
    val startDate:String,
    val endDate:String
)