package com.trucksup.field_officer.presenter.common.image_picker

interface TrucksFOImageController {
    fun getImage(valuekey: String, url: String)
    fun dataSubmitted(message: String)
    fun imageError(error: String)
}