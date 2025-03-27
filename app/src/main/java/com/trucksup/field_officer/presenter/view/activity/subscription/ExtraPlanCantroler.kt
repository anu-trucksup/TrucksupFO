package com.trucksup.field_officer.presenter.view.activity.subscription

import com.logistics.trucksup.modle.AddedAddon
import com.logistics.trucksup.modle.CalculateData
import com.trucksup.field_officer.presenter.view.activity.subscription.model.AddOnsMatrix
import java.util.ArrayList

interface ExtraPlanCantroler {
    fun ClickAdd(rs: Int, day: String, data: AddOnsMatrix)
    fun ClickMines(rs: Int, day: String, data: AddOnsMatrix)
    fun calculateData(dataAmount: CalculateData, data: ArrayList<AddedAddon>)
    fun calculateError(error: String)
}