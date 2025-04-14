package com.trucksup.field_officer.presenter.view.activity.businessAssociate.model

data class AddBrokerRequest(
    val actionType: String,
    val registrationNo: String,
    val requestNumber: String,
    val requestedBy: String,
    val brokerName: String,
    val city: String,
    val firmAddress: String,
    val firmName: String,
    val mobilenumber: String,
    val state: String,
    val documentType: String,
    val imageupload:String,
    val throughdocument:String,
    var throughdate: String,
    var BrokerStatus:String,
    var pincode:String
)