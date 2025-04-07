package com.trucksup.field_officer.presenter.cityPicker

data class CitySearchRequest(
    var searchText: String,
    var requestedBy: String,
    var requestId: String,
    var proFileLanguage: Int
)
