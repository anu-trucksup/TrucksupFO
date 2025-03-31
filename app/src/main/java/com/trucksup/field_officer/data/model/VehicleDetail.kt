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
    @SerializedName("rcFrontImgKey")
    @Expose
    val RCFrontImgKey: String,
    @SerializedName("rcBackImgKey")
    @Expose
    val RCBackImgKey: String,
    @SerializedName("policyDoc")
    @Expose
    val PolicyDoc: String,
    val rcFrontImgUrl:String,
    val rcBackImgUrl:String,
    val policyDocUrl:String

) {

}