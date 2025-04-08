package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityTsEndmaptripBinding
import com.trucksup.field_officer.databinding.MeetingScheduleAlertBinding
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.business_associate.BAScheduleMeetingActivity
import com.trucksup.field_officer.presenter.view.activity.growth_partner.GPScheduleMeetingActivity


class EndTripActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityTsEndmaptripBinding
    private lateinit var gmap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    // Define your two fixed locations
    private var startpointA = LatLng(28.510830, 77.085922)
    private val destinationpointB = LatLng(28.646981, 77.125658)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTsEndmaptripBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setClickListener()
    }

    private fun setClickListener() {
        val title_name = intent.getStringExtra("title")
        binding.tvTitle.text = title_name

        binding.btnSubmit.setOnClickListener {
            meetingScheduleDialog(title_name)
        }


        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        gmap = googleMap

        startLocationUpdates()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
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
                startpointA = LatLng(location.latitude, location.longitude)

                // Optional: clear old marker and draw new one
                gmap.addMarker(
                    MarkerOptions().position(startpointA).title("You").icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    )
                )
                gmap.addMarker(MarkerOptions().position(destinationpointB).title("Destination").
                icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_icon)))


                //map.moveCamera(CameraUpdateFactory.newLatLngZoom(startpointA, 15f))
                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationpointB, 10f))

            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun open_mapview(view: View) {
        openRouteInGoogleMaps(
            context = this,  // or requireContext() if inside a Fragment
            startLat = startpointA.latitude,
            startLng = startpointA.longitude,
            destLat = destinationpointB.latitude,
            destLng = destinationpointB.longitude
        )

    }

    private fun openRouteInGoogleMaps(
        context: Context,
        startLat: Double,
        startLng: Double,
        destLat: Double,
        destLng: Double
    ) {
        val uri = Uri.parse("google.navigation:q=$destLat,$destLng&mode=d")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "Google Maps app not installed", Toast.LENGTH_SHORT).show()
        }
    }


    private fun meetingScheduleDialog(title_name: String?) {
        val builder = AlertDialog.Builder(this@EndTripActivity)
        val binding = MeetingScheduleAlertBinding.inflate(LayoutInflater.from(this@EndTripActivity))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        //apply button
        binding.btnhappiness.setOnClickListener {
            dialog.dismiss()
            val happinessCodeBox = HappinessCodeBox(this, getString(R.string.hapinessCodeMsg),
                getString(R.string.EnterHappinessCode),
                getString(R.string.resand_sms))
            happinessCodeBox.show()
        }

        //cancel button
        binding.btnReschedule.setOnClickListener {
            dialog.dismiss()

            if (title_name.equals(getString(R.string.gp_followup), ignoreCase = true)){
                startActivity(Intent(this, GPScheduleMeetingActivity::class.java))
            }else if (title_name.equals(getString(R.string.ba_follow_up), ignoreCase = true)){
                startActivity(Intent(this, BAScheduleMeetingActivity::class.java))
            }else if (title_name.equals(getString(R.string.ts_followup), ignoreCase = true)){
                startActivity(Intent(this, TSScheduledMeetingActivity::class.java))
            }
        }
    }

}
