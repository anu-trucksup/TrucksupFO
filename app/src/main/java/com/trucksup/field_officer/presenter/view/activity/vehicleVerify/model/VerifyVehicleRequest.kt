package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class VerifyVehicleRequest(
    @SerializedName("mobileNumber")
    @Expose
    val mobileNumber: String,
    @SerializedName("orderId")
    @Expose
    val orderId: String,
    @SerializedName("refrenceNo")
    @Expose
    val refrenceNo: String,
    @SerializedName("requestDatetime")
    @Expose
    val requestDatetime: String,
    @SerializedName("requestId")
    @Expose
    val requestId: String,
    @SerializedName("vehicleNumber")
    @Expose
    val vehicleNumber: String,
    @SerializedName("isPreview")
    @Expose
    val isPreview: Boolean
)