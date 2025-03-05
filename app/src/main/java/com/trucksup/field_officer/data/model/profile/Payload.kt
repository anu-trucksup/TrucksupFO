package com.trucksup.field_officer.data.model.profile

data class Payload(
    val baseUser: BaseUser,
    val birthdate: Any,
    val city: Any,
    val country: Any,
    val createdAt: Any,
    val currency: Any,
    val gender: String,
    val id: Int,
    val latestUserOtp: Any,
    val profilePic: Any,
    val secretAnswer: String,
    val secretQuestion: String,
    val state: Any,
    val updatedAt: Any
)