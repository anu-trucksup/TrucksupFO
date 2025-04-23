package com.trucksup.field_officer.presenter.view.activity.miscellaneous.model

data class MiscDetails(
    val address: String,
    val boId: Int,
    val businessName: String,
    val category: String,
    val createdBy: String,
    val id: Int,
    val latitude: String,
    val longitude: String,
    val mobileNo: String,
    val name: String,
    val truckImageDetails: List<TruckImageDetail>,
    val truckNumber: String
)