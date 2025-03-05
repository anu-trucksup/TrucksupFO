package com.trucksup.field_officer.data.model

data class PayloadZ(
    val access_token: String,
    val expires_in: Long,
    val refresh_token: String,
    val scope: String,
    val token_type: String,
    val userType: String?=null

)