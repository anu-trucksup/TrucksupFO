package com.trucksup.field_officer.data.model

data class LoginResponse(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val scope: String,
    val token_type: String
)