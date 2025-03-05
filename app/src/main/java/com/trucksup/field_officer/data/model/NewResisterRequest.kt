package com.trucksup.field_officer.data.model

data class NewResisterRequest(

    val fullName: String? = null,
    val password: String? = null,

    val email: String,
    val mobileNumber: String,
    val mobileCode: String = ""
) {
}