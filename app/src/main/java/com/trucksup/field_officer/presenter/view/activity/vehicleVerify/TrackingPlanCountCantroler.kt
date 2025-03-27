package com.trucksup.field_officer.presenter.view.activity.vehicleVerify


interface TrackingPlanCountCantroler {

    fun ClickAdd(rs:Int,day:String,data: VasPlan)
    fun ClickMines(rs:Int,day:String,data: VasPlan)
    fun calculateData()
}