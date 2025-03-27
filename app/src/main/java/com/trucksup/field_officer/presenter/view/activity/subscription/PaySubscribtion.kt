package com.trucksup.field_officer.presenter.view.activity.subscription

import com.logistics.trucksup.modle.Broker
import com.trucksup.field_officer.presenter.view.activity.subscription.model.MyPlanData


interface PaySubscribtion {
    fun subClick(data: Broker)

    fun renewPlan(data: MyPlanData, position:Int)
}