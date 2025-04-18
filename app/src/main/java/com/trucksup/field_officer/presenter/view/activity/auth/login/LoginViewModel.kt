package com.trucksup.field_officer.presenter.view.activity.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.authModel.LoginRequest
import com.trucksup.field_officer.data.model.authModel.LoginResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel()  {

    private var resultLogin: MutableLiveData<ResponseModel<LoginResponse>> = MutableLiveData<ResponseModel<LoginResponse>>()
    val resultLoginLD: LiveData<ResponseModel<LoginResponse>> = resultLogin


    fun loginUser(token: String, request: LoginRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.loginUser(token, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultLogin.postValue(ResponseModel<LoginResponse>(serverError = response.error))
                }
                is ResultWrapper.Success -> {
                    resultLogin.postValue(ResponseModel<LoginResponse>(success = response.value))
                }
            }
        }
    }

}