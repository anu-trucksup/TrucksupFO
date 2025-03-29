package com.trucksup.field_officer.presenter.view.activity.auth.forget

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trucksup.field_officer.data.model.CheckUserProfileResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(val apiUseCase: APIUseCase,
                                                 application: Application) : AndroidViewModel(application)  {

    var resultReset: MutableLiveData<ResponseModel<String>> = MutableLiveData<ResponseModel<String>>()
    val resultResetLD: LiveData<ResponseModel<String>> = resultReset

    var checkUserProfile: MutableLiveData<ResponseModel<CheckUserProfileResponse>> = MutableLiveData<ResponseModel<CheckUserProfileResponse>>()
    val checkUserProfileLD: LiveData<ResponseModel<CheckUserProfileResponse>> = checkUserProfile


    fun forgotPassword(email: String, mobile: String, countryCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.forgotPassword(email, mobile, countryCode)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultReset.postValue(ResponseModel<String>(serverError = response.error))
                }
                is ResultWrapper.Success -> {
                    resultReset.postValue(ResponseModel<String>(success = response.value.payload))
                }
            }
        }
    }


    fun checkUserProfile(email: String, mobile: String, countryCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.checkUserProfile(email, mobile, countryCode)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    checkUserProfile.postValue(ResponseModel<CheckUserProfileResponse>(serverError = response.error))
                }
                is ResultWrapper.Success -> {
                    checkUserProfile.postValue(ResponseModel<CheckUserProfileResponse>(success = response.value))
                }
            }
        }
    }
}