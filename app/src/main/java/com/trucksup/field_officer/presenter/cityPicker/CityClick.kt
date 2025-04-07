package com.trucksup.field_officer.presenter.cityPicker

interface CityClick {
    fun onClickcity(value: String, state: String, id: String)
    fun onClickcityLanguage(
        cityEng: String,
        cityHi: String,
        stateEn: String,
        stateHi: String,
        id: String
    )
}