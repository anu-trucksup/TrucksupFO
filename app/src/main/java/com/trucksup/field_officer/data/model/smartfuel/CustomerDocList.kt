package com.trucksup.field_officer.data.model.smartfuel


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class CustomerDocList(
    @SerializedName("rcFrontImgKey")
    @Expose
    val RCFrontImgKey: String,

    @SerializedName("rcBackImgKey")
    @Expose
    val RCBackImgKey: String,

    @SerializedName("panImgKey")
    @Expose
    val panImgKey: String,

    @SerializedName("AddressFrontImgKey")
    @Expose
    val AddressFrontImgKey: String,

    @SerializedName("AddressBackImgKey")
    @Expose
    val AddressBackImgKey: String,

    @SerializedName("cancelChequeImgKey")
    @Expose
    val cancelChequeImgKey: String,

    @SerializedName("declarationImgKey")
    @Expose
    val declarationImgKey: String,
)

