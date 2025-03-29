package com.trucksup.field_officer.presenter.view.activity.truck_supplier

import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityBaMaptripBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity


class TSStartTripActivity : BaseActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityBaMaptripBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaMaptripBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)

        binding.btnSubmit.setOnClickListener {

            /* val happinessCodeBox = HappinessCodeBox(this, getString(R.string.hapinessCodeMsg),
                 getString(R.string.EnterHappinessCode),
                 getString(R.string.resand_sms))
             happinessCodeBox.show()*/
            startActivity(Intent(this, TSScheduledMeetingActivity::class.java))
        }
    }


    // This method is called when the map is ready to be used.
    override fun onMapReady(googleMap: GoogleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
       /* var location = Location(this)
        val myPos: LatLng = LatLng(Location.getLatitude(), Location.getLongitude())
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myPos))*/
    }
}