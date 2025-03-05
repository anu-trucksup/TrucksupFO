package com.trucksup.field_officer.data.model.category

data class CategoryAllResponse(
    val diagnostic: String,
    val errorCode: Int,
    val errorId: String,
    val httpCode: Int,
    val message: String,
    val payload: List<CategoryItem>,
    val status: String
)