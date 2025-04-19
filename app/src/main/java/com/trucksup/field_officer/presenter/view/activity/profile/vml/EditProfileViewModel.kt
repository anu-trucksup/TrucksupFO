package com.trucksup.field_officer.presenter.view.activity.profile.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.GetUserProfileResponse
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PinCodeResponse
import com.trucksup.field_officer.data.model.image.ImageResponse
import com.trucksup.field_officer.data.model.user.GetProfileRequest
import com.trucksup.field_officer.data.model.user.GetProfileResponse
import com.trucksup.field_officer.data.model.user.UpdateProfileRequest
import com.trucksup.field_officer.data.model.user.UpdateProfileResponse
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

    private var resultUpdateProfile: MutableLiveData<ResponseModel<UpdateProfileResponse>> =
        MutableLiveData<ResponseModel<UpdateProfileResponse>>()
    val resultUpdateProfileLD: LiveData<ResponseModel<UpdateProfileResponse>> = resultUpdateProfile

    private var resultGetProfile: MutableLiveData<ResponseModel<GetProfileResponse>> =
        MutableLiveData<ResponseModel<GetProfileResponse>>()
    val resultGetProfileLD: LiveData<ResponseModel<GetProfileResponse>> = resultGetProfile

    fun updateProfile(token: String, request: UpdateProfileRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.updateUserProfile(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultUpdateProfile.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultUpdateProfile.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun getProfile(token: String, request: GetProfileRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getUserProfile(token, request)) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    resultGetProfile.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    resultGetProfile.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

}