package com.trucksup.field_officer.presenter.view.activity.todayFollowup.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PinCodeResponse
import com.trucksup.field_officer.data.model.image.ImageResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class TodayFollowUpViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultTodayFollowup: MutableLiveData<ResponseModel<FollowUpResponse>> =
        MutableLiveData<ResponseModel<FollowUpResponse>>()
    val resultTodayFollowupLD: LiveData<ResponseModel<FollowUpResponse>> = resultTodayFollowup

    fun getTodayFollowup(token: String, request: FollowUpRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getTodaysFollowup(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultTodayFollowup.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultTodayFollowup.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

}