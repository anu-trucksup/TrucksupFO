package com.trucksup.field_officer.data.model

import com.trucksup.field_officer.data.model.profile.BaseUser

data class PayloadCX(
    val baseUser: BaseUser,
    val birthdate: String,
    val city: String,
    val country: String,
    val createdAt: String,
    val currency: String,
    val gender: String,
    val id: Int,
    val latestUserOtp: String,
    val profilePic: String,
    val secretAnswer: String,
    val secretQuestion: String,
    val state: String,
    val updatedAt: String
)