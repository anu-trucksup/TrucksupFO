package com.trucksup.field_officer.data.model.otp

data class Data(
    val code: Int,
    val loginToken: Any,
    val messageEng: String,
    val messageHin: String,
    val mobileNumber: Any
)