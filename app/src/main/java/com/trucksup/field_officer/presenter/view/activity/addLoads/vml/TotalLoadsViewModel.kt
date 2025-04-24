package com.trucksup.field_officer.presenter.view.activity.addLoads.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.DutyStatusRequest
import com.trucksup.field_officer.data.model.DutyStatusResponse
import com.trucksup.field_officer.data.model.home.HomeCountRequest
import com.trucksup.field_officer.data.model.home.HomeCountResponse
import com.trucksup.field_officer.data.model.smartfuel.AddSmartFuelLeadRequest
import com.trucksup.field_officer.data.model.smartfuel.AddSmartFuelLeadResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutRequest
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutResponse
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TotalLoadsViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultTotalAddLoads: MutableLiveData<ResponseModel<FollowUpResponse>> =
        MutableLiveData<ResponseModel<FollowUpResponse>>()
    val resultTotalAddLoadsLD: LiveData<ResponseModel<FollowUpResponse>> = resultTotalAddLoads

    private var totalAddLoadsDetails: MutableLiveData<ResponseModel<FollowUpResponse>> =
        MutableLiveData<ResponseModel<FollowUpResponse>>()
    val totalAddLoadsDetailsLD: LiveData<ResponseModel<FollowUpResponse>> = totalAddLoadsDetails

    fun getTotalAddLoad(token: String, request: FollowUpRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getTotalAddLoad(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultTotalAddLoads.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultTotalAddLoads.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun getTotalAddLoadDetails(token: String, request: FollowUpRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getTotalAddLoadDetails(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultTotalAddLoads.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultTotalAddLoads.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }


}