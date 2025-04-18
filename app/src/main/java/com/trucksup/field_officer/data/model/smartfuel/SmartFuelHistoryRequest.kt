package com.trucksup.field_officer.data.model.smartfuel


import com.google.gson.annotations.SerializedName

data class SmartFuelHistoryRequest(
    @SerializedName("mobilenumber")
    val mobilenumber: String,
    @SerializedName("referralCode")
    val referralCode: String,
    @SerializedName("requestDatetime")
    val requestDatetime: String,
    @SerializedName("requestId")
    val requestId: String,
    @SerializedName("requestedBy")
    val requestedBy: String
)