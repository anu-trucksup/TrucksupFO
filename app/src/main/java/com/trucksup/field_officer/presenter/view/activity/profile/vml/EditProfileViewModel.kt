package com.trucksup.field_officer.presenter.view.activity.profile.vml

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
class EditProfileViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var resultProfile: MutableLiveData<ResponseModel<PinCodeResponse>> =
        MutableLiveData<ResponseModel<PinCodeResponse>>()
    val resultProfileLD: LiveData<ResponseModel<PinCodeResponse>> = resultProfile

    fun updateProfile(token: String, request: PinCodeRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getCityStateByPin(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultProfile.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultProfile.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

}