package com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
class BAScheduleMeetingVM @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultSCbyPincode: MutableLiveData<ResponseModel<PinCodeResponse>> =
        MutableLiveData<ResponseModel<PinCodeResponse>>()
    val resultSCbyPincodeLD: LiveData<ResponseModel<PinCodeResponse>> = resultSCbyPincode


    private var onScheduleMeetingBAResponse: MutableLiveData<ResponseModel<ScheduleMeetingResponse>> =
        MutableLiveData<ResponseModel<ScheduleMeetingResponse>>()
    val onScheduleMeetingBAResponseLD: LiveData<ResponseModel<ScheduleMeetingResponse>> = onScheduleMeetingBAResponse

    private var onCompleteMeetingBAResponse: MutableLiveData<ResponseModel<ScheduleMeetingResponse>> =
        MutableLiveData<ResponseModel<ScheduleMeetingResponse>>()
    val onCompleteMeetingBAResponseLD: LiveData<ResponseModel<ScheduleMeetingResponse>> = onCompleteMeetingBAResponse

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

    fun ImageUpload(
        token: String,
        documentType: String, file: MultipartBody.Part,
        fileWaterMark: MultipartBody.Part,
        imgRes: TrucksFOImageController
    ) {
        val apiInterface = ApiClient().getClient
        apiInterface.uploadImages(token, documentType, "BusinessOfficer", file, fileWaterMark)

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


    fun onScheduleMeetingBA(request: ScheduleMeetingBARequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response =
                apiUseCase.scheduleMeetingBA(PreferenceManager.getAuthToken(), request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    onScheduleMeetingBAResponse.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    onScheduleMeetingBAResponse.postValue(ResponseModel(success = response.value))
                }
            }
        }

    }

    fun onCompleteMeetingBA(request: CompleteMeetingBARequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response =
                apiUseCase.completeBAMeeting(PreferenceManager.getAuthToken(), request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    onCompleteMeetingBAResponse.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    onCompleteMeetingBAResponse.postValue(ResponseModel(success = response.value))
                }
            }
        }

    }


}