package com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml

import com.trucksup.field_officer.presenter.view.activity.financeInsurance.chipData


data class FinanceDataLiatResponse(
    val inquiryDetails: List<chipData>? = null,
    val message: String? = null,
    val requestId: String? = null,
    val requestedBy: String? = null,
    val status: String? = null,
    val statuscode: Int? = null
)

