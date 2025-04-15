package com.trucksup.field_officer.data.model.smartfuel


import com.google.gson.annotations.SerializedName

data class SmartFuelHistoryResponse(
    @SerializedName("leadsHistory")
    val leadsHistory: List<LeadsHistory>,
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
) {
    data class LeadsHistory(
        @SerializedName("cardStatus")
        val cardStatus: String,
        @SerializedName("cardStatusFlag")
        val cardStatusFlag: String,
        @SerializedName("customerName")
        val customerName: String,
        @SerializedName("customerRegisteredMobileNo")
        val customerRegisteredMobileNo: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("leadDetails")
        val leadDetails: ArrayList<LeadDetails>,
        @SerializedName("profilePhoto")
        val profilePhoto: String,
        var isVisible:Boolean=false
    ) {
        data class LeadDetails(
            @SerializedName("cardStatus")
            val cardStatus: String,
            @SerializedName("cardStatusFlag")
            val cardStatusFlag: String,
            @SerializedName("customerName")
            val customerName: String,
            @SerializedName("customerRegisteredMobileNo")
            val customerRegisteredMobileNo: String,
            @SerializedName("datetime")
            val datetime: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("profilePhoto")
            val profilePhoto: String
        )
    }
}