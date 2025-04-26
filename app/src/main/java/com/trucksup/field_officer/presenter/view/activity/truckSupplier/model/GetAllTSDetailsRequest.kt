package com.trucksup.field_officer.presenter.view.activity.truckSupplier.model

import com.google.gson.annotations.SerializedName

data class GetAllTSDetailsRequest(
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