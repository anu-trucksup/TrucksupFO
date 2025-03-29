package com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml


import com.google.gson.annotations.SerializedName

data class InquiryHistoryRequest(
    @SerializedName("historyType")
    val historyType: String,
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