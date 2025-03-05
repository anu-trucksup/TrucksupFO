package com.trucksup.field_officer.data.model

data class PayloadX(
    val capital: String,
    val countryCode: String?,
    val countryId: Int,
    val countryName: String?,
    val countryFlag: String,
    val createdAt: String,
    val currency: String,
    val currencyName: String,
    val currencySymbol: String,
    val latitude: Double,
    val longitude: Double,
    val phoneCode: String,
    val stateCount: Int,
    val timezones: String,
    val updatedAt: String,
    val active: Boolean,
    val esimActive: Boolean
)