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

    private var resultPrevCommitList: MutableLiveData<ResponseModel<HomeCountResponse>> = MutableLiveData<ResponseModel<HomeCountResponse>>()
    val resultPrevCommitListLD: LiveData<ResponseModel<HomeCountResponse>> = resultPrevCommitList

    private var resultAddTodayCommit: MutableLiveData<ResponseModel<HomeCountResponse>> = MutableLiveData<ResponseModel<HomeCountResponse>>()
    val resultAddTodayCommitLD: LiveData<ResponseModel<HomeCountResponse>> = resultAddTodayCommit

    private var resultGetTodayCommit: MutableLiveData<ResponseModel<HomeCountResponse>> = MutableLiveData<ResponseModel<HomeCountResponse>>()
    val resultGetTodayCommitLD: LiveData<ResponseModel<HomeCountResponse>> = resultGetTodayCommit

    fun addTodayCommitList(request: HomeCountRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getAllHomeCountStatus(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultAddTodayCommit.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultAddTodayCommit.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun getTodayCommitList(request: HomeCountRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getAllHomeCountStatus(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultGetTodayCommit.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultGetTodayCommit.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun getAllPrevCommitList(request: HomeCountRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getAllHomeCountStatus(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultPrevCommitList.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultPrevCommitList.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }


}