package com.trucksup.field_officer.data.model.vehicle


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CalculationsRequest(
    @SerializedName("mobileNumber")
    @Expose
    val mobileNumber: String,
    @SerializedName("requestId")
    @Expose
    val requestId: String,
    @SerializedName("requestedBy")
    @Expose
    val requestedBy: String,
    @SerializedName("serviceType")
    @Expose
    val serviceType: String,
    @SerializedName("vasPlans")
    @Expose
    val vasPlans: List<VasPlan>
) {
    data class VasPlan(
        @SerializedName("isPaid")
        @Expose
        val isPaid: Boolean,
        @SerializedName("qty")
        @Expose
        var qty: Int,
        @SerializedName("vasId")
        @Expose
        val vasId: String
    )
}