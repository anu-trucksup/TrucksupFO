package com.trucksup.field_officer.presenter.view.activity.auth.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.Response
import com.trucksup.field_officer.data.model.authModel.SignRequest
import com.trucksup.field_officer.data.model.authModel.SignResponse
import com.trucksup.field_officer.data.model.image.TrucksupImageUploadResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.cityPicker.ApiClient
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultSendOTP: MutableLiveData<ResponseModel<Response<String>>> =
        MutableLiveData<ResponseModel<Response<String>>>()
    val resultSendOTPLD: LiveData<ResponseModel<Response<String>>> = resultSendOTP

    private var verifyOTPResult: MutableLiveData<ResponseModel<Response<String>>> =
        MutableLiveData<ResponseModel<Response<String>>>()
    val verifyOTPResultLD: LiveData<ResponseModel<Response<String>>> = verifyOTPResult

    private var registerUser: MutableLiveData<ResponseModel<SignResponse>> =
        MutableLiveData<ResponseModel<SignResponse>>()
    val registerUserLD: LiveData<ResponseModel<SignResponse>> = registerUser


    fun signUp(token: String, request: SignRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.registerUser(token, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    registerUser.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    registerUser.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }


    fun uploadImages(token: String, documentType: String, file: MultipartBody.Part,
        fileWaterMark: MultipartBody.Part,
        imgRes: TrucksFOImageController
    ) {
        val apiInterface = ApiClient().getClient
        apiInterface.uploadImages(token, documentType, "BusinessOfficer", file, fileWaterMark)
            ?.enqueue(object : Callback<TrucksupImageUploadResponse> {
                override fun onResponse(
                    call: Call<TrucksupImageUploadResponse>,
                    response: retrofit2.Response<TrucksupImageUploadResponse>
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