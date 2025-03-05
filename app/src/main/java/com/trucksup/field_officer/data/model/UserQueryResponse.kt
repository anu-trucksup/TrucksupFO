package com.trucksup.field_officer.data.model

data class UserQueryResponse(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: String,
    val status: String
)