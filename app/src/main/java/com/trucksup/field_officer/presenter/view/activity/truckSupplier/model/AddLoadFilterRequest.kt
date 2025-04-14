package com.trucksup.field_officer.presenter.view.activity.truckSupplier.model

data class AddLoadFilterRequest(
    val mobileNumber: String,
    val payload: AddLoadFilterPayload,
    val requestDatetime: String,
    val requestId: String
)

data class AddLoadFilterPayload(
    val loadType: Int,
    val tyreId: Int,
    val Source:String
)