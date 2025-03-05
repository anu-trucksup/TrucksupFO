package com.trucksup.field_officer.presenter.view.activity.financeInsurance

interface InsuranceController {
    fun deleteTruck(po:Int)
    fun dataSubmitted(message:String,message1: String)
    fun dataError(error:String)
}