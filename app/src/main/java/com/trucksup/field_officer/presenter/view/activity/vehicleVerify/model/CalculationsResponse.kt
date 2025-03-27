package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CalculationsResponse(
    @SerializedName("addonvasList")
    @Expose
    val addonvasList: Any,
    @SerializedName("amountToPaid")
    @Expose
    val amountToPaid: Int,
    @SerializedName("currentTrackingPlan")
    @Expose
    val currentTrackingPlan: List<CurrentTrackingPlan>,
    @SerializedName("currentVerificationPlan")
    @Expose
    val currentVerificationPlan: List<CurrentVerificationPlan>,
    @SerializedName("data")
    @Expose
    val `data`: List<Data>,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("requestId")
    @Expose
    val requestId: String,
    @SerializedName("requestedBy")
    @Expose
    val requestedBy: String,
    @SerializedName("responseDatetime")
    @Expose
    val responseDatetime: String,
    @SerializedName("status")
    @Expose
    val status: Int
) {
    data class CurrentTrackingPlan(
        @SerializedName("remainingCounts")
        @Expose
        val remainingCounts: String,
        @SerializedName("serviceType")
        @Expose
        val serviceType: String,
        @SerializedName("totalQty")
        @Expose
        val totalQty: Int
    )

    data class CurrentVerificationPlan(
        @SerializedName("remainingCounts")
        @Expose
        val remainingCounts: String,
        @SerializedName("serviceType")
        @Expose
        val serviceType: String,
        @SerializedName("totalQty")
        @Expose
        val totalQty: Int
    )

    data class Data(
        @SerializedName("gstPercent")
        @Expose
        val gstPercent: Int,
        @SerializedName("hsnNo")
        @Expose
        val hsnNo: String,
        @SerializedName("mrp")
        @Expose
        val mrp: Int,
        @SerializedName("planFor")
        @Expose
        val planFor: String,
        @SerializedName("qty")
        @Expose
        val qty: Int,
        @SerializedName("remainingCounts")
        @Expose
        val remainingCounts: String,
        @SerializedName("serviceName")
        @Expose
        val serviceName: String,
        @SerializedName("totalAmount")
        @Expose
        val totalAmount: Int,
        @SerializedName("unitText")
        @Expose
        val unitText: String,
        @SerializedName("vasId")
        @Expose
        val vasId: Int,
        @SerializedName("vasnote")
        @Expose
        val vasnote: String
    )
}