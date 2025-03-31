package com.trucksup.field_officer.presenter.view.activity.financeInsurance

interface TrucksUbImageController {
    fun getImage(value:String,url:String)
    fun dataSubmitted(message:String)
    fun imageError(error:String)
}