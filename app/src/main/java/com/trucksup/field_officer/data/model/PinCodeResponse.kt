package com.trucksup.field_officer.data.model

data class PinCodeResponse(
    val data: List<PnData>,
    val message: String,
    val requestId: String,
    val requestedBy: String,
    val responseDateTime: String,
    val status: String,
    val statusCode: Int
)

data class PnData(
    val circleName: String,
    val delivery: String,
    val district: String,
    val divisionName: String,
    val hindiCity: String,
    val hindiState: String,
    val latitude: String,
    val longitude: String,
    val officeName: String,
    val officeType: String,
    val pinCode: String,
    val regionName: String,
    val stateName: String
)