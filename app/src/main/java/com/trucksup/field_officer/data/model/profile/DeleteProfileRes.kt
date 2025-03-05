package com.trucksup.field_officer.data.model.profile

data class DeleteProfileRes(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: Payload,
    val status: String
)