package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityTsMaptripBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity


class TSStartTripActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityTsMaptripBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null

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
        binding = ActivityTsMaptripBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val title_name = intent.getStringExtra("title")
        binding.tvTitle.text = title_name

        binding.btnSubmit.setOnClickListener {
            val intent = Intent(this, EndTripActivity::class.java)
            intent.putExtra("title", "" + title_name)
            startActivity(intent)

        }


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    // This method is called when the map is ready to be used.
    override fun onMapReady(gMap: GoogleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        googleMap = gMap
        enableMyLocation()
    }

    private fun checkPermissions(permissions: ArrayList<String>) {

        Dexter.withContext(this@TSStartTripActivity)
            .withPermissions(
                permissions
            )
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            enableMyLocation()
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


    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        googleMap?.isMyLocationEnabled = true

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    googleMap?.addMarker(
                        MarkerOptions().position(currentLatLng).title("You are here")
                    )
                    googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
    }
}