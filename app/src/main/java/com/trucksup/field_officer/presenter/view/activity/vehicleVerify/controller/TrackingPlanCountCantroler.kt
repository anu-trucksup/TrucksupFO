package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.controller

import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.VasPlan


interface TrackingPlanCountCantroler {

    fun ClickAdd(rs:Int,day:String,data: VasPlan)
    fun ClickMines(rs:Int,day:String,data: VasPlan)
    fun calculateData()
}