package com.trucksup.field_officer.data.model

import com.glovejob.data.model.PayloadXXXX

data class PrivacyAllResponse(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: List<PayloadXXXX>,
    val status: String
)