package com.trucksup.field_officer.presenter.view.activity.subscription.model

data class payBillResponse(
    val data: DataBill,
    val message: String,
    val responseDateTime: String,
    val status: String,
    val statusCode: Int
)

data class DataBill(
    val orderNumber: String,
    val redirectUrl: String,
    val requestedBy: String
)