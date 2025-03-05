package com.trucksup.field_officer.data.model

class UserDeviceInfoRequest(
    val deviceBrand: String,
    val deviceModel: String,
    val deviceOS: String,
    val deviceToken: String,
    val deviceType: String,
    val userId: Int
)

