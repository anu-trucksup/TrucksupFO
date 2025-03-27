package com.trucksup.field_officer.presenter.view.activity.vehicleVerify

data class TrackingCountRequeat(
    val mobileNumber: String,
    val requestId: String,
    val requestedBy: String,
    val serviceType: String,
    val vasPlans: List<VasPlan>
)

data class VasPlan(
    val isPaid: Boolean,
    val qty: Int,
    val vasId: String
)