package com.trucksup.field_officer.data.model.smartfuel


import com.google.gson.annotations.SerializedName

data class AddSmartFuelLeadResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("requestId")
    val requestId: Int,
    @SerializedName("requestedBy")
    val requestedBy: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("statuscode")
    val statuscode: Int
)