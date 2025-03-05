package com.trucksup.field_officer.presenter.view.activity.auth.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trucksup.field_officer.data.model.TokenZ
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val apiUseCase: APIUseCase,
                                         application: Application) : AndroidViewModel(application)  {

    var resultLogin: MutableLiveData<ResponseModel<TokenZ>> = MutableLiveData<ResponseModel<TokenZ>>()
    val resultLoginLD: LiveData<ResponseModel<TokenZ>> = resultLogin


    fun loginUser(username: String, Password: String, type: String,countryCode:String) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.loginUser(username, Password, type,"91")) {
                is ResultWrapper.NetworkError -> {
                    Log.e("API Error", response.error)
                    resultLogin.postValue(ResponseModel<TokenZ>(networkError = response.error))
                }
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?.message?:"")
                    resultLogin.postValue(ResponseModel<TokenZ>(serverResponseError = response.error?.message))
                }
                is ResultWrapper.GenericError -> {
                    Log.e("API Error", response.error)
                    resultLogin.postValue(ResponseModel<TokenZ>(genericError = response.error))
                }
                is ResultWrapper.Success -> {
                    resultLogin.postValue(ResponseModel<TokenZ>(success = response.value))
                }
            }
        }
    }

}