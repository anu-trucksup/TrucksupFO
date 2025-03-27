package com.trucksup.field_officer.data.model


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GenerateJWTtokenRequest(
    @SerializedName("apiSecreteKey")
    @Expose
    val apiSecreteKey: String,
    @SerializedName("issuer")
    @Expose
    val issuer: String,
    @SerializedName("password")
    @Expose
    val password: String,
    @SerializedName("userAgent")
    @Expose
    val userAgent: String,
    @SerializedName("username")
    @Expose
    val username: String
)