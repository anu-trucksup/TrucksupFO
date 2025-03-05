package com.trucksup.field_officer.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class VehicleDetail(
    @SerializedName("insuranceValidity")
    @Expose
    val insuranceValidity: String,
    @SerializedName("vehicleNumber")
    @Expose
    val vehicleNumber: String,
    val rcFrontImgUrl: String,
    val rcBackImgUrl: String,
    val policyDocUrl: String,
    val s: String,
    val s1: String,
    val s2: String

) {

}