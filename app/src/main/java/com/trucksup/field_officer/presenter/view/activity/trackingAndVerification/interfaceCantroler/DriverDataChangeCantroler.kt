package com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler

interface DriverDataChangeCantroler {
    fun changeDone(message:String,code:Int)
    fun error(error:String)
}