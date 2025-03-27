package com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model

data class TrackingPlanListResponse(
    val data: List<DataTrackingPlan>?=null,
    val addonsData: List<DataTrackingPlan>?=null,
    val message: String,
    val requestId: String,
    val requestedBy: String,
    val responseDatetime: String,
    val status: Int
)

data class DataTrackingPlan(
    val actualPrice: Int?=null,
    val contractEndDate: String?=null,
    val contractStartDate: String?=null,
    val gstPercent: Int?=null,
    val hsnNo: String?=null,
    val imageUrl: String?=null,
    val mrp: Int?=null,
    val notes: String?=null,
    val recommended: String?=null,
    val serviceDescriptoin: String?=null,
    val serviceName: String?=null,
    val serviceProvider: String?=null,
    val serviceQty: Int?=null,
    val serviceType: String?=null,
    val serviceValidityType: String?=null,
    val unitText: String?=null,
    val vasId: Int?=null,
    val counterDay:Int=0,
    val remainingCounts:String?=null
)