package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model

data class TrackingCountResponse(
    val addonvasList: Any,
    val amountToPaid: Int,
    val currentTrackingPlan: List<CurrentTrackingPlan>,
    val currentVerificationPlan: List<CurrentVerificationPlan>,
    val data: List<DataPl>,
    val message: String,
    val requestId: String,
    val requestedBy: String,
    val responseDatetime: String,
    val status: Int
)

data class CurrentTrackingPlan(
    val remainingCounts: String,
    val serviceType: String,
    val totalQty: Int
)

data class CurrentVerificationPlan(
    val remainingCounts: String,
    val serviceType: String,
    val totalQty: Int
)

data class DataPl(
    val gstPercent: Int,
    val hsnNo: String,
    val mrp: Int,
    val planFor: String,
    val qty: Int,
    val remainingCounts: String,
    val serviceName: String,
    val totalAmount: Int,
    val unitText: String,
    val vasId: Int,
    val vasnote: String
)