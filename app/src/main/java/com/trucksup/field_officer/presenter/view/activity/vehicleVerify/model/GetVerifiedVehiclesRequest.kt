package com.logistics.trucksup.modle.requestModle


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetVerifiedVehiclesRequest(
    @SerializedName("limit")
    @Expose
    val limit: Int,
    @SerializedName("mobileNumber")
    @Expose
    val mobileNumber: String,
    @SerializedName("requestDatetime")
    @Expose
    val requestDatetime: String,
    @SerializedName("requestId")
    @Expose
    val requestId: String,
    @SerializedName("requestedBy")
    @Expose
    val requestedBy: String
)