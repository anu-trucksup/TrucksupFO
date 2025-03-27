package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model

data class TrackingPlanListRequest(
    val mobileNumber: String,
    val requestId: String,
    val requestedBy: String,
    val vasServiceType: String
)