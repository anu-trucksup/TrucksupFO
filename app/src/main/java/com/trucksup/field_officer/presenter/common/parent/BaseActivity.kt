package com.trucksup.field_officer.presenter.common.parent

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.dialog.ProgressDialogBox
import com.trucksup.field_officer.presenter.common.location.LocationHelper
import java.util.Locale

open class BaseActivity : AppCompatActivity() {
    private var address: String = ""
    private var progressDialog: ProgressDialogBox? = null
    var latitude: String = ""
    var longitude: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationPermission()
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

    override fun onBackPressed() {
        super.onBackPressed()
    }


    fun isLocationEnable(context: Context): Boolean {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted || coarseLocationGranted) {
            // Already granted
            return true
        } else {
            // Request permissions
            /*locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )*/
            return false
        }
    }


    fun enableLocationPermission() {
        // Request permissions
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
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

    fun isValidMobile(number: String): Boolean {
        val mobileRegex = Regex("^[6-9]\\d{9}\$")
        return mobileRegex.matches(number)
    }

    fun getSpecialCharacterCount(s: String?): Int {
        val blockCharacterSet =
            "1234567890~#^&|$%*!@/()[]-'\":;,?{}+=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪"
        blockCharacterSet.toCharArray()

        for (b in blockCharacterSet) {
            if (s!!.contains(b)) {
                return 0
            }
        }
        return 1
    }

    private fun checkLocationPermission() {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted || coarseLocationGranted) {
            // Already granted
            // getLocation()

            LocationHelper.getLastKnownLocation(this) { location ->
                location?.let {
                    val lat = it.latitude
                    val lon = it.longitude
                    // Use location
                } ?: run {
                    // Handle null location
                }
            }
        } else {
            // Request permissions
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            // Permissions granted
            // getLocation()

            LocationHelper.getLastKnownLocation(this) { location ->
                location?.let {
                    latitude = it.latitude.toString()
                    longitude = it.longitude.toString()

                    try {
                        val geocoder =
                            Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )
                        address = addresses?.get(0)?.getAddressLine(0).toString()
                    } catch (_: Exception) {

                    }
                    // Use location
                } ?: run {
                    // Handle null location
                }
            }
        } else {
            // Permissions denied
            showLocationDisabledDialog()
            // Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLocationDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("Location Services Disabled")
            .setMessage("Location services are required for this app. Please enable them in the settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { _, _ ->

                finishAffinity()
            }
            .setCancelable(false)
            .show()
    }

}
