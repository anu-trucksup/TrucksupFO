package com.trucksup.field_officer.presenter.view.activity.truckSupplier

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.databinding.ActivityTsMaptripBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.TSOnboardViewModel
import com.trucksup.field_officer.presenter.view.adapter.PlacesAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class TSStartTripActivity : BaseActivity(), OnMapReadyCallback {
    private var latLong: LatLng? = null
    private lateinit var binding: ActivityTsMaptripBinding
    private var googleMap: GoogleMap? = null
    private var mViewModel: TSOnboardViewModel? = null
    private val placeFields = listOf(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.ADDRESS,
        Place.Field.LAT_LNG
    )
    private lateinit var placesClient: PlacesClient
    private lateinit var token: AutocompleteSessionToken
    private lateinit var adapter: PlacesAdapter
    private val predictions = mutableListOf<AutocompletePrediction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTsMaptripBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this)[TSOnboardViewModel::class.java]

        // when the map is ready to be used.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        token = AutocompleteSessionToken.newInstance()
        placesClient = Places.createClient(this)

        val titleName = intent.getStringExtra("title")
        val address = intent.getStringExtra("address")
        val customDetails = intent.getStringExtra("customDetails")
        binding.tvTitle.text = titleName

        if (!address.isNullOrEmpty()) {
            validateAddressWithPlaces(address) { it ->
                if (it) {
                    binding.etAddress.setText(address)
                } else {
                    binding.etAddress.error = "Invalid or unknown address."
                }

            }

        }

        binding.etAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = binding.etAddress.text
                if (!text.isNullOrEmpty() && text.length > 2) {
                    searchPlaces(text.toString())
                } else {
                    predictions.clear()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.etPincode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.etPincode.text.length == 6) {
                    showProgressDialog(this@TSStartTripActivity, false)
                    val request = PinCodeRequest(
                        binding.etPincode.text.toString(),
                        PreferenceManager.getRequestNo(),
                        PreferenceManager.getPhoneNo(this@TSStartTripActivity)
                    )
                    mViewModel?.getCityStateByPin(PreferenceManager.getAuthToken(), request)

                } else {
                    setUI()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.btnSubmit.setOnClickListener {

            if (checkValidation()) {
                //val latLong = getLatLongFromAddress(this, binding.etAddress.text.toString())

                val intent = Intent(this, EndTripActivity::class.java)
                intent.putExtra("title", "" + titleName)
                intent.putExtra("currentAddress", "" + binding.etAddress.text.toString())
                intent.putExtra("currentLatitude", latLong?.latitude)
                intent.putExtra("currentLongitude", latLong?.longitude)
                intent.putExtra("customDetails", customDetails)
                startActivity(intent)
            }
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.ivClose.setOnClickListener {
            binding.rlAddress.visibility = View.GONE
            binding.etAddress.visibility = View.VISIBLE
            binding.etAddress.setText("")
        }

        setupObserver()
        setPlaceData()
    }

    private fun validateAddressWithPlaces(address: String, onResult: (Boolean) -> Unit) {

        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(address)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                val isValid = response.autocompletePredictions.isNotEmpty()
                onResult(isValid)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    private fun setPlaceData() {

        adapter = PlacesAdapter(predictions) { prediction ->
            binding.tvAddress.text = "${prediction.getPrimaryText(null)}"
            binding.rlAddress.visibility = View.VISIBLE
            binding.placesRecyclerView.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.etAddress.visibility = View.GONE
            Toast.makeText(this, "Clicked: ${prediction.getPrimaryText(null)}", Toast.LENGTH_SHORT)
                .show()

            val placeId = prediction.placeId

            val request = FetchPlaceRequest.builder(placeId, placeFields)
                .build()

            placesClient.fetchPlace(request)
                .addOnSuccessListener { response ->
                    val place = response.place
                    latLong = place.latLng
                }
                .addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Log.e("Places", "Place not found: ${exception.statusCode}")
                    }
                }
        }

        binding.placesRecyclerView.let { recyclerView ->
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
        }

    }

    private fun searchPlaces(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                predictions.clear()
                predictions.addAll(response.autocompletePredictions)
                adapter.notifyDataSetChanged()
                binding.placesRecyclerView.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.rlAddress.visibility = View.GONE
                // binding.etAddress.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Log.e("Places", "Error getting predictions: ${e.message}")
            }
    }

    private fun setUI() {
        binding.etCity.setText("")
        binding.etState.setText("")
    }

    private fun getLatLongFromAddress(context: Context, addressStr: String): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses: MutableList<Address>? = geocoder.getFromLocationName(addressStr, 1)
            if (addresses!!.isNotEmpty()) {
                val location = addresses[0]
                Pair(location.latitude, location.longitude)
                return location
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun setupObserver() {
        mViewModel?.resultSCbyPinCodeLD?.observe(this@TSStartTripActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@TSStartTripActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statusCode == 200) {
                    if (responseModel.success.data.isNotEmpty()) {
                        pinData(
                            responseModel.success.data[0].district,
                            responseModel.success.data[0].hindiCity,
                            responseModel.success.data[0].stateName,
                            responseModel.success.data[0].hindiState
                        )
                    } else {
                        val abx = AlertBoxDialog(
                            this@TSStartTripActivity,
                            responseModel.success.message,
                            "m"
                        )
                        abx.show()

                        setUI()

                        binding.etPincode.setText("")
                    }
                } else {
                    val abx = AlertBoxDialog(
                        this@TSStartTripActivity,
                        responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }
            }
        }

    }

    private fun pinData(city: String, cityHindi: String, state: String, stateHindi: String) {
        dismissProgressDialog()

        binding.etCity.setText(city)
        binding.etState.setText(state)

    }

    private fun checkValidation(): Boolean {
        if (TextUtils.isEmpty(binding.etAddress.text)) {
            LoggerMessage.onSNACK(
                binding.etAddress,
                resources.getString(R.string.enter_add),
                this
            )
            return false
        }

        if (TextUtils.isEmpty(binding.etPincode.text)) {
            LoggerMessage.onSNACK(
                binding.etPincode,
                resources.getString(R.string.enter_pin),
                this
            )
            return false
        }


        if (TextUtils.isEmpty(binding.etCity.text)) {
            LoggerMessage.onSNACK(
                binding.etCity,
                resources.getString(R.string.enter_city),
                this
            )
            return false
        }

        if (TextUtils.isEmpty(binding.etState.text)) {
            LoggerMessage.onSNACK(
                binding.etState,
                resources.getString(R.string.enter_state),
                this
            )
            return false
        }

        if (latLong == null) {
            LoggerMessage.onSNACK(
                binding.etState,
                "Address is not valid",
                this
            )
            return false
        }

        return true
    }

    // This method is called when the map is ready to be used.
    override fun onMapReady(gMap: GoogleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        googleMap = gMap
        //enableMyLocation()

        setLocation()
    }

    /* private fun checkPermissions(permissions: ArrayList<String>) {

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
     }*/

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


    /* private fun enableMyLocation() {
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
     }*/

    private fun setLocation() {
        googleMap?.isMyLocationEnabled = true
        checkLocationPermission() {
            latitude?.let {
                val currentLatLng = LatLng(latitude.toDouble(), longitude.toDouble())
                googleMap?.addMarker(
                    MarkerOptions().position(currentLatLng).title("You are here")
                )
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }

        }
    }
}