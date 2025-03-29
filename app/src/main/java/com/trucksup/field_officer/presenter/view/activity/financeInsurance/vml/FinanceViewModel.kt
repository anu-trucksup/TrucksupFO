package com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.network.ResponseModel
import com.trucksup.field_officer.data.network.ResultWrapper
import com.trucksup.field_officer.domain.usecases.APIUseCase
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinanceViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel()  {

    private var resultFinance: MutableLiveData<ResponseModel<FinanceDataLiatResponse>> = MutableLiveData<ResponseModel<FinanceDataLiatResponse>>()
    val resultFinanceLD: LiveData<ResponseModel<FinanceDataLiatResponse>> = resultFinance

    private var resultsubmitFinance: MutableLiveData<ResponseModel<FinaceDataSubmitResponse>> = MutableLiveData<ResponseModel<FinaceDataSubmitResponse>>()
    val resultsubmitFinanceLD: LiveData<ResponseModel<FinaceDataSubmitResponse>> = resultsubmitFinance


    fun getFinanceData(request: FinanceDataLiatRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getFinanceData(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?:"")
                    resultFinance.postValue(ResponseModel(serverError = response.error))
                }
                is ResultWrapper.Success -> {
                    resultFinance.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }

    fun submitFinanceData(request: LoanDataSubmitRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.submitFinanceData(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?:"")
                    resultsubmitFinance.postValue(ResponseModel(serverError = response.error))
                }
                is ResultWrapper.Success -> {
                    resultsubmitFinance.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }


}