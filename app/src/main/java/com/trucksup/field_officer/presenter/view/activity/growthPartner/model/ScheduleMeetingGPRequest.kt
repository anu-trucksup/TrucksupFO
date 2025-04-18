package com.trucksup.field_officer.presenter.view.activity.growthPartner.model

data class ScheduleMeetingGPRequest(
    val boid: Int,
    val cust_Name: String,
    val lat: String,
    val long: String,
    val mobileNumber: String,
    val profileType: Int,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String,
    val scheduledDate: String,
    val scheduledtime: String
)