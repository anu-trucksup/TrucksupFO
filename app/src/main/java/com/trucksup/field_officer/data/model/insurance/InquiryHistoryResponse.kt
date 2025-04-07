package com.trucksup.field_officer.data.model.insurance


import com.google.gson.annotations.SerializedName

data class InquiryHistoryResponse(
    @SerializedName("inquiryHistory")
    var inquiryHistory: ArrayList<InquiryHistory>,
    @SerializedName("message")
    val message: String,
    @SerializedName("requestId")
    val requestId: String,
    @SerializedName("requestedBy")
    val requestedBy: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("statuscode")
    val statuscode: Int
) {
    data class InquiryHistory(
        @SerializedName("callStatus")
        val callStatus: String,
        @SerializedName("historyDetails")
        val historyDetails: ArrayList<HistoryDetail>,
        @SerializedName("id")
        val id: String,
        @SerializedName("inquiryBy")
        val inquiryBy: String,
        @SerializedName("mobileNumber")
        val mobileNumber: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("refNo")
        val refNo: String,
        @SerializedName("requestId")
        val requestId: String,
        @SerializedName("profilePhoto")
        val profilePhoto: String,
        @SerializedName("callStatusFlag")
        val callStatusFlag: String,
        @SerializedName("dateAndTime")
        val dateAndTime: String,
        var isVisible: Boolean = false
    ) {
        data class HistoryDetail(
            @SerializedName("callStatus")
            val callStatus: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("inqiuryBy")
            val inqiuryBy: String,
            @SerializedName("mobileNumber")
            val mobileNumber: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("refNo")
            val refNo: String,
            @SerializedName("status")
            val status: String,
            @SerializedName("userMobileNo")
            val userMobileNo: String,
            @SerializedName("callStatusFlag")
            val callStatusFlag: String,
            @SerializedName("statusflag")
            val statusflag: String,
            @SerializedName("dateAndTime")
            val dateAndTime: String,
        )
    }
}