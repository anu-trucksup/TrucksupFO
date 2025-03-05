package com.trucksup.field_officer.presenter.view.activity.other

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(val apiUseCase: APIUseCase,
                                          application: Application): AndroidViewModel(application) {
    var loggedInStatus: MutableLiveData<Int> = MutableLiveData<Int>()
    val loggedInStatusLD: LiveData<Int> = loggedInStatus

    @SuppressLint("SuspiciousIndentation")
    fun checkLoggedInStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            val isUserLoggedIn = apiUseCase.isUserLoggedIn()
            if (isUserLoggedIn) {
                loggedInStatus.postValue(1)
            } else {
                loggedInStatus.postValue(0)
            }
        }
    }

    /*var forceUpdateResponse: MutableLiveData<ResponseModel<ForceUpdateResponse>> = MutableLiveData<ResponseModel<ForceUpdateResponse>>()
    val forceUpdateResponseLD: LiveData<ResponseModel<ForceUpdateResponse>> = forceUpdateResponse

    fun getForceUpdate() {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.forceUpdate()) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    forceUpdateResponse.postValue(ResponseModel<ForceUpdateResponse>(networkError = response.error))
                }
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message?:"")
                    forceUpdateResponse.postValue(ResponseModel<ForceUpdateResponse>(serverResponseError = response.error?.message))
                }
                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    forceUpdateResponse.postValue(ResponseModel<ForceUpdateResponse>(genericError = response.error))
                }
                is ResultWrapper.Success -> {
                    forceUpdateResponse.postValue(ResponseModel<ForceUpdateResponse>(success = response.value))
                }
            }
        }
    }*/
}