package com.trucksup.field_officer.presenter.common.parent

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
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
import java.io.IOException
import java.util.Locale

open class BaseActivity : AppCompatActivity() {
    private val permissionCallbackMap = mutableMapOf<String, () -> Unit>()
    private var progressDialog: ProgressDialogBox? = null
    var latitude: String = ""
    var longitude: String = ""
    var address: String = ""

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

    fun checkLocationPermission(onLocationReady: (() -> Unit)? = null) {
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
            LocationHelper.getLastKnownLocation(this) { location ->
                location?.let {
                    latitude = it.latitude.toString()
                    longitude = it.longitude.toString()

                    try {
                        fetchAddressFromLocation(location)
                        onLocationReady?.invoke()
                    } catch (_ex: Exception) {
                        Log.e("", "" + _ex)
                    }
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

    private fun fetchAddressFromLocation(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            )

            if (!addresses.isNullOrEmpty()) {
                address = addresses[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            // Permissions granted

            LocationHelper.getLastKnownLocation(this) { location ->
                location?.let {
                    latitude = it.latitude.toString()
                    longitude = it.longitude.toString()

                    try {
                        val geocoder =
                            Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(
                            it.latitude,
                            it.longitude,
                            1
                        )
                        address = addresses?.get(0)?.getAddressLine(0).toString()
                    } catch (_ex: Exception) {
                        Log.e("", "" + _ex)
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


    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { (permission, isGranted) ->
            if (isGranted) {
                permissionCallbackMap[permission]?.invoke()
            } else {
               // Toast.makeText(this, "$permission permission denied", Toast.LENGTH_SHORT).show()
                showCameraDisabledDialog()
            }
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

    private fun showCameraDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("Camera Permission Required")
            .setMessage("Camera access is required to take photos. Please enable it in app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showStorageDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gallery Permission Required")
            .setMessage("Gallery access is required to take photos. Please enable it in app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }


    fun requestCameraAndGalleryPermissions(onGranted: () -> Unit) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13+, adjust to use READ_MEDIA_IMAGES
            val mediaPermissions = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            ).filter {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }

            if (mediaPermissions.isEmpty()) {
                onGranted()
            } else {
                mediaPermissions.forEach {
                    permissionCallbackMap[it] = onGranted
                }
                permissionLauncher.launch(mediaPermissions.toTypedArray())
               /* val permanentlyDenied = mediaPermissions.any {
                    !ActivityCompat.shouldShowRequestPermissionRationale(this, it)
                }


                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    showCameraDisabledDialog()
                } else {
                    onGranted()
                }


                permissionLauncher.launch(mediaPermissions.toTypedArray())

                if (permanentlyDenied) {
                    // We wait until after permission result to show dialog

                }*/

            }
        } else {
            val neededPermissions = listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE // Or use READ_MEDIA_IMAGES for SDK 33+
            ).filter {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }

            if (neededPermissions.isEmpty()) {
                onGranted()
            } else {
                neededPermissions.forEach {
                    permissionCallbackMap[it] = onGranted
                }
                permissionLauncher.launch(neededPermissions.toTypedArray())
            }
        }

    }

}
