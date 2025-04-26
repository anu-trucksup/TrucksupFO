package com.trucksup.field_officer.presenter.view.activity.growthPartner.vml

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
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllMeetUpTSResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllMeetupTSRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class GPFollowUpViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var getAllMeetUpGPResponse: MutableLiveData<ResponseModel<GetAllMeetUpTSResponse>> =
        MutableLiveData<ResponseModel<GetAllMeetUpTSResponse>>()
    val getAllMeetUpGPResponseLD: LiveData<ResponseModel<GetAllMeetUpTSResponse>> = getAllMeetUpGPResponse

    fun getAllMeetupGP(token: String, request: GetAllMeetupTSRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getAllMeetupGP(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    getAllMeetUpGPResponse.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    getAllMeetUpGPResponse.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

}