package com.trucksup.field_officer.presenter.view.activity.auth.forget

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.authModel.ForgetRequest
import com.trucksup.field_officer.data.model.authModel.ForgetResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePasswordViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {
    private var resultReset: MutableLiveData<ResponseModel<ForgetResponse>> =
        MutableLiveData<ResponseModel<ForgetResponse>>()
    val resultResetLD: LiveData<ResponseModel<ForgetResponse>> = resultReset


    fun resetPassword(token: String, request: ForgetRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response =
                apiUseCase.resetPassword(token, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultReset.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultReset.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

}