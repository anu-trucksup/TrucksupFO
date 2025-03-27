package com.trucksup.field_officer.presenter.view.activity.subscription.model

data class MyPlanResponse(
    val data: List<MyPlanData>,
    val errorMessage: String,
    val message: String,
    val responseDatetime: String,
    val status: String,
    val statusCode: Int
)

data class MyPlanData(
    val customerName: String,
    val endDate: String,
    val mobileNumber: String,
    val planName: String,
    val planPrice: Int,
    val planStatus: Boolean,
    val plans: String,
    val productId: Int,
    val remainingdays: Int,
    val startdate: String,
    val subscriptionDays: Int,
    val subscriptionId: Int
)