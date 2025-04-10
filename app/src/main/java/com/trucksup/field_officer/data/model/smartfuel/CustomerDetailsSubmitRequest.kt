package com.trucksup.field_officer.data.model.smartfuel


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import com.trucksup.field_officer.data.model.VehicleDetail

data class CustomerDetailsSubmitRequest(
    @SerializedName("requestId")
    @Expose
    val requestId: String,
    @SerializedName("requestDatetime")
    @Expose
    val requestDatetime: String,
    @SerializedName("etCustomerFullname")
    @Expose
    val etCustomerFullname: String,
    @SerializedName("etCustomerMobile")
    @Expose
    val etCustomerMobile: String,
    @SerializedName("etCustomerEmail")
    @Expose
    val etCustomerEmail: String,
    @SerializedName("etCustomerVehicleNumber")
    @Expose
    val etCustomerVehicleNumber: String,
    @SerializedName("etPanNumber")
    @Expose
    val etPanNumber: String,
    @SerializedName("addressproof")
    @Expose
    val addressproof: String,
    @SerializedName("rcode")
    @Expose
    val rcode: String,
)