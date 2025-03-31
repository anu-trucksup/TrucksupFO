package com.trucksup.field_officer.presenter.view.activity.auth.signup

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trucksup.field_officer.data.model.NewResisterRequest
import com.trucksup.field_officer.data.model.Response
import com.trucksup.field_officer.data.model.TokenZ
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



    fun signUp(registerUserRequest: NewResisterRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.registerUser(registerUserRequest)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    registerUser.postValue(ResponseModel<Response<User>>(serverError = response.error))
                }
                is ResultWrapper.Success -> {
                    registerUser.postValue(ResponseModel<Response<User>>(success = response.value))
                }
            }
        }
    }
}