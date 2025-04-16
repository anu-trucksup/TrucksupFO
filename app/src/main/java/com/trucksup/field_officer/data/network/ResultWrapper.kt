package com.trucksup.field_officer.data.network

import com.trucksup.field_officer.data.model.Response
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class ServerResponseError(val error: String?) : ResultWrapper<Nothing>()
}

class ServerException constructor(var status: String? = null, var messageLabel: String? = null) :
    Exception()

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResultWrapper.Success(checkSuccessStatus(apiCall.invoke()))
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> ResultWrapper.ServerResponseError("Server not Reachable.")
                is HttpException -> {
                    if (throwable.code() == 400) {
                        val error_description: String
                        // Log.e("msgBodyResponse",""+ (throwable.response()?.errorBody()?.byteString().toString()))
                        /*  val response1 = throwable.apply {
                              val response: String = this.response()?.errorBody()?.string()!!
                              Log.e("msgBodyResponse", "" + response)
                              val jresponse = JSONObject(response)
                              error_description = jresponse.getString("error_description")
                              ResultWrapper.ServerResponseError("" + error_description)
                          }*/

                        ResultWrapper.ServerResponseError(
                            "" + (throwable.response()?.errorBody()?.byteString().toString())
                        )

                    } else {
                        ResultWrapper.ServerResponseError("Something went wrong! \n Server Error")
                    }
                }

                else -> {
                    ResultWrapper.ServerResponseError("Something went wrong! Server Error \n" + throwable.message + "" + throwable.stackTraceToString())
                }
            }
        }
    }
}

fun <T> checkSuccessStatus(value: T): T {
    if (value is Response<*>) {
        if (value.status.equals("Successful", true)) {
            return value
        } else if (value.status.equals("EXCEPTION", true)) {
            throw ServerException(value.status, value.message)
        } else {
            throw ServerException(value.status, value.message)
        }
    }

    return value
}