package com.trucksup.field_officer.presenter.view.activity.growthPartner.model

data class GPOnboardingData(
    //GP Basic Detail
    val ContactName: String,
    val ContactNumber: String,
    val PartnerType: String,
    val PartnerSubType: String,

    //GP Personal Detail
    val ProfilePhoto: String,
    val ProfilePhotoURL: String,
    val SalesCodeofBO: String,
    val GPMobileNumber: String,
    val GPName: String,
    val BusinessName: String,
    val BusinessType: String,
    val Pincode: String,
    val City: String,
    val State: String,
    val BusinessAddress: String,

    //GP KYC Detail
    val KYCStatus: String,

    //GP Establishment Detail
    val EstablishmentPhotoKey: String,
    val EstablishmentPhotoURL: String,

    //Bank Account Details
    val AccountHolderName: String,
    val AccountNumber: String,
    val BankName: String,
    val IFSCCode: String,
    val UploadCheque: String,
    val PANNumber: String,

)