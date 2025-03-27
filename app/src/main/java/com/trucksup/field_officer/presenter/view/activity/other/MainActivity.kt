package com.trucksup.field_officer.presenter.view.activity.other

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityMainBinding
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.view.activity.profile.EditProfileActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
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
        binding = ActivityMainBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        selectTab(binding.tabOwner.id)

        setListener()

        setNavigationMenu()
    }

    private fun setListener() {
        //drawer menu button
        binding.drawerBtn.setOnClickListener {
            binding.drawerLay.open()
        }

        //owner button
        binding.tabOwner.setOnClickListener {
            selectTab(R.id.tab_owner)
//            switchFragment(OwnerFragment())
        }

        //broker button
        binding.tabBroker.setOnClickListener {
            selectTab(R.id.tab_broker)
//            switchFragment(BrokerFragment())
        }

        //misc button
        binding.tabMisc.setOnClickListener {
            selectTab(R.id.tab_misc)
//            switchFragment(MiscFragment())
        }

        binding.dutySwitchBtn.setOnCheckedChangeListener { compoundButton, b ->
            if (b == true) {
                binding.tvOnDuty.text = "On Duty"
                binding.tvOnDuty.setTextColor(resources.getColor(R.color.on_duty_color))
                binding.dutySwitchBtn.trackTintList =
                    resources.getColorStateList(R.color.on_duty_color)
            } else {
                binding.tvOnDuty.text = "Off Duty"
                binding.tvOnDuty.setTextColor(resources.getColor(R.color.red))
                binding.dutySwitchBtn.trackTintList = resources.getColorStateList(R.color.red)
            }
            DialogBoxes.onOffDuty(this)
        }

        //close drawer
        binding.nn.navBackBtn.setOnClickListener {
            binding.drawerLay.close()
        }

        //edit profile button
        binding.nn.btnEditProfile.setOnClickListener {
            var intent = Intent(this@MainActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun selectTab(tabId: Int) {
        binding.tabOwner.setBackgroundResource(R.drawable.tab_unselected_background)
        binding.tabBroker.setBackgroundResource(R.drawable.tab_unselected_background)
        binding.tabMisc.setBackgroundResource(R.drawable.tab_unselected_background)
        binding.tabOwner.isSelected = false
        binding.tabBroker.isSelected = false
        binding.tabMisc.isSelected = false

        val selectedTab = findViewById<TextView>(tabId)
        selectedTab.setBackgroundResource(R.drawable.tab_selected_background)
        selectedTab.isSelected = true

        when (tabId) {
           // R.id.tab_owner -> switchFragment(TruckOwnerFragment())
           // R.id.tab_broker -> switchFragment(BusAssociateFragment())
            //R.id.tab_misc -> switchFragment(MiscFragment())
        }
    }

    private fun adjustFontScale(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }

    private fun checkPermissions(permissions: ArrayList<String>) {
        binding.addressShimmer.visibility = View.VISIBLE
        binding.addressUpdate.visibility = View.GONE
        Dexter.withContext(this@MainActivity)
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
                                    val geocoder =
                                        Geocoder(this@MainActivity, Locale.getDefault())
                                    val addresses = geocoder.getFromLocation(
                                        location!!.latitude,
                                        location!!.longitude,
                                        1
                                    )
                                    binding.addressUpdate.text =
                                        addresses?.get(0)?.getAddressLine(0)
                                    binding.addressShimmer.visibility = View.GONE
                                    binding.addressUpdate.visibility = View.VISIBLE
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

    private fun setNavigationMenu() {
       /* var list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        binding.listSlidermenu.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            adapter = NavigationMenuItem(this@MainActivity, list)
            hasFixedSize()
        }*/
    }

}