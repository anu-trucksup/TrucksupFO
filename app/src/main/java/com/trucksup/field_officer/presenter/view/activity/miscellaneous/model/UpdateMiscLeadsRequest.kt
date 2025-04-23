package com.trucksup.field_officer.presenter.view.activity.miscellaneous.model

data class UpdateMiscLeadsRequest(
    val actionType: String,
    val address: String,
    val businessName: String,
    val category: String,
    val createdBy: String,
    val mobileNo: String,
    val name: String,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String,
    val truckNumber: String,
    val trucksImageXML: TrucksImageXML,
    val userId: Int
)