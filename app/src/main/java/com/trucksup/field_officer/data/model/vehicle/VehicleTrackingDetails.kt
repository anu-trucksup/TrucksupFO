package com.logistics.trucksup.modle

data class VehicleTrackingDetails(
    val data: VehicleTrackingDetailsData,
    val message: String,
    val requestId: String,
    val requestedBy: String,
    val status: String,
    val statuscode: Int
)

data class VehicleTrackingDetailsData(
    val currentPlan: List<CurrentPlan>,
    val milestones: List<Milestone>,
    val vehicleTrackList: List<VehicleTrack>,
    val vehicleTrackingHistory:List<TrackingHistory>,
    val vehicleTrackListByTripId: List<VehicleTrackByTripId>
)

data class CurrentPlan(
    val extraQty: Int,
    val freeQty: Int,
    val id: Int,
    val mobileNumber: String,
    val price: Int,
    val profileName: String,
    val receivedAmount: Int,
    val serviceName: String,
    val serviceType: String,
    val totalQty: Int,
    val transactionId: String,
    val unitText: String,
    val vasId: Int
)

data class Milestone(
    val address: String,
    val event: String,
    val key: String,
    val time: String,
    val title: String,
    val to_time: String,
    val upcoming: Boolean
)

data class VehicleTrack(
    val consentStatus: String,
    val driverMobileNumber: String,
    val fromLocation: String,
    val publicLink: String,
    val toLocation: String,
    val tripDays: Int,
    val tripEndDate: String,
    val tripId: String,
    val tripStartDate: String,
    val truckNumber: String
)
data class TrackingHistory(
    val consentStatus: String,
    val driverMobileNumber: String,
    val fromLocation: String,
    val publicLink: String,
    val toLocation: String,
    val tripDays: Int,
    val tripEndDate: String,
    val tripId: String,
    val tripStartDate: String,
    val truckNumber: String
)
data class VehicleTrackByTripId(
    val consentStatus: String,
    val driverMobileNumber: String,
    val fromLocation: String,
    val publicLink: String,
    val toLocation: String,
    val tripDays: Int,
    val tripEndDate: String,
    val tripId: String,
    val tripStartDate: String,
    val truckNumber: String
)