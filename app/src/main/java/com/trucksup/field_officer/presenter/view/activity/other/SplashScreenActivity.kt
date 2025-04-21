package com.trucksup.field_officer.presenter.view.activity.other

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.databinding.ActivitySplashBinding
import com.trucksup.field_officer.databinding.AlartBoxBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import com.trucksup.field_officer.presenter.view.activity.dashboard.HomeActivity
import com.trucksup.field_officer.presenter.view.activity.other.vml.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private var mSplashBinding: ActivitySplashBinding? = null
    private var splashViewModel: SplashViewModel? = null
    private val locationPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {
                // Proceed to app
                startApp()
            } else {
                // Show dialog or close app
                showPermissionRequiredDialog()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mSplashBinding?.root)

        splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]

        if (isOnline(this)) {
            checkLocationPermission()

        } else {
            val abx = AlertBoxDialog(
                this@SplashScreenActivity,
                "Please Check your Internet Connectivity.",
                "m"
            )
            abx.show()
        }
    }

    private fun setupObserver() {
        splashViewModel!!.loggedInStatusLD.observe(this) { status: Int? ->
            when (status) {
                0 -> {
                    val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                    intent.putExtra("mobile", "")
                    startActivity(intent)
                    finish()
                }

                1 -> {
                    val intent2 = Intent(this@SplashScreenActivity, HomeActivity::class.java)
                    startActivity(intent2)
                    finish()
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()

    /*    if (isOnline(this)) {

            if (isLocationEnable(this)) {
                callMainScreen()
                setupObserver()
            } else {
                enableLocationPermission()
            }

        } else {
            val abx = AlertBoxDialog(
                this@SplashScreenActivity,
                "Please Check your Internet Connectivity.",
                "m"
            )
            abx.show()
        }*/

    }

    private fun callMainScreen() {                // call main screen
        Handler().postDelayed({ splashViewModel?.checkLoggedInStatus() }, 2000)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        // System.exit(0);
    }

    private fun locationDialog() {
        val builder = AlertDialog.Builder(this@SplashScreenActivity)
        val binding = AlartBoxBinding.inflate(LayoutInflater.from(this@SplashScreenActivity))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        binding.message.text = "Location permission is required to use this Application."

        //apply button
        binding.ok.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun checkLocationPermission() {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fine == PackageManager.PERMISSION_GRANTED || coarse == PackageManager.PERMISSION_GRANTED) {
            startApp()
        } else {

            locationPermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showPermissionRequiredDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Location permission is required to use this app.")
            .setCancelable(false)
            .setPositiveButton("Grant") { _, _ ->
                checkLocationPermission()
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .show()
    }

    private fun startApp() {
        // All permissions granted, continue with app flow
//        Toast.makeText(this, "Location permission granted!", Toast.LENGTH_SHORT).show()
        // For example, navigate to your HomeActivity
        callMainScreen()
        setupObserver()
    }

    private fun isOnline(context: Context): Boolean {
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

}