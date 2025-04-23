package com.trucksup.field_officer.presenter.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.trucksup.field_officer.presenter.common.location.LocationHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class CommonApplication: Application() {
    companion object {
        const val imagePathUrl: String = "https://sslapi.trucksups.in/S3ImageAPI/get-imagefile?fileName="
        var BASE_API_URL: String = "https://testapi.trucksups.in/"
        var appContext: CommonApplication? = null
        private var sharedPreferences: SharedPreferences? = null
        var token: String? = ""

        fun getSharedPreferences(): SharedPreferences? {
            if(appContext != null) {
                sharedPreferences = appContext?.getSharedPreferences("trucksupfo_pref", Context.MODE_PRIVATE)
                return sharedPreferences
            }
            throw Exception("Application Class not extended from BaseApplication class")
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        FirebaseApp.initializeApp(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration tokenfailed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
           token = task.result

            // Log and notify the callback
            Log.d("TAG", "FCM Token: $token")
            // Log.d(TAG, "Device Token: $token"+ FontVariation.Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
            //onTokenReceived(token)
        }

        LocationHelper.init(this)
    }


}