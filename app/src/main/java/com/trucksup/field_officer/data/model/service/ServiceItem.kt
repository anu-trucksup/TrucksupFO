package com.trucksup.field_officer.data.model.service

data class ServiceItem(
    val bussinessdiscount: Int?=null,
    val categoryId: Int?=null,
    val categoryName: String?=null,
    val createdAt: String?=null,
    val id: Int?=null,
    val name: String,
    val description: String?=null,
    val price: Int?=null,
    val regulardiscount: Int?=null,
    val shopkeeper: Any?=null,
    val shopkeeperId: Int?=null,
    val updatedAt: String?=null
)