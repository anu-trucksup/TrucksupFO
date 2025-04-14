package com.trucksup.field_officer.presenter.view.activity.auth.forget

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.Response
import com.trucksup.field_officer.data.model.otp.NewOtpResponse
import com.trucksup.field_officer.data.model.otp.OtpRequest
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultSendOTP: MutableLiveData<ResponseModel<NewOtpResponse>> =
        MutableLiveData<ResponseModel<NewOtpResponse>>()
    val resultSendOTPLD: LiveData<ResponseModel<NewOtpResponse>> = resultSendOTP

    fun sendOTP(auth: String, request: OtpRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.sendOTP(auth, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultSendOTP.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultSendOTP.postValue(ResponseModel(success = response.value))
                    /*  increaseDisableTime()
                      startTimer(disableTime)*/
                }
            }
        }
    }

}