package com.trucksup.field_officer.data.model.deleteResponse

data class DeleteProfileResponse(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: String,
    val status: String
)