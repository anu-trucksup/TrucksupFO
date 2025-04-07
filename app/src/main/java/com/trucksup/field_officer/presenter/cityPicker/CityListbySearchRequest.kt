package com.trucksup.field_officer.presenter.cityPicker

data class CityListbySearchRequest(
    val statusCode: Int,
    val data: List<CitySearchData>,
    val message: String
)

data class CitySearchData(
    val city: String,
    val hindiCityName: String,
    val hindiStateName: String,
    val latitude: Double,
    val longitude: Double,
    val pincode1: Int,
    val pincode2: Int,
    val srno: Int,
    val state: String
)