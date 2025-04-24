package com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml

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
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllMeetUpBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.GetAllMeetupBAResponse
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpRequest
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.model.FollowUpResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllMeetUpTSResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.GetAllMeetupTSRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class BAFollowUpViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel() {

    private var getAllMeetUpBAResponse: MutableLiveData<ResponseModel<GetAllMeetupBAResponse>> =
        MutableLiveData<ResponseModel<GetAllMeetupBAResponse>>()
    val getAllMeetUpBAResponseLD: LiveData<ResponseModel<GetAllMeetupBAResponse>> = getAllMeetUpBAResponse

    fun getAllMeetupBA(token: String, request: GetAllMeetUpBARequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getAllMeetupBA(
                token,
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error ?: "")
                    getAllMeetUpBAResponse.postValue(ResponseModel(serverError = response.error))
                }

                is ResultWrapper.Success -> {
                    getAllMeetUpBAResponse.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

}