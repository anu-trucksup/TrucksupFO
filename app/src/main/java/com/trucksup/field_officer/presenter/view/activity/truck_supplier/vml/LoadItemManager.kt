package com.trucksup.field_officer.presenter.view.activity.truck_supplier.vml

import com.trucksup.field_officer.presenter.view.activity.truck_supplier.model.AddLoadData


interface LoadItemManager {
    fun loadItoms(type: String, valueEnglish: String, valueHindi: String, unit: String, id: Int)
    fun filtterData(data: AddLoadData)
    fun filtterError(error: String)
}