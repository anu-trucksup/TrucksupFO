package com.trucksup.field_officer.data.model.resister

data class ShineShopsRequest(
    val address: String,
    val categoryId: Int,
    val cityId: Int,
    val discount: Int,
    val name: String
)