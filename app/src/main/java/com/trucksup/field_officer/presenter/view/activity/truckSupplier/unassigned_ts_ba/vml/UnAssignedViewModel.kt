package com.trucksup.field_officer.presenter.view.activity.truckSupplier.unassigned_ts_ba.vml

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
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllTSDetailsRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllTSDetailsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UnAssignedViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultUnAssignedBA: MutableLiveData<ResponseModel<GetAllTSDetailsResponse>> =
        MutableLiveData<ResponseModel<GetAllTSDetailsResponse>>()
    val resultUnAssignedBALD: LiveData<ResponseModel<GetAllTSDetailsResponse>> = resultUnAssignedBA

    private var resultUnAssignedTS: MutableLiveData<ResponseModel<GetAllTSDetailsResponse>> =
        MutableLiveData<ResponseModel<GetAllTSDetailsResponse>>()
    val resultUnAssignedTSLD: LiveData<ResponseModel<GetAllTSDetailsResponse>> = resultUnAssignedTS


    fun getUnAssignedBA(token: String, request: GetAllTSDetailsRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getAllTSDetails(
                token, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultUnAssignedBA.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultUnAssignedBA.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun getUnAssignedTS(token: String, request: GetAllTSDetailsRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getAllTSDetails(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultUnAssignedTS.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultUnAssignedTS.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

}