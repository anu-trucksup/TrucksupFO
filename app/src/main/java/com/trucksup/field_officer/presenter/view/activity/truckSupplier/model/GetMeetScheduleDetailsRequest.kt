package com.logistics.trucksup.activities.preferre.modle

import com.google.gson.annotations.SerializedName

data class GetMeetScheduleDetailsRequest(
    @SerializedName("requestId")
    val requestId: Int,
    @SerializedName("requestedBy")
    val requestedBy: String,
    @SerializedName("requestDatetime")
    val requestDatetime: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("mobileNo")
    val mobileNo: String
)