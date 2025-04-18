package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGpMaptripBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity


class GPStartTripActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityGpMaptripBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var googleMap: GoogleMap

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
        binding = ActivityGpMaptripBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnSubmit.setOnClickListener {

            /* val happinessCodeBox = HappinessCodeBox(this, getString(R.string.hapinessCodeMsg),
                 getString(R.string.EnterHappinessCode),
                 getString(R.string.resand_sms))
             happinessCodeBox.show()*/
            startActivity(Intent(this, GPScheduleMeetingActivity::class.java))
        }


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    // This method is called when the map is ready to be used.
    override fun onMapReady(googleMap: GoogleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        this.googleMap = googleMap
        // Specify the location
        val myLocation = LatLng(37.7749, -122.4194) // San Francisco

        // Move the camera
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation, 15f)
        googleMap.animateCamera(cameraUpdate)

        // Add a marker
        val markerOptions = MarkerOptions()
            .position(myLocation)
            .title("My Location")
        googleMap.addMarker(markerOptions)
    }

    private fun checkPermissions(permissions: ArrayList<String>) {

        Dexter.withContext(this@GPStartTripActivity)
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
                                    val myPos = LatLng(location?.latitude!!, location.longitude)
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(myPos))

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