package com.trucksup.field_officer.presenter.view.activity.miscellaneous.model

data class AddMiscLeadRequest(
    val actionType: String,
    val address: String,
    val businessName: String,
    val category: String,
    val createdBy: String,
    val mobileNo: String,
    val name: String,
    val requestDatetime: String,
    val requestId: String,
    val requestedBy: String,
    val truckNumber: String,
    val trucksImageXML: ArrayList<TrucksImageXML>,
    val userId: Int,
    val miscId:String
)