package com.trucksup.field_officer.presenter.view.activity.subscription.model

data class PlanRequest(
    val subscriptionType: String,
    val requestedby: String,
    val mobileNo: String,
    val requestDatetime: String,
    val PreferredLanguage:String,
)