package com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trucksupui.model.SubmitInsuranceInquiryData
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
class InsuranceViewModel @Inject constructor(val apiUseCase: APIUseCase) : ViewModel()  {

    private var resultsubmitInsurance: MutableLiveData<ResponseModel<SubmitInsuranceInquiryData>> = MutableLiveData<ResponseModel<SubmitInsuranceInquiryData>>()
    val resultsubmitInsuranceLD: LiveData<ResponseModel<SubmitInsuranceInquiryData>> = resultsubmitInsurance


    fun submitInsuranceData(request: SubmitInsuranceInquiryRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = apiUseCase.submitInsuranceInquiry(
                PreferenceManager.getAuthToken(),
                request
            )) {
                is ResultWrapper.ServerResponseError -> {
                    Log.e("API Error", response.error?:"")
                    resultsubmitInsurance.postValue(ResponseModel(serverError = response.error))
                }
                is ResultWrapper.Success -> {
                    resultsubmitInsurance.postValue(ResponseModel(success = response.value))
                }
            }
        }
    }


}