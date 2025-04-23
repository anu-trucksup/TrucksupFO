package com.trucksup.field_officer.presenter.view.activity.commit.vml

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommitmentViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultDutyStatus: MutableLiveData<ResponseModel<DutyStatusResponse>> = MutableLiveData<ResponseModel<DutyStatusResponse>>()
    val resultDutyStatusLD: LiveData<ResponseModel<DutyStatusResponse>> = resultDutyStatus

    private var resultAllHomeCountStatus: MutableLiveData<ResponseModel<HomeCountResponse>> = MutableLiveData<ResponseModel<HomeCountResponse>>()
    val resultAllHomeCountStatusLD: LiveData<ResponseModel<HomeCountResponse>> = resultAllHomeCountStatus


    fun dutyStatus(request: DutyStatusRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.dutyStatus(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultDutyStatus.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultDutyStatus.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }


    fun getAllHomeCountStatus(request: HomeCountRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getAllHomeCountStatus(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultAllHomeCountStatus.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultAllHomeCountStatus.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }


}