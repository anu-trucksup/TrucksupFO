package com.trucksup.field_officer.presenter.view.activity.truck_supplier.vml

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class TSOnboardViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultSCbyPincode: MutableLiveData<ResponseModel<PinCodeResponse>> =
        MutableLiveData<ResponseModel<PinCodeResponse>>()
    val resultSCbyPincodeLD: LiveData<ResponseModel<PinCodeResponse>> = resultSCbyPincode

    private var imgUploadResult: MutableLiveData<ResponseModel<ImageResponse>> =
        MutableLiveData<ResponseModel<ImageResponse>>()
    val imgUploadResultLD: LiveData<ResponseModel<ImageResponse>> = imgUploadResult


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

    fun uploadImage(
        bucketName: String?, id: Int?, position: Int?, requestId: Int?, file: MultipartBody.Part?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.uploadImage(
                bucketName, id, position, requestId, file
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    imgUploadResult.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    imgUploadResult.postValue(ResponseModel(success = response.value))
                }
            }
        }


    }


}