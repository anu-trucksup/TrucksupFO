package com.trucksup.field_officer.presenter.view.activity.miscellaneous.model


import com.google.gson.annotations.SerializedName

data class GetAllMiscLeadResponse(
    @SerializedName("completedLeads")
    val completedLeads: ArrayList<IncompletedLead>,
    @SerializedName("incompletedLeads")
    val incompletedLeads: ArrayList<IncompletedLead>,
    @SerializedName("message")
    val message: String,
    @SerializedName("requestId")
    val requestId: Int,
    @SerializedName("requestedBy")
    val requestedBy: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("statuscode")
    val statuscode: Int
) {
    data class IncompletedLead(
        @SerializedName("actionType")
        val actionType: String,
        @SerializedName("boId")
        val boId: Int,
        @SerializedName("businessName")
        val businessName: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("latitude")
        val latitude: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("longitude")
        val longitude: String,
        @SerializedName("mobileNo")
        val mobileNo: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("truckImageList")
        val truckImageList: ArrayList<TruckImage>,
        @SerializedName("truckNumber")
        val truckNumber: String
    ) {
        data class TruckImage(
            @SerializedName("id")
            val id: Int,
            @SerializedName("imageKey")
            val imageKey: String,
            @SerializedName("key")
            val key: String,
            @SerializedName("miscId")
            val miscId: Int,
            @SerializedName("truckNumber")
            val truckNumber: String
        )
    }
}