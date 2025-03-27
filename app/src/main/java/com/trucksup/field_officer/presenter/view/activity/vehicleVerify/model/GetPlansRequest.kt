package com.logistics.trucksup.modle.requestModle


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetPlansRequest(
    @SerializedName("mobileNumber")
    @Expose
    val mobileNumber: String,
    @SerializedName("requestId")
    @Expose
    val requestId: String,
    @SerializedName("requestedBy")
    @Expose
    val requestedBy: String,
    @SerializedName("vasServiceType")
    @Expose
    val vasServiceType: String
)