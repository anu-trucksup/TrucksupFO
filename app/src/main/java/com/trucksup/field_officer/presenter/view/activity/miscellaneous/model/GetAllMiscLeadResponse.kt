package com.trucksup.field_officer.presenter.view.activity.miscellaneous.model

data class GetAllMiscLeadResponse(
    val leadsDetails: List<LeadsDetail>,
    val message: String,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)