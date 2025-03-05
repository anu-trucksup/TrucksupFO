package com.trucksup.field_officer.presenter.common.permission

interface PermissionResultCallback {
    fun PermissionGranted(request_code: Int)
    fun PartialPermissionGranted(request_code: Int, granted_permissions: ArrayList<String>)
    fun PermissionDenied(request_code: Int)
    fun NeverAskAgain(request_code: Int)
}

