package com.trucksup.field_officer.presenter.view.activity.auth.logout

interface LogoutManager {
    fun onClickLogout()
    fun logout(type: String)
    fun logoutError(error:String)
}