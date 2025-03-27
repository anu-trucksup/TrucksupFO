package com.trucksup.field_officer.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GenerateJWTtokenResponse(
    @SerializedName("accessToken")
    @Expose
    val accessToken: String,
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("responseDatetime")
    @Expose
    val responseDatetime: String,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("statusCode")
    @Expose
    val statusCode: Int
)