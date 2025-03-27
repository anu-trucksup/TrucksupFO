package com.trucksup.field_officer.presenter.view.activity.subscription.model

data class PlanAmountCalculateRequest(
    val addOnsMatrix: List<AddOnsMatrix>,
    val discountPercent: String,
    val mobileNumber: String,
    val promoCode: String,
    val promoCodeApply: String,
    val requestDatetime: String,
    val requestId: String,
    val subId: String,
    val ProfileType:Int
)

data class AddOnsMatrix(
    val id: Int,
    val qty: Int,
    val type: String
)