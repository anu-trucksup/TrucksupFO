package com.trucksup.field_officer.data.model


import com.google.gson.annotations.SerializedName

data class DutyStatusRequest(
    @SerializedName("boid")
    val boid: Int,
    @SerializedName("changedBy")
    val changedBy: Int,
    @SerializedName("isOnDuty")
    val isOnDuty: Boolean,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("requestDatetime")
    val requestDatetime: String,
    @SerializedName("requestId")
    val requestId: Int,
    @SerializedName("requestedBy")
    val requestedBy: String
)