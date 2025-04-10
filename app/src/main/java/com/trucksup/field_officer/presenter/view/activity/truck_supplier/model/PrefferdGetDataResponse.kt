package com.trucksup.field_officer.presenter.view.activity.truck_supplier.model

data class PrefferdGetDataResponse(
    val message: String,
    val ownersData: List<OwnersData>,
    val prefLane: List<PrefLaneDt>,
    val responseDatetime: String,
    val status: String,
    val statusCode: Int,
    val vehicleDetails: List<VehicleDetail>
)

data class OwnersData(
    val address: String,
    val bussinessName: String,
    val city: String,
    val state: String,
    val id: Int,
    val lat: String,
    val long: String,
    val number: String,
    val ownerName: String,
    val pinCode: String,
    val profileType: Int,
    val source: String,
    )

data class PrefLaneDt(
    val from: String,
    val id: Int,
    val mobileNo: String,
    val to: String,
    val isShow: Boolean = false,
    val hindiFrom: String,
    val hindiTo: Int,
)

data class VehicleDetail(
    val vehicleNo: String? = null,
    val vehicleStatus: String? = null,
    val mobileNo: String? = null,
    val ownerName: String? = null,
    val bodytype: String? = null,
    val tyre: String? = null,
    val capacity: String? = null,
    val vehicleSize: String? = null,
    val typeofSourcing: String? = null,
    val hindiTypeofSourcing: String? = null,
    val hindiBodyType: String? = null,
    )
