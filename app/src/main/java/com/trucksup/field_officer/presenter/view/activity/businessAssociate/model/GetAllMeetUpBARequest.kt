package com.trucksup.field_officer.presenter.view.activity.businessAssociate.model

data class GetAllMeetUpBARequest(
    val boID: Int,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String,
    val type: String
)