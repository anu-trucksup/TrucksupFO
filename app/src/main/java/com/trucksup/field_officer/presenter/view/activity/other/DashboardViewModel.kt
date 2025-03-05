package com.trucksup.field_officer.presenter.view.activity.other

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trucksup.field_officer.data.model.PrivacyAllResponse
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(val apiUseCase: APIUseCase, application: Application) :
    AndroidViewModel(application) {


    var navAllResponse: MutableLiveData<ResponseModel<PrivacyAllResponse>> = MutableLiveData<ResponseModel<PrivacyAllResponse>>()
    val navAllResponseLD: LiveData<ResponseModel<PrivacyAllResponse>> = navAllResponse

  /*  fun allPrivacydetails() {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.privacyDetails("Property")) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    navAllResponse.postValue(ResponseModel<PrivacyAllResponse>(networkError = response.error))
                }
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message?:"")
                    navAllResponse.postValue(ResponseModel<PrivacyAllResponse>(serverResponseError = response.error?.message))
                }
                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    navAllResponse.postValue(ResponseModel<PrivacyAllResponse>(genericError = response.error))
                }
                is ResultWrapper.Success -> {
                    navAllResponse.postValue(ResponseModel<PrivacyAllResponse>(success = response.value))
                }

                else -> {}
            }
        }
    }*/

}
