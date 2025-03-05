package com.trucksup.field_officer.data.model.user

data class UpdateProfileRequest(
    val accountEnabled: Int,
    val builder: Boolean ?=null,
    val individual:Boolean ?=null,
    val agent:Boolean ?=null,
    val userType:String,
    val deleteAccount: Int,
    val email: String,
    val fullName: String,
    val mobileNumber: String
)