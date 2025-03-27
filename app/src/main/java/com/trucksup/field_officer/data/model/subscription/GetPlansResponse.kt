package com.trucksup.field_officer.data.model.subscription


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetPlansResponse(
    @SerializedName("data")
    @Expose
    val `data`: ArrayList<Data>,
    @SerializedName("addonsData")
    @Expose
    val `addonsData`: ArrayList<Data>,
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
    data class Data(
        @SerializedName("actualPrice")
        @Expose
        val actualPrice: Int?,
        @SerializedName("contractEndDate")
        @Expose
        val contractEndDate: String?,
        @SerializedName("contractStartDate")
        @Expose
        val contractStartDate: String?,
        @SerializedName("gstPercent")
        @Expose
        val gstPercent: Int?,
        @SerializedName("hsnNo")
        @Expose
        val hsnNo: String?,
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String?,
        @SerializedName("mrp")
        @Expose
        val mrp: Int?,
        @SerializedName("notes")
        @Expose
        val notes: String?,
        @SerializedName("recommended")
        @Expose
        val recommended: String?,
        @SerializedName("serviceDescriptoin")
        @Expose
        val serviceDescriptoin: String?,
        @SerializedName("serviceName")
        @Expose
        val serviceName: String?,
        @SerializedName("serviceProvider")
        @Expose
        val serviceProvider: String?,
        @SerializedName("serviceQty")
        @Expose
        val serviceQty: Int?,
        @SerializedName("serviceType")
        @Expose
        val serviceType: String?,
        @SerializedName("serviceValidityType")
        @Expose
        val serviceValidityType: String?,
        @SerializedName("unitText")
        @Expose
        val unitText: String?,
        @SerializedName("vasId")
        @Expose
        val vasId: Int?,
        @SerializedName("remainingCounts")
        @Expose
        val remainingCounts: String?,
        val counterDay:Int=0
    )
}