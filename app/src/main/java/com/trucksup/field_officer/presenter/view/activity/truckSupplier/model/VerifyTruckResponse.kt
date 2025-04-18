package com.trucksup.field_officer.presenter.view.activity.truckSupplier.model

data class VerifyTruckResponse(
    val message: String,
    val responseDatetime: String,
    val status: String,
    val statusCode: Int
)