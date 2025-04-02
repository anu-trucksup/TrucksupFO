package com.trucksup.field_officer.presenter.common.parent

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.trucksup.field_officer.presenter.common.dialog.ProgressDialogBox

open class BaseActivity : AppCompatActivity() {
    private var progressDialog: ProgressDialogBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    fun isValidMobile(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches() && phone.length == 10
    }
}
