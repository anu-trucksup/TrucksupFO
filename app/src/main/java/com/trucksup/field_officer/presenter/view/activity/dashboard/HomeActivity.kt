package com.trucksup.field_officer.presenter.view.activity.dashboard

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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
import com.trucksup.field_officer.data.model.HomeServicesModel
import com.trucksup.field_officer.data.model.home.HomeCountRequest
import com.trucksup.field_officer.data.model.home.MenuItemsCount
import com.trucksup.field_officer.data.model.home.OtherItemsCount
import com.trucksup.field_officer.data.model.home.TodayPerformanceCount
import com.trucksup.field_officer.data.model.home.UserDetails
import com.trucksup.field_officer.databinding.ActivityHomeBinding
import com.trucksup.field_officer.databinding.HomeMainServicesDialogBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.HelplineBox
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutAlertBox
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutManager
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutRequest
import com.trucksup.field_officer.presenter.view.activity.dashboard.vml.DashBoardViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.FinanceActivity
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.InsuranceActivity
import com.trucksup.field_officer.presenter.view.activity.other.FollowUpActivity
import com.trucksup.field_officer.presenter.view.activity.other.NavItems
import com.trucksup.field_officer.presenter.view.activity.other.NewOnboardingSelection
import com.trucksup.field_officer.presenter.view.activity.profile.EditProfileActivity
import com.trucksup.field_officer.presenter.view.activity.profile.MyEarningActivity
import com.trucksup.field_officer.presenter.view.activity.smartfuel.AddSmartFuelActivity
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.unassigned_ts_ba.activity.UnAssignedTSBAActivity
import com.trucksup.field_officer.presenter.view.adapter.HomeFeaturesAdapter
import com.trucksup.field_officer.presenter.view.adapter.ServicesMainAdapter
import com.trucksup.field_officer.presenter.view.adapter.OnItemClickListener
import com.trucksup.field_officer.presenter.view.adapter.TUKawachDialogAdapter
import com.trucksup.field_officer.presenter.view.adapter.NavigationMenuItem
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class HomeActivity : BaseActivity(), OnItemClickListener, LogoutManager {
    private lateinit var binding: ActivityHomeBinding
    private var mViewModel: DashBoardViewModel? = null
    private var dutyStatus: Boolean = false
    private var trackingCount: String? = null
    private var verificationCount: String? = null
    private var dlCount: String? = null


    override fun onStart() {
        super.onStart()
        val permissionList = ArrayList<String>()
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        //checkPermissions(permissionList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        adjustFontScale(getResources().configuration, 1.0f)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this)[DashBoardViewModel::class.java]

        //main services
        mainServices(null)

        //earnings
        earningCounts(null)

        //today's performance
        todayPerformance(null)

        setListener()

        setupObserver()

        setNavigationMenu()
    }

    override fun onResume() {
        super.onResume()
        //dashboard api hit
        setDashboardApi()
    }

    private fun setDashboardApi() {
        showProgressDialog(this, false)
        var request = HomeCountRequest(
            PreferenceManager.getServerDateUtc(),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this)
        )
        mViewModel?.getAllHomeCountStatus(request)
    }

    private fun openFeatureDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        val binding = HomeMainServicesDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //tukawach list
        var tuKawachList = ArrayList<HomeServicesModel>()
        tuKawachList.add(HomeServicesModel("Vehicle Tracking", R.drawable.veh_track, trackingCount))
        tuKawachList.add(
            HomeServicesModel(
                "Vehicle Verification",
                R.drawable.veh_verify,
                verificationCount
            )
        )
        tuKawachList.add(HomeServicesModel("Driving License", R.drawable.dl_verify, dlCount))
        binding.rvMainServicesDialog.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
            adapter = TUKawachDialogAdapter(this@HomeActivity, tuKawachList)
            hasFixedSize()
        }

        //cancel button
        binding.imgCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setListener() {
        binding.addressUpdate.text = address
        binding.addressShimmer.visibility = View.GONE
        binding.addressUpdate.visibility = View.VISIBLE

        //close drawer
        binding.navOpenBtn.setOnClickListener {
            binding.drawerLay.open()
        }

        binding.OnSwitchBtn.setOnCheckedChangeListener { compoundButton, b ->
            dutyStatus = b
//            if (b) {
//                binding.txtOnDuty.text = "On Duty"
//                binding.txtOnDuty.setTextColor(resources.getColor(R.color.on_duty_color))
//                binding.OnSwitchBtn.trackTintList =
//                    resources.getColorStateList(R.color.on_duty_color)
//            } else {
//                binding.txtOnDuty.text = "Off Duty"
//                binding.txtOnDuty.setTextColor(resources.getColor(R.color.red))
//                binding.OnSwitchBtn.trackTintList = resources.getColorStateList(R.color.red)
//            }
            if (!latitude.isNullOrEmpty() && !longitude.isNullOrEmpty() && !address.isNullOrEmpty()) {
                DialogBoxes.onOffDuty(this, dutyStatus, mViewModel, latitude, longitude, address)
            }
        }

        //close drawer
        binding.nn.navBackBtn.setOnClickListener {
            binding.drawerLay.close()
        }

        //edit profile button
        binding.nn.btnEditProfile.setOnClickListener {
            val intent = Intent(this@HomeActivity, EditProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }

        //Logout button
        binding.navLogout.setOnClickListener {
            val logoutBox = LogoutAlertBox(this, this)
            logoutBox.show()
            binding.drawerLay.close()
        }

        binding.llHome.setOnClickListener {
            setSelectionNav()
        }

        binding.llOnboard.setOnClickListener {
            val intent = Intent(this@HomeActivity, NewOnboardingSelection::class.java)
            startActivity(intent)
        }

        binding.llHelp.setOnClickListener {
            val call = HelplineBox(this, "7070327070")
            call.show()
        }

        binding.homeEarnings.totalEarn.setOnClickListener {
            val intent = Intent(this@HomeActivity, MyEarningActivity::class.java)
            startActivity(intent)
        }

        binding.homeEarnings.todayFollowup.setOnClickListener {
            val intent = Intent(this@HomeActivity, FollowUpActivity::class.java)
            startActivity(intent)
        }

        binding.homeEarnings.unassignedTsba.setOnClickListener {
            val intent = Intent(this@HomeActivity, UnAssignedTSBAActivity::class.java)
            startActivity(intent)
        }

        setSelectionNav()
    }

    private fun setSelectionNav() {
        binding.homeCard.setCardBackgroundColor(resources.getColor(R.color.blue))
        binding.ivHome.setColorFilter(resources.getColor(R.color.white))
        //binding.llHome.setBackgroundColor(resources.getColor(R.color.blue));
    }

    /* private fun checkPermissions(permissions: ArrayList<String>) {
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

                                     latitude = location.latitude.toString()
                                     longitude = location.longitude.toString()
                                     address = addresses?.get(0)?.getAddressLine(0).toString()

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
     }*/



    private fun setNavigationMenu() {
        val list = ArrayList<NavItems>()
        list.add(
            NavItems(
                R.drawable.nav_my_business,
                "My Business",
                "To view the details of my onboarded team."
            )
        )
        list.add(NavItems(R.drawable.nav_targets, "Targets", "To view my daily targets."))
        list.add(
            NavItems(
                R.drawable.travel_exp,
                "Travel Expenses",
                "To request for travel expenses."
            )
        )

        binding.listSlidermenu.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, RecyclerView.VERTICAL, false)
            adapter = NavigationMenuItem(this@HomeActivity, list)
            hasFixedSize()
        }
    }

    override fun onItemClick(pos: Int) {

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

            3 -> {
                val intent = Intent(this, AddSmartFuelActivity::class.java)
                startActivity(intent)
            }

            else -> {
                Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClickLogout() {

        val profileDetail = PreferenceManager.getUserData(this)
        val request = LogoutRequest(
            profileDetail?.boUserid?.toInt() ?: 0,
            PreferenceManager.getServerDateUtc(),
            PreferenceManager.getRequestNo().toInt(),
            PreferenceManager.getPhoneNo(this)
        )
        showProgressDialog(this, false)
        mViewModel?.logoutUser(request)

    }

    override fun logout(type: String) {
        if (type == "out") {
            dismissProgressDialog()

            /*if (isMyServiceRunning(LocationService::class.java)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    stopService(Intent(this, LocationService::class.java))
                } else {
                    stopService(Intent(this, LocationService::class.java))

                }
            }*/
            PreferenceManager.removeData(this)
            PreferenceManager.setLogout(false, this)

            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finishAffinity()
        } else {
        }

    }

    override fun logoutError(error: String) {
        dismissProgressDialog()
        LoggerMessage.toastPrint(error, this)
    }

    private fun setupObserver() {
        mViewModel?.resultDutyStatusLD?.observe(this) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                LoadingUtils.hideDialog()

                val abx = AlertBoxDialog(this, responseModel.serverError.toString(), "m")
                abx.show()
            } else {
                LoadingUtils.hideDialog()
                if (responseModel.success != null) {
                    if (responseModel.success.statuscode == 200) {
                        if (dutyStatus == true) {
                            binding.txtOnDuty.text = "On Duty"
                            binding.txtOnDuty.setTextColor(resources.getColor(R.color.on_duty_color))
                            binding.OnSwitchBtn.trackTintList =
                                resources.getColorStateList(R.color.on_duty_color)
                        } else {
                            binding.txtOnDuty.text = "Off Duty"
                            binding.txtOnDuty.setTextColor(resources.getColor(R.color.red))
                            binding.OnSwitchBtn.trackTintList =
                                resources.getColorStateList(R.color.red)
                        }
                    } else {

                    }
                } else {

                }
            }
        }

        mViewModel?.resultAllHomeCountStatusLD?.observe(this) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(this, responseModel.serverError.toString(), "m")
                abx.show()
            } else {
                dismissProgressDialog()
                if (responseModel.success != null) {
                    if (responseModel.success.statuscode == 200) {
                        var serviceCounts: MenuItemsCount? =
                            responseModel.success.homeMenuItems?.menuItemsCount
                        var earningCounts: OtherItemsCount? =
                            responseModel.success.homeMenuItems?.otherItemsCount
                        var todayPerformanceCounts: TodayPerformanceCount? =
                            responseModel.success.homeMenuItems?.todayPerformanceCount
                        var userDetail: UserDetails? =
                            responseModel.success.homeMenuItems?.userDetails

                        //tracking count
                        trackingCount = serviceCounts?.vehicleTracking

                        //verification count
                        verificationCount = serviceCounts?.vehicleVerification

                        //dl count
                        dlCount = serviceCounts?.dl

                        //home title
                        binding.homeTitle.text = userDetail?.msg ?: ""

                        //nav name
                        binding.nn.navUserName.text = userDetail?.name ?: ""

                        //nav mobile
                        binding.nn.navMobileNo.text = userDetail?.mobileNo ?: ""

                        //nav referral code
                        if (!userDetail?.refcode.isNullOrEmpty()) {
                            binding.nn.navReferralCode.text =
                                "Referral Code: " + userDetail?.refcode ?: ""
                        }

                        //nav user image
                        try {
                            Glide.with(this)
                                .load(userDetail?.image)
                                .placeholder(R.drawable.profile)
                                .error(R.drawable.profile)
                                .into(binding.nn.navUserImg)
                        } catch (e: Exception) {
                        }

                        //duty status
                        dutyStatus = userDetail!!.dutyStatus
                        if (dutyStatus == true) {
                            binding.OnSwitchBtn.isChecked = true
                            binding.txtOnDuty.text = "On Duty"
                            binding.txtOnDuty.setTextColor(resources.getColor(R.color.on_duty_color))
                            binding.OnSwitchBtn.trackTintList =
                                resources.getColorStateList(R.color.on_duty_color)
                        } else {
                            binding.OnSwitchBtn.isChecked = false
                            binding.txtOnDuty.text = "Off Duty"
                            binding.txtOnDuty.setTextColor(resources.getColor(R.color.red))
                            binding.OnSwitchBtn.trackTintList =
                                resources.getColorStateList(R.color.red)
                        }

                        //main services
                        mainServices(serviceCounts)

                        //earning counts
                        earningCounts(earningCounts)

                        //today's performance
                        todayPerformance(todayPerformanceCounts)
                    } else {

                    }
                } else {

                }
            }
        }

        mViewModel?.logoutStatusLD?.observe(this) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(this, responseModel.serverError.toString(), "m")
                abx.show()
            } else {
                dismissProgressDialog()
                if (responseModel.success != null) {
                    PreferenceManager.removeData(this)
                    PreferenceManager.setLogout(false, this)

                    val loginIntent = Intent(this, LoginActivity::class.java)
                    startActivity(loginIntent)
                    finishAffinity()
                } else {
                }
            }
        }
    }

    private fun mainServices(serviceCounts: MenuItemsCount?) {
        val serviceList = ArrayList<HomeServicesModel>()
        serviceList.add(
            HomeServicesModel(
                "TU Kawach",
                R.drawable.ic_kawach,
                serviceCounts?.tuKawach
            )
        )
        serviceList.add(
            HomeServicesModel(
                "Finance",
                R.drawable.ic_finanace,
                serviceCounts?.finance
            )
        )
        serviceList.add(
            HomeServicesModel(
                "Insurance",
                R.drawable.ic_insurance,
                serviceCounts?.insurance
            )
        )
        serviceList.add(
            HomeServicesModel(
                "Smart Fuel",
                R.drawable.ic_smart_fuel,
                serviceCounts?.smartFuel
            )
        )
        serviceList.add(HomeServicesModel("GPS", R.drawable.ic_gps, serviceCounts?.gps))
        serviceList.add(HomeServicesModel("FASTag", R.drawable.ic_fasttag, serviceCounts?.gps))
        binding.rvServices.apply {
            binding.rvServices.layoutManager = GridLayoutManager(this@HomeActivity, 3)
            adapter = ServicesMainAdapter(this@HomeActivity, serviceList)
            hasFixedSize()
        }
    }

    private fun earningCounts(earningCounts: OtherItemsCount?) {
        //total earning
        if (earningCounts?.totalEarning.isNullOrEmpty()) {
            binding.homeEarnings.tvTotalEarn.text = "₹ 0"
        } else {
            binding.homeEarnings.tvTotalEarn.text = "₹ " + earningCounts?.totalEarning
        }

        //today followup
        if (earningCounts?.todayFollowUp.isNullOrEmpty()) {
            binding.homeEarnings.tvFollowupCount.text = "0"
        } else {
            binding.homeEarnings.tvFollowupCount.text = earningCounts?.todayFollowUp
        }

        //tsba
        if (earningCounts?.unassignedTSBA.isNullOrEmpty()) {
            binding.homeEarnings.unassignedTsBaCount.text = "0"
        } else {
            binding.homeEarnings.unassignedTsBaCount.text = earningCounts?.unassignedTSBA
        }
    }

    private fun todayPerformance(featureCount: TodayPerformanceCount?) {
        var featuresList = ArrayList<HomeServicesModel>()
        featuresList.add(
            HomeServicesModel(
                "Truck Suppliers",
                R.drawable.truck_img,
                featureCount?.truckSuppliers
            )
        )
        featuresList.add(
            HomeServicesModel(
                "Business Associates",
                R.drawable.ba_ic,
                featureCount?.businessAssociates
            )
        )
        featuresList.add(
            HomeServicesModel(
                "Growth Partners",
                R.drawable.growth_part,
                featureCount?.growthPartners
            )
        )
        featuresList.add(
            HomeServicesModel(
                "Total Add Loads",
                R.drawable.load_feature,
                featureCount?.totalAddLoads
            )
        )
        featuresList.add(
            HomeServicesModel(
                "Total downloads",
                R.drawable.down_feature,
                featureCount?.totalDownloads
            )
        )
        featuresList.add(
            HomeServicesModel(
                "Subscription Plans",
                R.drawable.subscribe_feature,
                featureCount?.subsriptionPlans
            )
        )
        featuresList.add(
            HomeServicesModel(
                "Finance Leads",
                R.drawable.finance_feature,
                featureCount?.financeLeads
            )
        )
        featuresList.add(
            HomeServicesModel(
                "Insurance Leads",
                R.drawable.insure_feature,
                featureCount?.insuranceLeads
            )
        )
        featuresList.add(
            HomeServicesModel(
                "Smart Fuel Leads",
                R.drawable.fuel_feature,
                featureCount?.smartFuelLeads
            )
        )
        binding.rvFeatures.apply {
            layoutManager = GridLayoutManager(this@HomeActivity, 3)
            adapter = HomeFeaturesAdapter(this@HomeActivity, featuresList)
            hasFixedSize()
        }

    }

}