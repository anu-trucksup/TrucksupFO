package com.trucksup.field_officer.presenter.view.activity.auth.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.Response
import com.trucksup.field_officer.data.model.authModel.SignRequest
import com.trucksup.field_officer.data.model.authModel.SignResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultSendOTP: MutableLiveData<ResponseModel<Response<String>>> =
        MutableLiveData<ResponseModel<Response<String>>>()
    val resultSendOTPLD: LiveData<ResponseModel<Response<String>>> = resultSendOTP

    private var verifyOTPResult: MutableLiveData<ResponseModel<Response<String>>> =
        MutableLiveData<ResponseModel<Response<String>>>()
    val verifyOTPResultLD: LiveData<ResponseModel<Response<String>>> = verifyOTPResult

    private var registerUser: MutableLiveData<ResponseModel<SignResponse>> =
        MutableLiveData<ResponseModel<SignResponse>>()
    val registerUserLD: LiveData<ResponseModel<SignResponse>> = registerUser


    fun signUp(token: String, request: SignRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.registerUser(token, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    registerUser.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    registerUser.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }
}