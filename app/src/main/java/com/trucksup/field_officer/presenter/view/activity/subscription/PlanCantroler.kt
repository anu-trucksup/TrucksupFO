package com.trucksup.field_officer.presenter.view.activity.subscription

import com.logistics.trucksup.modle.AddonsData
import com.logistics.trucksup.modle.Broker
import com.logistics.trucksup.modle.CurantPlan
import com.logistics.trucksup.modle.CurantPlanData
import com.logistics.trucksup.modle.OwnerFaq
import com.trucksup.field_officer.presenter.view.activity.subscription.model.MyPlanData
import com.trucksup.field_officer.presenter.view.activity.subscription.model.payBillResponse

interface PlanCantroler {
    fun planData(
        data: ArrayList<Broker>,
        dataAddOns: ArrayList<AddonsData>,
        faqData: ArrayList<OwnerFaq>,
        currentPlanDetails: CurantPlanData,
        vehicleverification: ArrayList<AddonsData>
    )

    fun myPlanData(data: ArrayList<MyPlanData>, key: String)
    fun planBill(data: payBillResponse)
    fun planError(error: String)

}