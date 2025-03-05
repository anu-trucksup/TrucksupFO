package com.trucksup.field_officer.data.model

data class CountryResponse(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: PayloadXX,
    val status: String
)