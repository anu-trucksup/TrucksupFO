package com.trucksup.field_officer.data.network

data class ResponseModel<T>(
    val success: T? = null,
    val serverError: String? = null,
    /*val networkError: String? = null,
    val genericError: String? = null*/
)