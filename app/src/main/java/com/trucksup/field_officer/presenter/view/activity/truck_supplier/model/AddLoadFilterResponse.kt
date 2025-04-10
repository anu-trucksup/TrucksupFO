package com.trucksup.field_officer.presenter.view.activity.truck_supplier.model

data class AddLoadFilterResponse(
    val data: AddLoadData?=null,
    val isSuccess: Boolean,
    val message: String,
    val requestId: String,
    val responseDatetime: String,
    val statusCode: Int
)

data class AddLoadData(
    val bodyType: List<AddLoadTyre>?=null,
    val length: List<AddLoadTyre>?=null,
    val materialWeight: List<AddLoadTyre>?=null,
    val topthree: AddLoadTopthree?=null,
    val tyre: List<AddLoadTyre>?=null
)

data class AddLoadBodyType(
    val id: Int?=null,
    val name: String?=null,
    val name_hindi: String?=null,
    val sequenceNo: Int?=null
)

data class AddLoadTopthree(
    val fromCities: List<AddLoadFromCity>,
    val productTypes: List<AddLoadProductType>,
    val toCities: List<AddLoadToCity>
)

data class AddLoadTyre(
    val hindiUnit: String?=null,
    val id: Int?=null,
    val name: String?=null,
    val sequenceNo: Int?=null,
    val unit: String?=null
)

data class AddLoadFromCity(
    val name: String?=null
)

data class AddLoadProductType(
    val name: String?=null
)

data class AddLoadToCity(
    val name: String?=null
)