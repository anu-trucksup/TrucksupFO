package com.trucksup.field_officer.presenter.view.activity.trackingAndVerification.interfaceCantroler

import com.logistics.trucksup.modle.CurrentPlan
import com.logistics.trucksup.modle.Milestone
import com.logistics.trucksup.modle.VehicleTrack
import com.logistics.trucksup.modle.VehicleTrackByTripId
import com.logistics.trucksup.modle.VehicleTrackingDetailsData

interface TrackingDetailsDataCantroler {
    fun TrackingDetailsData(data: VehicleTrackingDetailsData)
    fun resentConsetData(message:String)
    fun error(error:String)
}