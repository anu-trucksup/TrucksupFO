package com.trucksup.field_officer.presenter.view.activity.auth.forget

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trucksup.field_officer.data.model.PasswordRequest
import com.trucksup.field_officer.data.model.Response
import com.trucksup.field_officer.data.model.TokenZ
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    val apiUseCase: APIUseCase,
    application: Application
) :
    AndroidViewModel(application) {
    var resultLogin: MutableLiveData<ResponseModel<TokenZ>> =
        MutableLiveData<ResponseModel<TokenZ>>()
    val resultLoginLD: LiveData<ResponseModel<TokenZ>> = resultLogin

    var resultReset: MutableLiveData<ResponseModel<String>> =
        MutableLiveData<ResponseModel<String>>()
    val resultResetLD: LiveData<ResponseModel<String>> = resultReset

    var resultValidateAnsReset: MutableLiveData<ResponseModel<Boolean>> =
        MutableLiveData<ResponseModel<Boolean>>()
    val resultValidateAnsResetLD: LiveData<ResponseModel<Boolean>> = resultValidateAnsReset

    var disableCounter: MutableLiveData<Int> = MutableLiveData<Int>()
    val disableCounterLD: LiveData<Int> = disableCounter

    var resultSendOTP: MutableLiveData<ResponseModel<Response<String>>> =
        MutableLiveData<ResponseModel<Response<String>>>()
    val resultSendOTPLD: LiveData<ResponseModel<Response<String>>> = resultSendOTP
    var disableTime = 30000
    var currentTimerTime = 0


    fun resetTimer() {
        disableTime = 30000
        currentTimerTime = 0
    }

    fun increaseDisableTime() {
        disableTime *= 2;
    }

    fun startTimer() {

        CoroutineScope(Dispatchers.IO).launch {
            startTimer(disableTime)
        }
    }

    suspend fun startTimer(value: Int) {
        currentTimerTime = value * 1
        for (i in currentTimerTime downTo 0 step 1000) {
            disableCounter.postValue(i / 1000)
            delay(1000)
        }
    }

    fun validateQuestion(
        email: String,
        mobile: String,
        countryCode: String,
        secretAnswer: String,
        newPassword: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response =
                apiUseCase.validateQuestionAnswer(email, mobile, countryCode, secretAnswer)) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    resultValidateAnsReset.postValue(ResponseModel<Boolean>(networkError = response.error))
                }

                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message ?: "")
                    resultValidateAnsReset.postValue(ResponseModel<Boolean>(serverResponseError = response.error?.message))
                }

                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    resultValidateAnsReset.postValue(ResponseModel<Boolean>(genericError = response.error))
                }

                is ResultWrapper.Success -> {
                    Log.i("API success", "Answer validated")
                    resetPassword(email, mobile, countryCode, newPassword, "", secretAnswer)
                }
            }
        }
    }

    fun resetPassword(
        email: String,
        mobile: String,
        countryCode: String,
        newPassword: String,
        otp: String,
        secretAnswer: String
    ) {
        val passwordRequest = PasswordRequest()
        passwordRequest.newPassword = newPassword
        passwordRequest.currentPassword = otp
        passwordRequest.secretAnswer = secretAnswer
        CoroutineScope(Dispatchers.IO).launch {
            when (val response =
                apiUseCase.resetPassword(email, mobile, countryCode, passwordRequest)) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    resultReset.postValue(ResponseModel<String>(networkError = response.error))
                }

                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message ?: "")
                    resultReset.postValue(ResponseModel<String>(serverResponseError = response.error?.message))
                }

                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    resultReset.postValue(ResponseModel<String>(genericError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultReset.postValue(ResponseModel<String>(success = response.value.payload))
                }
            }
        }
    }


    fun sendOTP(email: String, mobile: String, countryCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.forgotPassword(email, mobile, countryCode)) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    resultSendOTP.postValue(ResponseModel<Response<String>>(networkError = response.error))
                }

                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message ?: "")
                    resultSendOTP.postValue(ResponseModel<Response<String>>(serverResponseError = response.error?.message))
                }

                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    resultSendOTP.postValue(ResponseModel<Response<String>>(genericError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultSendOTP.postValue(ResponseModel<Response<String>>(success = response.value))
                    increaseDisableTime()
                    startTimer(disableTime)
                }
            }
        }
    }

    fun loginUser(username: String, Password: String, type: String, countryCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.loginUser(username, Password, type, countryCode)) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    resultLogin.postValue(ResponseModel<TokenZ>(networkError = response.error))
                }

                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message ?: "")
                    resultLogin.postValue(ResponseModel<TokenZ>(serverResponseError = response.error?.message))
                }

                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    resultLogin.postValue(ResponseModel<TokenZ>(genericError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultLogin.postValue(ResponseModel<TokenZ>(success = response.value))
                }
            }
        }
    }
}