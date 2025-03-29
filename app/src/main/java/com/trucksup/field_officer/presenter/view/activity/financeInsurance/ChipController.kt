package com.trucksup.field_officer.presenter.view.activity.financeInsurance


interface ChipController {

    fun updateData(chipTextList: ArrayList<chipData>, value: String)

    fun requestError(error: String)
}