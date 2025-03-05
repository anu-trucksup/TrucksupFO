package com.trucksup.field_officer.data.model

class Response<T> {
    var payload: T? = null
    var status: String? = null
    var httpCode: Int? = 0
    var errorCode: Int? = 0
    var message: String? = null
    var errorId: String? = null
    var diagnostic: String? = null
}