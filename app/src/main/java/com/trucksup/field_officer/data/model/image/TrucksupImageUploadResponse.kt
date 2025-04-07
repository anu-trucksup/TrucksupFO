package com.trucksup.field_officer.data.model.image


import com.google.gson.annotations.SerializedName

data class TrucksupImageUploadResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("s3FileName")
    val s3FileName: String,
    @SerializedName("url")
    val url: String
)