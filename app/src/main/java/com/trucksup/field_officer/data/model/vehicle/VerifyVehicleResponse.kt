package com.logistics.trucksup.modle


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class VerifyVehicleResponse(
@SerializedName("message")
val message: String,
@SerializedName("requestId")
val requestId: String,
@SerializedName("requestedBy")
val requestedBy: String,
@SerializedName("responseDatetime")
val responseDatetime: String,
@SerializedName("status")
val status: String,
@SerializedName("statuscode")
val statuscode: String,
@SerializedName("vehicleDetails")
val vehicleDetails: VehicleDetails
) {
//    data class VehicleDetails(
//        @SerializedName("chasiNo")
//        val chasiNo: String,
//        @SerializedName("cubicCapacity")
//        val cubicCapacity: String,
//        @SerializedName("engineNumber")
//        val engineNumber: String,
//        @SerializedName("fuelType")
//        val fuelType: String,
//        @SerializedName("insuranceCompany")
//        val insuranceCompany: String,
//        @SerializedName("insurancePolicyNo")
//        val insurancePolicyNo: String,
//        @SerializedName("insuranceUpto")
//        val insuranceUpto: String,
//        @SerializedName("nationalPermitNumber")
//        val nationalPermitNumber: String,
//        @SerializedName("nationalPermitUpto")
//        val nationalPermitUpto: String,
//        @SerializedName("ownerName")
//        val ownerName: String,
//        @SerializedName("permitLevel")
//        val permitLevel: String,
//        @SerializedName("permitValidUpto")
//        val permitValidUpto: String,
//        @SerializedName("puccNo")
//        val puccNo: String,
//        @SerializedName("puccUpto")
//        val puccUpto: String,
//        @SerializedName("rcAuthName")
//        val rcAuthName: String,
//        @SerializedName("registrationDate")
//        val registrationDate: String,
//        @SerializedName("stateCode")
//        val stateCode: String,
//        @SerializedName("status")
//        val status: String,
//        @SerializedName("vehicleNumber")
//        val vehicleNumber: String,
//        @SerializedName("vehicleRegisteredAt")
//        val vehicleRegisteredAt: String,
//        @SerializedName("vehicleTaxUpto")
//        val vehicleTaxUpto: String,
//        @SerializedName("registrationValidUpTo")
//        val registrationValidUpTo: String
//    )
data class VehicleDetails(
    @SerializedName("Chasi No")
    val chasiNo: String,
    @SerializedName("cubicCapacity")
    val cubicCapacity: String,
    @SerializedName("Engine Number")
    val engineNumber: String,
    @SerializedName("Fuel Type")
    val fuelType: String,
    @SerializedName("Insurance Company")
    val insuranceCompany: String,
    @SerializedName("Insurance Policy No")
    val insurancePolicyNo: String,
    @SerializedName("Insurance Upto")
    val insuranceUpto: String,
    @SerializedName("National Permit Number")
    val nationalPermitNumber: String,
    @SerializedName("National Permit Upto")
    val nationalPermitUpto: String,
    @SerializedName("Owner Name")
    val ownerName: String,
    @SerializedName("permitLevel")
    val permitLevel: String,
    @SerializedName("Permit Valid upto")
    val permitValidUpto: String,
    @SerializedName("PUCC No")
    val puccNo: String,
    @SerializedName("PUCC upto")
    val puccUpto: String,
    @SerializedName("rc_auth_name")
    val rcAuthName: String,
    @SerializedName("Registration Date")
    val registrationDate: String,
    @SerializedName("State Code")
    val stateCode: String,
    @SerializedName("Status")
    val status: String,
    @SerializedName("vehicleNumber")
    val vehicleNumber: String,
    @SerializedName("Vehicle Registered at")
    val vehicleRegisteredAt: String,
    @SerializedName("Vehicle Tax Upto")
    val vehicleTaxUpto: String,
    @SerializedName("registrationValidUpTo")
    val registrationValidUpTo: String,
    @SerializedName("Manufacturing Year")
    val manufacturingYear: String,
    @SerializedName("Model")
    val model: String,
    @SerializedName("Fitness")
    val Fitness: String,
    @SerializedName("RTO Code")
    val rtoCode: String,
    @SerializedName("pucDate")
    val pucDate: String,
    @SerializedName("insuranceDate")
    val insuranceDate: String,
    @SerializedName("nationalPermitDate")
    val nationalPermitDate: String,
    @SerializedName("dataVehicleFitUpto")
    val dataVehicleFitUpto: String,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("present_address")
    val present_address: String,
    @SerializedName("permanent_address")
    val permanent_address: String,
    @SerializedName("body_type")
    val body_type: String,
    @SerializedName("data_Registration_Date_status")
    val data_Registration_Date_status: String,
    @SerializedName("data_Insurance_Upto_status")
    val data_Insurance_Upto_status: String,
    @SerializedName("data_Permit_Valid_upto_status")
    val data_Permit_Valid_upto_status: String,
    @SerializedName("data_National_Permit_Upto_status")
    val data_National_Permit_Upto_status: String,
    @SerializedName("data_PUCC_upto_status")
    val data_PUCC_upto_status: String,
    @SerializedName("data_Vehicle_Fit_Upto_status")
    val data_Vehicle_Fit_Upto_status: String,
    @SerializedName("data_Vehicle_Tax_Upto_status")
    val data_Vehicle_Tax_Upto_status: String
)
}