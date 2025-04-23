package com.trucksup.field_officer.presenter.view.activity.growthPartner.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.logistics.trucksup.activities.preferre.modle.GetMeetScheduleDetailsRequest
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PinCodeResponse
import com.trucksup.field_officer.data.model.image.ImageResponse
import com.trucksup.field_officer.data.model.image.TrucksupImageUploadResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.cityPicker.ApiClient
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerRequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerResponse
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.CompleteMeetingBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.ScheduleMeetingBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.ScheduleMeetingResponse
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.CompleteMeetingGPRequest
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.ScheduleMeetingGPRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetMeetScheduleDetailsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class GPScheduleMeetingVM @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultSCbyPincode: MutableLiveData<ResponseModel<PinCodeResponse>> =
        MutableLiveData<ResponseModel<PinCodeResponse>>()
    val resultSCbyPinCodeLD: LiveData<ResponseModel<PinCodeResponse>> = resultSCbyPincode

    //by me
    private var onScheduleMeetingGPResponse: MutableLiveData<ResponseModel<ScheduleMeetingResponse>> =
        MutableLiveData<ResponseModel<ScheduleMeetingResponse>>()
    val onScheduleMeetingGPResponseLD: LiveData<ResponseModel<ScheduleMeetingResponse>> = onScheduleMeetingGPResponse

    private var resultGetGPScheduleMeetingData: MutableLiveData<ResponseModel<GetMeetScheduleDetailsResponse>> =
        MutableLiveData<ResponseModel<GetMeetScheduleDetailsResponse>>()
    val rresultGetGPScheduleMeetingDataLD: LiveData<ResponseModel<GetMeetScheduleDetailsResponse>> = resultGetGPScheduleMeetingData
    //by me

    private var onCompleteMeetingGPResponse: MutableLiveData<ResponseModel<ScheduleMeetingResponse>> =
        MutableLiveData<ResponseModel<ScheduleMeetingResponse>>()
    val onCompleteMeetingGPResponseLD: LiveData<ResponseModel<ScheduleMeetingResponse>> = onCompleteMeetingGPResponse

    fun getCityStateByPin(token: String, request: PinCodeRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getCityStateByPin(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultSCbyPincode.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultSCbyPincode.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }


    fun trucksupImageUpload(
        token: String,
        filetype: String,
        foldername: String,
        file: MultipartBody.Part,
        fileWaterMark: MultipartBody.Part,
        imgRes: TrucksFOImageController
    ) {
        val apiInterface = ApiClient().getClient
        apiInterface.uploadImages(token, filetype, foldername, file, fileWaterMark)

            ?.enqueue(object : Callback<TrucksupImageUploadResponse> {
                override fun onResponse(
                    call: Call<TrucksupImageUploadResponse>,
                    response: Response<TrucksupImageUploadResponse>
                ) {

                    if (response.isSuccessful) {
                        if (response.body()?.s3FileName != null) {
                            imgRes.getImage(
                                response.body()?.s3FileName.toString(),
                                response.body()?.url.toString()
                            )
                        } else {
                            imgRes.imageError(response.body()?.message.toString())
                        }

                    } else {
                        imgRes.imageError("Something server error")
                    }
                }

                override fun onFailure(call: Call<TrucksupImageUploadResponse>, t: Throwable) {
                    imgRes.imageError("Something server error")
                }
            })
    }

    //add by me
    fun onScheduleMeetingGP(request: ScheduleMeetingGPRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response =
                apiUseCase.scheduleMeetingGP(PreferenceManager.getAuthToken(), request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    onScheduleMeetingGPResponse.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    onScheduleMeetingGPResponse.postValue(ResponseModel(success = response.value))
                }
            }
        }

    }
    fun getBAMeetScheduleData(request: GetMeetScheduleDetailsRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getGPMeetSchedule(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultGetGPScheduleMeetingData.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultGetGPScheduleMeetingData.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }
    //add by me

    fun onCompleteMeetingBA(request: CompleteMeetingGPRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response =
                apiUseCase.completeBAMeeting(PreferenceManager.getAuthToken(), request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    onCompleteMeetingGPResponse.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    onCompleteMeetingGPResponse.postValue(ResponseModel(success = response.value))
                }
            }
        }

    }


}