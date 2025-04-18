package com.trucksup.field_officer.presenter.view.activity.businessAssociate.model

data class CompleteMeetingBARequest(
    val boid: Int,
    val contact_MobileNo: String,
    val contact_Person: String,
    val currAddress: String,
    val currLocation: String,
    val cust_Meeting: Boolean,
    val face_ImageKey: String,
    val fastTag: Boolean,
    val finance: Boolean,
    val followUpDate: String,
    val gift: Boolean,
    val gps: Boolean,
    val id: Int,
    val insurance: Boolean,
    val isLoadGiven: Boolean,
    val latitude: String,
    val longitude: String,
    val office_ImageKey: String,
    val remarks: String,
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String,
    val smartFuel: Boolean,
    val subscription_Plan: Boolean,
    val trucksHub: Boolean
)