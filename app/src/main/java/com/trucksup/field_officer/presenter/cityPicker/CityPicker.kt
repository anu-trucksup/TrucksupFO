package com.trucksup.field_officer.presenter.cityPicker

interface CityPicker {
    fun fromCity(
        value: String,
        valueState: String,
        id: String,
        type: String,
        valueHi: String,
        valueStateHi: String
    )

    fun toCity(
        value: String,
        valueState: String,
        id: String,
        type: String,
        valueHi: String,
        valueStateHi: String
    )

    fun cityState(
        cityEng: String,
        cityHi: String,
        stateEn: String,
        stateHi: String,
        id: String,
        type: String
    )
}