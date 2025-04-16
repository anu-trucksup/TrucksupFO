package com.trucksup.field_officer.presenter.view.activity.other

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.trucksup.field_officer.databinding.ActivityWelcomeLocationBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.dashboard.HomeActivity
import java.util.Locale

class WelcomeLocationActivity : BaseActivity() {

    private lateinit var binding: ActivityWelcomeLocationBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onStart() {
        super.onStart()
        val permissionList = ArrayList<String>()
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        checkPermissions(permissionList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeLocationBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        /*  Handler().postDelayed({ startActivity(Intent(baseContext,
              HomeActivity::class.java)) }, 3000)*/

    }

    private fun checkPermissions(permissions: ArrayList<String>) {
        binding.addressShimmer.visibility = View.VISIBLE
        binding.addressUpdate.visibility = View.GONE
        Dexter.withContext(this@WelcomeLocationActivity)
            .withPermissions(
                permissions
            )
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            fusedLocationClient.getCurrentLocation(
                                Priority.PRIORITY_HIGH_ACCURACY,
                                CancellationTokenSource().token
                            ).addOnSuccessListener { location: Location? ->
                                try {
                                    val geocoder = Geocoder(this@WelcomeLocationActivity, Locale.getDefault())
                                    val addresses = geocoder.getFromLocation(
                                        location!!.latitude,
                                        location!!.longitude,
                                        1
                                    )
                                    binding.addressUpdate.text =
                                        addresses?.get(0)?.getAddressLine(0)
                                    Log.e("location", "location:" + addresses?.get(0))
                                    binding.addressShimmer.visibility = View.GONE
                                    binding.addressUpdate.visibility = View.VISIBLE

                                    Handler().postDelayed({
                                        startActivity(
                                            Intent(
                                                baseContext,
                                                HomeActivity::class.java
                                            )
                                        )

                                        finish()
                                    }, 3000)
                                } catch (e: Exception) {
//                                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                                    startActivity(intent)
                                    showLocationDisabledDialog()
                                }
                            }
                        }

                        if (report.isAnyPermissionPermanentlyDenied) {
//                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                            startActivity(intent)
                            showLocationDisabledDialog()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    // Remember to invoke this method when the custom rationale is closed
                    // or just by default if you don't want to use any custom rationale.
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            }
            .check()
    }

    private fun showLocationDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle("Location Services Disabled")
            .setMessage("Location services are required for this app. Please enable them in the settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            .setCancelable(false)
            .show()
    }

}