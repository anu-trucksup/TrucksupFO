package com.trucksup.field_officer.presenter.view.activity.businessAssociate.model

import com.google.gson.annotations.SerializedName

data class GetAllBADetailsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("statuscode")
    val statuscode: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("requestId")
    val requestId: Int,
    @SerializedName("requestedBy")
    val requestedBy: String,
    @SerializedName("getTSDetails")
    val getBADetails: List<GetBAdetails>,
)

{
    data class GetBAdetails(
        @SerializedName("ownerName")
        val ownerName: String,
        @SerializedName("mobileNo")
        val mobileNo: String,
        @SerializedName("address")
        val address: String,
        @SerializedName("trucksAdded")
        val trucksAdded: String,
        @SerializedName("kycStatus")
        val kycStatus: String,
        @SerializedName("lastMeeting")
        val lastMeeting: String,
        @SerializedName("meetingCount")
        val meetingCount: String,
        @SerializedName("visitType")
        val visitType: String,
        @SerializedName("lastCallInitiated")
        val lastCallInitiated: String,
        @SerializedName("distance")
        val distance: String,
        //var isVisible:Boolean=false
    )
}


