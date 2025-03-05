package com.trucksup.field_officer.data.model

data class CityData(
    val active: Boolean,
    val cityCode: String,
    val id: Int,
    val name: String,
    val countryCode: String,
    val countryId: Int,
    val countryName: String,
    val createdAt: String,
    val stateCode: String,
    val stateId: Int,
    val stateName: String,
    val status: Boolean,
    val updatedAt: String
)