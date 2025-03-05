package com.trucksup.field_officer.data.model.service

data class AllServiceByShpIzDResponse(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: List<ServiceItem>,
    val status: String
)