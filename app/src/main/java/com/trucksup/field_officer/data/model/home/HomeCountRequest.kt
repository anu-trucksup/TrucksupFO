package com.trucksup.field_officer.data.model.home

data class HomeCountRequest(
    val requestDatetime: String,
    val requestId: Int,
    val requestedBy: String
)