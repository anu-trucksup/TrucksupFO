package com.logistics.trucksup.modle

import com.google.gson.annotations.SerializedName

data class VerifiedDLDetailsResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("planDetails")
    val planDetails: PlanDetails,
    @SerializedName("status")
    val status: String,
    @SerializedName("statuscode")
    val statuscode: Int,
    @SerializedName("verifiedDLDetails")
    val verifiedDLDetails: ArrayList<VerifiedDLDetail>
) {
    data class PlanDetails(
        @SerializedName("count")
        val count: Int,
        @SerializedName("countLeft")
        val countLeft: String
    )

    data class VerifiedDLDetail(
        @SerializedName("dob")
        val dob: String,
        @SerializedName("doe")
        val doe: String,
        @SerializedName("license_number")
        val licenseNumber: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("trxnDateTime")
        val trxnDateTime: String
    )
}
