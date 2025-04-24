package com.trucksup.field_officer.data.model.home


import com.google.gson.annotations.SerializedName

data class UserDetails(
    @SerializedName("mobileNo")
    val mobileNo: String,
    @SerializedName("msg")
    val msg: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("refcode")
    val refcode: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("dutyStatus")
    var dutyStatus: String
)