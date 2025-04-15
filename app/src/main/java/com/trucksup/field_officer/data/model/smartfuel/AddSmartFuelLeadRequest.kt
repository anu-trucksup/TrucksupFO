package com.trucksup.field_officer.data.model.smartfuel


import com.google.gson.annotations.SerializedName

data class AddSmartFuelLeadRequest(
    @SerializedName("addressProof")
    val addressProof: String,
    @SerializedName("addressProofBack")
    val addressProofBack: String,
    @SerializedName("addressProofFront")
    val addressProofFront: String,
    @SerializedName("bpMobileNumber")
    val bpMobileNumber: String,
    @SerializedName("cancelledCheque")
    val cancelledCheque: String,
    @SerializedName("customerEmail")
    val customerEmail: String,
    @SerializedName("customerName")
    val customerName: String,
    @SerializedName("customerRegisteredMobileNo")
    val customerRegisteredMobileNo: String,
    @SerializedName("customerVehicleNo")
    val customerVehicleNo: String,
    @SerializedName("declarationform")
    val declarationform: String,
    @SerializedName("panCard")
    val panCard: String,
    @SerializedName("panCardFile")
    val panCardFile: String,
    @SerializedName("rcBackPhoto")
    val rcBackPhoto: String,
    @SerializedName("rcFrontPhoto")
    val rcFrontPhoto: String,
    @SerializedName("requestDatetime")
    val requestDatetime: String,
    @SerializedName("requestId")
    val requestId: String,
    @SerializedName("requestedBy")
    val requestedBy: String,
    @SerializedName("salesCode")
    val salesCode: String,
    @SerializedName("source")
    val source: String,
    @SerializedName("userMobileNumber")
    val userMobileNumber: String
)