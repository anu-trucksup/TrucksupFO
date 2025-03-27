package com.trucksup.field_officer.data.model.vehicle


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class GetVerifiedVehiclesResponse(
    @SerializedName("message")
    @Expose
    val message: String,
    @SerializedName("planDetails")
    @Expose
    val planDetails: PlanDetails,
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("statuscode")
    @Expose
    val statuscode: Int,
    @SerializedName("vehicleDetail")
    @Expose
    val vehicleDetail: ArrayList<VehicleDetail>
) {
    data class PlanDetails(
        @SerializedName("count")
        @Expose
        val count: Int,
        @SerializedName("countLeft")
        @Expose
        val countLeft: String
    )

    data class VehicleDetail(
        @SerializedName("data_Chasi_No")
        @Expose
        val dataChasiNo: String,
        @SerializedName("data_Owner_Name")
        @Expose
        val dataOwnerName: String,
        @SerializedName("dateTime")
        @Expose
        val dateTime: String,
        @SerializedName("status")
        @Expose
        val status: String,
        @SerializedName("vehicleNumber")
        @Expose
        val vehicleNumber: String
    )
}