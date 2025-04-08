package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.tooling.data.position
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityBaMaptripBinding
import com.trucksup.field_officer.databinding.ActivityTsEndmaptripBinding
import com.trucksup.field_officer.databinding.ActivityTsMaptripBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.other.HomeActivity
import java.util.Locale
import kotlin.math.max
import kotlin.math.min


class TSEndTripActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    // Define your two fixed locations
    private val pointA = LatLng(28.510830, 77.085922)
    private val pointB = LatLng(28.646981, 77.125658)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ts_endmaptrip)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Mark the two fixed locations
        map.addMarker(MarkerOptions().position(pointA).title("Start Point"))
        map.addMarker(MarkerOptions().position(pointB).title("End Point"))

        // Draw a line between them
        map.addPolyline(
            PolylineOptions()
                .add(pointA, pointB)
                .width(5f)
                .color(Color.BLUE)
                .geodesic(true)
        )

        // Move camera to start point
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(pointA, 15f))

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 1000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val location = result.lastLocation ?: return
                val userLatLng = LatLng(location.latitude, location.longitude)

                // Optional: clear old marker and draw new one
                map.clear()
                map.addMarker(MarkerOptions().position(pointA).title("Start"))
                map.addMarker(MarkerOptions().position(pointB).title("End"))
                map.addMarker(MarkerOptions().position(userLatLng).title("You").icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))

                map.moveCamera(CameraUpdateFactory.newLatLng(userLatLng))

                // Optional: check if user is between pointA and pointB
                val isBetween = isUserBetween(userLatLng, pointA, pointB)
                Log.d("LOCATION_TRACK", "User between A and B? $isBetween")
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun isUserBetween(user: LatLng, start: LatLng, end: LatLng): Boolean {
        // Simple bounding box check — refine if needed
        val latMin = min(start.latitude, end.latitude)
        val latMax = max(start.latitude, end.latitude)
        val lngMin = min(start.longitude, end.longitude)
        val lngMax = max(start.longitude, end.longitude)

        return user.latitude in latMin..latMax && user.longitude in lngMin..lngMax
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
