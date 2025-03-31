package com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.image.ImageResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.InsuranceActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class InsuranceViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel()  {

    private var resultsubmitInsurance: MutableLiveData<ResponseModel<SubmitInsuranceInquiryData>> = MutableLiveData<ResponseModel<SubmitInsuranceInquiryData>>()
    val resultsubmitInsuranceLD: LiveData<ResponseModel<SubmitInsuranceInquiryData>> = resultsubmitInsurance

    private var imgUploadResult: MutableLiveData<ResponseModel<ImageResponse>> = MutableLiveData<ResponseModel<ImageResponse>>()
    val imgUploadResultLD: LiveData<ResponseModel<ImageResponse>> = imgUploadResult


    fun submitInsuranceData(request: SubmitInsuranceInquiryRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.submitInsuranceInquiry(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?:"")
                    resultsubmitInsurance.postValue(ResponseModel(serverError = response.error))
                }
                is ResultWrapper.Success -> {
                    resultsubmitInsurance.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun uploadImage(bucketName: String?, id: Int?, position: Int?, requestId: Int?, file: MultipartBody.Part?
    ) {
        CoroutineScope(Dispatchers.IO).launch {
        when (val response = apiUseCase.uploadImage(
           bucketName, id, position, requestId, file
        )) {
            is ResultWrapper.ServerResponseError -> {
                Log.e("API Error", response.error?:"")
                imgUploadResult.postValue(ResponseModel(serverError = response.error))
            }
            is ResultWrapper.Success -> {
                imgUploadResult.postValue(ResponseModel(success = response.value))
            }
        }
    }


    }

    fun trucksupImageUpload(
        authToken: String,
        s: String,
        prepareFilePartTrucksHum: MultipartBody.Part,
        prepareFilePartTrucksHum1: MultipartBody.Part,
        insuranceActivity: InsuranceActivity,
        insuranceActivity1: InsuranceActivity,
        s1: String
    ) {
        TODO("Not yet implemented")
    }


}