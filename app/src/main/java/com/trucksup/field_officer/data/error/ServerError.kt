package com.trucksup.field_officer.data.error

class ServerError(private var httpCode: Int, var errorCode: Int, var message: String?) {


    fun setHttpCode(httpCode: Int) {
        this.httpCode = httpCode
    }
}
