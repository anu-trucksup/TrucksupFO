package com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTSteamViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultAllTSteam: MutableLiveData<ResponseModel<FollowUpResponse>> =
        MutableLiveData<ResponseModel<FollowUpResponse>>()
    val resultAllTSteamLD: LiveData<ResponseModel<FollowUpResponse>> = resultAllTSteam

    fun getAllTSteam(token: String, request: FollowUpRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getTotalEarning(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultAllTSteam.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultAllTSteam.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

}