package com.trucksup.field_officer.data.model

data class AutoImageSlideResponse(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: List<ImageItem>,
    val status: String
)