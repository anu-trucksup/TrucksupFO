package com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.logistics.trucksup.activities.preferre.modle.PrefferLanRequest
import com.logistics.trucksup.activities.preferre.modle.PrefferdResponse
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PinCodeResponse
import com.trucksup.field_officer.data.model.image.TrucksupImageUploadResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.cityPicker.ApiClient
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.AddTruckDialog
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.VerifyTruckResponse
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
class TSOnboard2ViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultVerifyTruck: MutableLiveData<ResponseModel<VerifyTruckResponse>> =
        MutableLiveData<ResponseModel<VerifyTruckResponse>>()
    val resultVerifyTruckLD: LiveData<ResponseModel<VerifyTruckResponse>> = resultVerifyTruck

    private var resultRcResponse: MutableLiveData<ResponseModel<RcResponse>> =
        MutableLiveData<ResponseModel<RcResponse>>()
    val resultRcResponseLD: LiveData<ResponseModel<RcResponse>> = resultRcResponse

    private var resultLoadResponse: MutableLiveData<ResponseModel<AddLoadFilterResponse>> =
        MutableLiveData<ResponseModel<AddLoadFilterResponse>>()
    val resultLoadResponseLD: LiveData<ResponseModel<AddLoadFilterResponse>> = resultLoadResponse

    private var onBoardTsResponse: MutableLiveData<ResponseModel<PrefferdResponse>> =
        MutableLiveData<ResponseModel<PrefferdResponse>>()
    val onBoardTsResponseLD: LiveData<ResponseModel<PrefferdResponse>> = onBoardTsResponse

    private var resultSCbyPinCode: MutableLiveData<ResponseModel<PinCodeResponse>> =
        MutableLiveData<ResponseModel<PinCodeResponse>>()
    val resultSCbyPinCodeLD: LiveData<ResponseModel<PinCodeResponse>> = resultSCbyPinCode


    fun getCityStateByPin(token: String, request: PinCodeRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getCityStateByPin(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultSCbyPinCode.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultSCbyPinCode.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun verifyTruckDetails(authToken: String, vehicleRegNo: String?, mobileNo: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.verifyTruck(
                authToken, vehicleRegNo, mobileNo
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultVerifyTruck.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultVerifyTruck.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun getRcDetails(authToken: String, request: RcRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getRcDetails(authToken, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultRcResponse.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultRcResponse.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun getLoadFilter(
        request: AddLoadFilterRequest,
        addTruckDialog: AddTruckDialog,
        context: AppCompatActivity) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getAddLoadFilter(request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultLoadResponse.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultLoadResponse.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun onBoardTruckSupplier(request: PrefferLanRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response =
                apiUseCase.onBoardTruckSupplier(PreferenceManager.getAuthToken(), request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    onBoardTsResponse.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    onBoardTsResponse.postValue(ResponseModel(success = response.value))
                }
            }
        }

    }

    fun uploadProfileImage(
        token: String,
        documentType: String, file: MultipartBody.Part,
        fileWaterMark: MultipartBody.Part,
        imgRes: TrucksFOImageController
    ) {
        val apiInterface = ApiClient().getClient
        apiInterface.uploadImages(token, documentType,"TruckSupplier", file, fileWaterMark)

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