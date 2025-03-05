package com.trucksup.field_officer.presenter.view.activity.financeInsurance


interface ChipCantroler {

    fun updateData(chipTextList: ArrayList<chipData>, value: String)
    fun DataList(data: ArrayList<chipData>)
    fun updateData(message: String, message1: String)
    fun requestError(error: String)
}