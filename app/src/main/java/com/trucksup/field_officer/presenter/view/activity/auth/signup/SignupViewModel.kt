package com.trucksup.field_officer.presenter.view.activity.auth.signup

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trucksup.field_officer.data.model.NewResisterRequest
import com.trucksup.field_officer.data.model.Response
import com.trucksup.field_officer.data.model.User
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(val apiUseCase: APIUseCase,
                                          application: Application) : AndroidViewModel(application) {

    var resultSendOTP: MutableLiveData<ResponseModel<Response<String>>> = MutableLiveData<ResponseModel<Response<String>>>()
    val resultSendOTPLD: LiveData<ResponseModel<Response<String>>> = resultSendOTP

    var verifyOTPResult: MutableLiveData<ResponseModel<Response<String>>> = MutableLiveData<ResponseModel<Response<String>>>()
    val verifyOTPResultLD: LiveData<ResponseModel<Response<String>>> = verifyOTPResult

    var registerUser: MutableLiveData<ResponseModel<Response<User>>> = MutableLiveData<ResponseModel<Response<User>>>()
    val registerUserLD: LiveData<ResponseModel<Response<User>>> = registerUser


    /*var checkUserProfile: MutableLiveData<ResponseModel<CheckUserProfileResponse>> = MutableLiveData<ResponseModel<CheckUserProfileResponse>>()
    val checkUserProfileLD: LiveData<ResponseModel<CheckUserProfileResponse>> = checkUserProfile
*/


    fun sendOTP(mobile: String, email: String, countryCode: String ) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.sendOTP(mobile, email, countryCode)) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    resultSendOTP.postValue(ResponseModel<Response<String>>(networkError = response.error))
                }
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message?:"")
                    resultSendOTP.postValue(ResponseModel<Response<String>>(serverResponseError = response.error?.message))
                }
                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    resultSendOTP.postValue(ResponseModel<Response<String>>(genericError = response.error))
                }
                is ResultWrapper.Success -> {
                    resultSendOTP.postValue(ResponseModel<Response<String>>(success = response.value))
                }
            }
        }
    }

   /* fun checkUserProfile(email: String, mobile: String, countryCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.checkUserProfile(email, mobile, countryCode)) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    checkUserProfile.postValue(ResponseModel<CheckUserProfileResponse>(networkError = response.error))
                }
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message?:"")
                    checkUserProfile.postValue(ResponseModel<CheckUserProfileResponse>(serverResponseError = response.error?.message))
                }
                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    checkUserProfile.postValue(ResponseModel<CheckUserProfileResponse>(genericError = response.error))
                }
                is ResultWrapper.Success -> {
                    checkUserProfile.postValue(ResponseModel<CheckUserProfileResponse>(success = response.value))
                }
            }
        }
    }*/

    fun verifyOTP(otp: String, email: String, countryCode: String, mobileNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.verifyOTP(otp, email, mobileNumber, countryCode)) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    verifyOTPResult.postValue(ResponseModel<Response<String>>(networkError = response.error))
                }
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message?:"")
                    verifyOTPResult.postValue(ResponseModel<Response<String>>(serverResponseError = response.error?.message))
                }
                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    verifyOTPResult.postValue(ResponseModel<Response<String>>(genericError = response.error))
                }
                is ResultWrapper.Success -> {
                    verifyOTPResult.postValue(ResponseModel<Response<String>>(success = response.value))
                }
            }
        }
    }

    fun signUp(registerUserRequest: NewResisterRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.registerUser(registerUserRequest)) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    registerUser.postValue(ResponseModel<Response<User>>(networkError = response.error))
                }
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message?:"")
                    registerUser.postValue(ResponseModel<Response<User>>(serverResponseError = response.error?.message))
                }
                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    registerUser.postValue(ResponseModel<Response<User>>(genericError = response.error))
                }
                is ResultWrapper.Success -> {
                    registerUser.postValue(ResponseModel<Response<User>>(success = response.value))
                }
            }
        }
    }
}