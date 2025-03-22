package com.trucksup.field_officer.presenter.view.activity.other

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.trucksup.field_officer.databinding.ActivityHomeBinding
import com.trucksup.field_officer.databinding.HomeMainServicesDialogBinding
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.FinanceActivity
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.InsuranceActivity
import com.trucksup.field_officer.presenter.view.activity.profile.EditProfileActivity
import com.trucksup.field_officer.presenter.view.activity.profile.MyEarningActivity
import com.trucksup.field_officer.presenter.view.adapter.HomeFeaturesAdapter
import com.trucksup.field_officer.presenter.view.adapter.ServicesMainAdapter
import com.trucksup.field_officer.presenter.view.adapter.OnItemClickListner
import com.trucksup.field_officer.presenter.view.adapter.TUKawachDialogAdapter
import com.trucksup.field_officer.presenter.view.adapter.NavigationMenuItem
import java.util.Locale


class HomeActivity : BaseActivity(), OnItemClickListner {
    private lateinit var binding: ActivityHomeBinding
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
        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        val view = binding.root
        setContentView(view)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setListonService()

        setSecondRvList()

        setListener()

        setNavigationMenu()
    }

    private fun setListonService() {

        binding.rvServices.apply {
            binding.rvServices.layoutManager = GridLayoutManager(this@HomeActivity, 3)
            adapter = ServicesMainAdapter(this@HomeActivity)
            hasFixedSize()
        }
    }


    private fun setSecondRvList() {

        val list = ArrayList<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        binding.rvFeatures.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 3)
            adapter = HomeFeaturesAdapter(this@HomeActivity, list)
            hasFixedSize()
        }
    }

    private fun setDialogRvList(binding: HomeMainServicesDialogBinding) {

        binding.rvMainServicesDialog.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = TUKawachDialogAdapter(this@HomeActivity)
            hasFixedSize()
        }
    }

    private fun openFeatureDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val binding = HomeMainServicesDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        setDialogRvList(binding)
        dialog.show()
    }

    private fun setListener() {

        binding.OnSwitchBtn.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.txtOnDuty.text = "On Duty"
                binding.txtOnDuty.setTextColor(resources.getColor(R.color.on_duty_color))
                binding.OnSwitchBtn.trackTintList =
                    resources.getColorStateList(R.color.on_duty_color)
            } else {
                binding.txtOnDuty.text = "Off Duty"
                binding.txtOnDuty.setTextColor(resources.getColor(R.color.red))
                binding.OnSwitchBtn.trackTintList = resources.getColorStateList(R.color.red)
            }
            DialogBoxes.onOffDuty(this)
        }

        //close drawer
        binding.nn.navBackBtn.setOnClickListener {
            binding.drawerLay.close()
        }

        //edit profile button
        binding.nn.btnEditProfile.setOnClickListener {
            val intent = Intent(this@HomeActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.llHome.setOnClickListener {

            setSelectionNav("home")
           /* val intent = Intent(this@HomeActivity, Ho::class.java)
            startActivity(intent)*/
        }

        binding.llOnboard.setOnClickListener {
            val intent = Intent(this@HomeActivity, NewOnboardingSelection::class.java)
            startActivity(intent)
        }

        binding.llHelp.setOnClickListener {
            val intent = Intent(this@HomeActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.homeEarnings.totalEarn.setOnClickListener {
            val intent = Intent(this@HomeActivity, MyEarningActivity::class.java)
            startActivity(intent)
        }

        binding.homeEarnings.todayFollowup.setOnClickListener {
            val intent = Intent(this@HomeActivity, FollowUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSelectionNav(str: String) {
        binding.homeCard.setCardBackgroundColor(resources.getColor(R.color.blue))
        binding.ivHome.setColorFilter(resources.getColor(R.color.white))
        //binding.llHome.setBackgroundColor(resources.getColor(R.color.blue));
    }

    private fun checkPermissions(permissions: ArrayList<String>) {
        binding.addressShimmer.visibility = View.VISIBLE
        binding.addressUpdate.visibility = View.GONE
        Dexter.withContext(this@HomeActivity)
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
                                        Geocoder(this@HomeActivity, Locale.getDefault())
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
        val list = ArrayList<NavItems>()
        list.add(NavItems(R.drawable.nav_my_business,"My Business","To view the details of my onboarded team."))
        list.add(NavItems(R.drawable.nav_targets,"Targets","To view my daily targets."))
        list.add(NavItems(R.drawable.travel_exp,"Travel Expenses","To request for travel expenses."))

        binding.listSlidermenu.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, RecyclerView.VERTICAL, false)
            adapter = NavigationMenuItem(this@HomeActivity, list)
            hasFixedSize()
        }
    }

    override fun onItemClick(pos : Int) {

        when (pos) {
            0 -> {
                openFeatureDialog(this)
            }
            1 -> {
                val intent = Intent(this, FinanceActivity::class.java)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, InsuranceActivity::class.java)
                startActivity(intent)
            }
        }
    }
}