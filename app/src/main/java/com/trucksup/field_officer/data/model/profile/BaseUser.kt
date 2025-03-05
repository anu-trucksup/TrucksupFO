package com.trucksup.field_officer.data.model.profile

data class BaseUser(
    val accountEnabled: Int,
    val cpassword: Any,
    val createdAt: String,
    val createdBy: Any,
    val deleteAccount: Int,
    val email: String,
    val firstName: String,
    val id: Int,
    val isEmailVerified: Int,
    val isMobileVerified: Int,
    val lastLogin: String,
    val lastName: String,
    val loginResponse: Any,
    val middleName: Any,
    val mobileCode: String,
    val mobileNumber: String,
    val password: String,
    val passwordSalt: String,
    val signupPlatform: Any,
    val updatedAt: String,
    val updatedBy: Any,
    val userType: String,
    val username: Any
)