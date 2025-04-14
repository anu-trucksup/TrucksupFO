package com.trucksup.field_officer.presenter.view.activity.smartfuel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.image.ImageResponse
import com.trucksup.field_officer.data.model.image.TrucksupImageUploadResponse
import com.trucksup.field_officer.data.model.smartfuel.AddSmartFuelLeadRequest
import com.trucksup.field_officer.data.model.smartfuel.AddSmartFuelLeadResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.cityPicker.ApiClient
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryData
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryRequest
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
class SmartFuelViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultSubmitSmartFuel: MutableLiveData<ResponseModel<AddSmartFuelLeadResponse>> =
        MutableLiveData<ResponseModel<AddSmartFuelLeadResponse>>()
    val resultSubmitSmartFuelLD: LiveData<ResponseModel<AddSmartFuelLeadResponse>> =
        resultSubmitSmartFuel

    private var imgUploadResult: MutableLiveData<ResponseModel<ImageResponse>> =
        MutableLiveData<ResponseModel<ImageResponse>>()
    val imgUploadResultLD: LiveData<ResponseModel<ImageResponse>> = imgUploadResult


    fun submitSmartFuel(request: AddSmartFuelLeadRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.addSmartFuelLead(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultSubmitSmartFuel.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultSubmitSmartFuel.postValue(ResponseModel(success = response.value))
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

    fun trucksupImageUpload(token: String,
        documentType: String, file: MultipartBody.Part,
        fileWaterMark: MultipartBody.Part,
        imgRes: TrucksFOImageController) {
        val apiInterface = ApiClient().getClient
        apiInterface.uploadImages(token, documentType, file, fileWaterMark)

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


}