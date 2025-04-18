package com.logistics.trucksup.activities.preferre.modle

data class PrefferLanRequest(
    val address: String,
    val businessName: String,
    val city: String,
    val createdBy: String,
    val lat: String,
    val long: String,
    val mobileNo: String,
    val ownerName: String,
    val pinCode: String,
    val preflane: List<Preflane>,
    val profileType: Int,
    val requestedBy: String,
    val requestedDate: String,
    val trucksDetails: List<TrucksDetail>,
    var clickAction: String
)

data class Preflane(
    val fromCity: String,
    val toCity: String
)

data class TrucksDetail(
    val vehicleNo: String,
    val vehicleStatus: String,
    val mobileNo: String,
    val ownerName: String,
    val bodytype: String,
    val tyre: String,
    val capacity: String,
    val vehicleSize: String,
    val typeofSourcing: String,
    val hindiTypeofSourcing: String? = null,
    val hindiBodyType: String? = null,
)