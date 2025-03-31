package com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml

data class LoanDataSubmitRequest(
    val city: String,
    val createdBy: String,
    val loanAmount: String,
    val mobileNumber: String,
    val name: String,
    val profileType: String,
    val requestDateTime: String,
    val requestId: String,
    val requestedBy: String,
    val state: String,
    val typeOfLoan: String,
    val financeFor:String,
    val userMobileNo:String,
    val referralCode:String,
    val Source:String
)