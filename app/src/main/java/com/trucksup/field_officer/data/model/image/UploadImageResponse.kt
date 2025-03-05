package com.trucksup.field_officer.data.model.image

data class UploadImageResponse(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: UploadImage,
    val status: String
)