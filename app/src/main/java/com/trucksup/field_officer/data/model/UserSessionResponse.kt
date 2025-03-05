package com.glovejob.data.model

data class UserSessionResponse(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: PayloadXXX,
    val status: String
)