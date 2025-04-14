package com.trucksup.field_officer.data.model.otp

data class SmsRequest(
    var RequestType:String?=null,
    var ActionType:String?=null,
    var RequestedBy:String?=null,
    var MessageText:String?=null,
    var Recipients:String?=null,
    var Variables:String?=null,
    var DO_ID:String?=null,
    var AppSignatureKey:String,
    var caller:String,
    var DltTemplateId:String
)
