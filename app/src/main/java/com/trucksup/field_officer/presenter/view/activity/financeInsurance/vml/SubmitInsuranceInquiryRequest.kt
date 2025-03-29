package com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.trucksup.field_officer.data.model.VehicleDetail

data class SubmitInsuranceInquiryRequest(
    @SerializedName("requestId")
    @Expose
    val requestId: String,
    @SerializedName("requestDatetime")
    @Expose
    val requestDatetime: String,
    @SerializedName("userMobileNo")
    @Expose
    val userMobileNo: String,
    @SerializedName("fullName")
    @Expose
    val fullName: String,
    @SerializedName("profileType")
    @Expose
    val profileType: String,
    @SerializedName("mobileNumber")
    @Expose
    val mobileNumber: String,
    @SerializedName("vehicleType")
    @Expose
    val vehicleType: String,
    @SerializedName("vehicleUnderInsurance")
    @Expose
    val vehicleUnderInsurance: String,
    @SerializedName("createdBy")
    @Expose
    val createdBy: String,
    @SerializedName("insuranceFor")
    @Expose
    val insuranceFor: String,
    @SerializedName("requestedBy")
    @Expose
    val requestedBy: String,
    @SerializedName("referralCode")
    @Expose
    val referralCode: String,
    @SerializedName("vehicleDetails")
    @Expose
    val vehicleDetails: List<VehicleDetail>,
    @SerializedName("source")
    @Expose
    val source: String
)