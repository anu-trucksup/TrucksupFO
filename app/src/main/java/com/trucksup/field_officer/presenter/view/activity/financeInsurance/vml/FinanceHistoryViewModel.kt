package com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse
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
class FinanceHistoryViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel()  {

    private var resultInquiryHistory: MutableLiveData<ResponseModel<InquiryHistoryResponse>> = MutableLiveData<ResponseModel<InquiryHistoryResponse>>()
    val resultInquiryHistoryLD: LiveData<ResponseModel<InquiryHistoryResponse>> = resultInquiryHistory


    fun inquiryHistory(request: InquiryHistoryRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.getInquiryHistory(
                PreferenceManager.getAuthTokenOld(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?:"")
                    resultInquiryHistory.postValue(ResponseModel(serverError = response.error))
                }
                is ResultWrapper.Success -> {
                    resultInquiryHistory.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }




}