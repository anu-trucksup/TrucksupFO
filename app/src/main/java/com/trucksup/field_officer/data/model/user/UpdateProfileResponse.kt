package com.trucksup.field_officer.data.model.user

data class UpdateProfileResponse(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: UpdateProfileItem,
    val status: String
)