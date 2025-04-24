package com.trucksup.field_officer.presenter.view.activity.businessAssociate.model

data class GetAllMeetupBAResponse(
    val boVisitDetails: List<BoVisitDetail>,
    val message: String,
    val requestId: Int,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)