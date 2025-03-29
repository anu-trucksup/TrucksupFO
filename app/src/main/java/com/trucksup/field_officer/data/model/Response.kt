package com.trucksup.field_officer.data.model

class Response<T> {
    var payload: T? = null
    val message: String?=null
    val requestId: String?=null
    val requestedBy: String?=null
    val status: String?=null
    val statuscode: Int?=null
}