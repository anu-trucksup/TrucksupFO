package com.trucksup.field_officer.presenter.view.activity.miscellaneous.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.authModel.SignRequest
import com.trucksup.field_officer.data.model.authModel.SignResponse
import com.trucksup.field_officer.data.model.home.HomeCountRequest
import com.trucksup.field_officer.data.model.home.HomeCountResponse
import com.trucksup.field_officer.data.model.image.TrucksupImageUploadResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.cityPicker.ApiClient
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.AddMiscLeadRequest
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.AddMiscLeadsResponse
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.GetAllMiscLeadResponse
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.GetMiscLeadRequest
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.UpdateMiscLeadsRequest
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.UpdateMiscLeadsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import javax.inject.Inject

@HiltViewModel
class MiscellaneousViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var addMiscellaneousResult: MutableLiveData<ResponseModel<AddMiscLeadsResponse>> =
        MutableLiveData<ResponseModel<AddMiscLeadsResponse>>()
    val addMiscellaneousResultLD: LiveData<ResponseModel<AddMiscLeadsResponse>> = addMiscellaneousResult

    private var getAllMiscResult: MutableLiveData<ResponseModel<GetAllMiscLeadResponse>> =
        MutableLiveData<ResponseModel<GetAllMiscLeadResponse>>()
    val getAllMiscResultLD: LiveData<ResponseModel<GetAllMiscLeadResponse>> = getAllMiscResult

    private var updateMiscLeadsResult: MutableLiveData<ResponseModel<UpdateMiscLeadsResponse>> =
        MutableLiveData<ResponseModel<UpdateMiscLeadsResponse>>()
    val updateMiscLeadsResultLD: LiveData<ResponseModel<UpdateMiscLeadsResponse>> = updateMiscLeadsResult


    fun addMiscellaneous(token: String, request: AddMiscLeadRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.addMiscLeadsByBO(token, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    addMiscellaneousResult.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    addMiscellaneousResult.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun getAllMiscellaneous(token: String, request: GetMiscLeadRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getBOMiscLeads(token, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    getAllMiscResult.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    getAllMiscResult.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun updateMiscLeadsByBO(token: String, request: UpdateMiscLeadsRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.updateMiscLeadsByBO(token, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    updateMiscLeadsResult.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    updateMiscLeadsResult.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun uploadImages(token: String, documentType: String, file: MultipartBody.Part,
                     fileWaterMark: MultipartBody.Part, imgRes: TrucksFOImageController
    ) {
        val apiInterface = ApiClient().getClient
        apiInterface.uploadImages(token, documentType, "Miscellaneous", file, fileWaterMark)
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