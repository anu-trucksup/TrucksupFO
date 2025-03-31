package com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class SubmitInsuranceInquiryData(
    @SerializedName("hindiMessage")
    @Expose
    val hindiMessage: String,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("message1")
    @Expose
    val message1: String,
    @SerializedName("refNo")
    @Expose
    val refNo: String,
    @SerializedName("requestId")
    @Expose
    val requestId: String,
    @SerializedName("requestedBy")
    @Expose
    val requestedBy: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("statuscode")
    @Expose
    val statuscode: Int
)