package com.trucksup.field_officer.presenter.common.parent

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.trucksup.field_officer.presenter.common.dialog.ProgressDialogBox
import java.util.Locale

open class BaseActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialogBox? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    fun showProgressDialog(
        context: Context?,
        isCancelable: Boolean
    ) {
        dismissProgressDialog()
        if (context != null) {
            try {
                progressDialog = ProgressDialogBox(context)
                progressDialog?.let { jarvisLoader ->
                    jarvisLoader.setCanceledOnTouchOutside(true)
                    jarvisLoader.setCancelable(isCancelable)
                    jarvisLoader.show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun dismissProgressDialog() {
        if (progressDialog != null && progressDialog?.isShowing!!) {
            progressDialog = try {
                progressDialog?.dismiss()
                null
            } catch (e: Exception) {
                null
            }
        }
    }


    fun showInAnimation() {
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }

        return false
    }

    fun adjustFontScale(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }

   /* fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches() && phone.length == 10
    }*/
    fun isValidMobile(number: String): Boolean {
        val mobileRegex = Regex("^[6-9]\\d{9}\$")
        return mobileRegex.matches(number)
    }

}
