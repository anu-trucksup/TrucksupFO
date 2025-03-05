package com.trucksup.field_officer.data.di

import android.util.Log
import com.trucksup.field_officer.presenter.utils.CommonApplication
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    val srdp = CommonApplication.getSharedPreferences()
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = srdp?.getString("access_token", null)
        val builder = chain.request()
            .newBuilder()

        if (token != null) {
            // start home screen
            // move to home screen
            Log.d("RETROFIT-LOG", "bearear token ==> $token")
            builder.addHeader("Authorization", "Basic $token")
        }
        val request: Request = builder.build()
        return chain.proceed(request)
    }
}