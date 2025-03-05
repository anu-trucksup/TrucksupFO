package com.trucksup.field_officer.presenter.view.activity.financeInsurance

import com.trucksup.field_officer.data.model.insurance.InquiryHistoryResponse


interface EnquiryHistoryController {

    fun inquiryHistorySuccess(inquiryHistoryResponse: InquiryHistoryResponse)
    fun inquiryHistoryFailure(msg:String)

}