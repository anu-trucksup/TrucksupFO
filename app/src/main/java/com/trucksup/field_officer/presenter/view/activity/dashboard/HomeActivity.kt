package com.trucksup.field_officer.presenter.view.activity.dashboard

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
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
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.DutyStatusRequest
import com.trucksup.field_officer.data.model.HomeServicesModel
import com.trucksup.field_officer.data.model.home.HomeCountRequest
import com.trucksup.field_officer.data.model.home.MenuItemsCount
import com.trucksup.field_officer.data.model.home.OtherItemsCount
import com.trucksup.field_officer.data.model.home.TodayPerformanceCount
import com.trucksup.field_officer.data.model.home.UserDetails
import com.trucksup.field_officer.databinding.ActivityHomeBinding
import com.trucksup.field_officer.databinding.AttendDialogLayoutBinding
import com.trucksup.field_officer.databinding.HomeMainServicesDialogBinding
import com.trucksup.field_officer.databinding.OnOffDutyBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.HelplineBox
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutAlertBox
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutManager
import com.trucksup.field_officer.presenter.view.activity.auth.logout.LogoutRequest
import com.trucksup.field_officer.presenter.view.activity.commit.MyTodayCommitmentActivity
import com.trucksup.field_officer.presenter.view.activity.dashboard.vml.DashBoardViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.FinanceActivity
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.InsuranceActivity
import com.trucksup.field_officer.presenter.view.activity.todayFollowup.FollowUpActivity
import com.trucksup.field_officer.presenter.view.activity.other.NewOnboardingSelection
import com.trucksup.field_officer.presenter.view.activity.other.model.NavItems
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
import java.util.Date

@AndroidEntryPoint
class HomeActivity : BaseActivity(), OnItemClickListener, LogoutManager {
    private lateinit var binding: ActivityHomeBinding
    private var mViewModel: DashBoardViewModel? = null
    private var dutyStatus: Boolean = false
    private var apiDutyStatus: Boolean = false
    private var trackingCount: String? = null
    private var verificationCount: String? = null
    private var dlCount: String? = null
    private var dialog: AlertDialog? = null
    private var dialog2: AlertDialog? = null

    private var backPressedTime: Long = 0
    private lateinit var toast: Toast


    override fun onStart() {
        super.onStart()
//        val permissionList = ArrayList<String>()
//        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
//        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        //checkPermissions(permissionList)
        setLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
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

    private fun setLocation() {
        checkLocationPermission() {
            Log.e("Location", latitude + "@" + longitude)
            binding.addressUpdate.text = address
            Log.e("Address", "address:" + address)
            binding.addressShimmer.visibility = View.GONE
            binding.addressUpdate.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if (dialog != null) {
            dialog?.dismiss()
            dialog = null
        }

        if (dialog2 != null) {
            dialog2?.dismiss()
            dialog2 = null
        }
        //dashboard api hit
        setDashboardApi()
    }

    private fun setDashboardApi() {
        showProgressDialog(this, false)
        val request = HomeCountRequest(
            PreferenceManager.getServerDateUtc(),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this),
            PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0
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
        val tuKawachList = ArrayList<HomeServicesModel>()
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
//        binding.addressUpdate.text = address
//        binding.addressShimmer.visibility = View.GONE
//        binding.addressUpdate.visibility = View.VISIBLE
        binding.tvCommit.setOnClickListener {
            val intent = Intent(this@HomeActivity, MyTodayCommitmentActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        }
        //close drawer
        binding.navOpenBtn.setOnClickListener {
            binding.drawerLay.open()
        }

        //ON OFF duty button
        binding.OnSwitchBtn.setOnCheckedChangeListener { compoundButton, b ->
            if (apiDutyStatus == false) {
                dutyStatus = b
                if (!latitude.isNullOrEmpty() && !longitude.isNullOrEmpty() && !address.isNullOrEmpty()) {
                    apiDutyStatus = true
//                    onOffDuty()
                    attendanceDialog()
                }
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
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
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
        list.add(NavItems(R.drawable.nav_report, "Report", "To view the report of targets."))
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
//            apiDutyStatus = false
            if (responseModel.serverError != null) {
                if (dutyStatus == false) {
                    dutyStatus = true
                    onDutyToggleChange()
                } else {
                    dutyStatus = false
                    offDutyToggleChange()
                }
                LoadingUtils.hideDialog()

                val abx = AlertBoxDialog(this, responseModel.serverError.toString(), "m")
                abx.show()
            } else {
                LoadingUtils.hideDialog()
                if (responseModel.success != null) {
                    if (responseModel.success.statuscode == 200) {
                        if (dutyStatus == true) {
                            onDutyToggleChange()
                        } else {
                            offDutyToggleChange()
                        }
                    } else {
                        if (dutyStatus == false) {
                            dutyStatus = true
                            onDutyToggleChange()
                        } else {
                            dutyStatus = false
                            offDutyToggleChange()
                        }

                    }
                } else {

                    if (dutyStatus == false) {
                        dutyStatus = true
                        onDutyToggleChange()
                    } else {
                        dutyStatus = false
                        offDutyToggleChange()
                    }
                }
            }
            apiDutyStatus = false
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
                        val serviceCounts: MenuItemsCount? =
                            responseModel.success.homeMenuItems?.menuItemsCount
                        val earningCounts: OtherItemsCount? =
                            responseModel.success.homeMenuItems?.otherItemsCount
                        val todayPerformanceCounts: TodayPerformanceCount? =
                            responseModel.success.homeMenuItems?.todayPerformanceCount
                        val userDetail: UserDetails? =
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
                        if (userDetail?.dutyStatus.isNullOrEmpty()) {
                            dutyStatus = false
                        } else {
                            if (userDetail?.dutyStatus?.lowercase() == "true") {
                                dutyStatus = true
                            } else {
                                dutyStatus = false
                            }
                        }


//                        dutyStatus = userDetail!!.dutyStatus?:""
                        apiDutyStatus = true
                        if (dutyStatus == true) {
                            onDutyToggleChange()
                            apiDutyStatus = false
                        } else {
                            dutyStatus = true
                            offDutyToggleChange()
                            if (!latitude.isNullOrEmpty() && !longitude.isNullOrEmpty() && !address.isNullOrEmpty()) {
                                onOffDuty()
                            }
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
                "Total Downloads",
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


    ///////////////////////////////////////////////////////////////
    private fun onOffDuty() {
        if (dialog != null) {
            dialog?.dismiss()
            dialog = null
        }
        val builder = AlertDialog.Builder(this@HomeActivity)
        val binding = OnOffDutyBinding.inflate(LayoutInflater.from(this@HomeActivity))
        builder.setView(binding.root)
        dialog = builder.create()
        dialog?.setCancelable(false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        if (dutyStatus == true) {
            binding.textView6.text = getString(R.string.activate_msg)
        } else {
            binding.textView6.text = getString(R.string.not_activate_msg)
        }

        //activate button
        binding.btnActivate.setOnClickListener {
            attendanceDialog()
            dialog?.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            if (dutyStatus == false) {
                dutyStatus = true
                onDutyToggleChange()
            } else {
                dutyStatus = false
                offDutyToggleChange()
            }
            apiDutyStatus = false
            dialog?.dismiss()
        }

        dialog?.show()
    }

    //////////////////////////////////////////////////////////////
    private fun attendanceDialog() {
        if (dialog2 != null) {
            dialog2?.dismiss()
            dialog2 = null
        }
        val builder = AlertDialog.Builder(this@HomeActivity)
        val binding = AttendDialogLayoutBinding.inflate(LayoutInflater.from(this@HomeActivity))
        builder.setView(binding.root)
        dialog2 = builder.create()
        dialog2?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog2?.setCancelable(false)

        if (dutyStatus == true) {
            binding.message.text = getString(R.string.on_duty_confirm_msg)
        } else {
            binding.message.text = getString(R.string.off_duty_confirm_msg)
        }

        val calendar = Calendar.getInstance()
        val getCurrentDate = SimpleDateFormat("dd-MMM-yy")
        val getCurrentTime = SimpleDateFormat("hh:mm a")
        val currentDate = getCurrentDate.format(calendar.time)
        val currentTime = getCurrentTime.format(Date()).toString()
        val formattedTime = currentTime.replace("am", "AM").replace("pm", "PM");

        binding.tvDate.setText("Date: " + currentDate)
        binding.tvTime.setText("Time: " + formattedTime)

        //ok button
        binding.confirm.setOnClickListener {
            if (latitude.isNullOrEmpty() || longitude.isNullOrEmpty()) {
                checkLocationPermission() {
                    LoadingUtils.showDialog(this@HomeActivity, false)
                    val request = DutyStatusRequest(
                        PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0,
                        PreferenceManager.getUserData(this@HomeActivity)?.boUserid?.toInt() ?: 0,
                        dutyStatus,
                        latitude ?: "",
                        address ?: "",
                        longitude ?: "",
                        "" + PreferenceManager.getServerDateUtc(),
                        PreferenceManager.getRequestNo().toInt(),
                        "" + PreferenceManager.getPhoneNo(this@HomeActivity)
                    )
                    mViewModel?.dutyStatus(request)
                }
            } else {
                LoadingUtils.showDialog(this@HomeActivity, false)
                val request = DutyStatusRequest(
                    PreferenceManager.getUserData(this)?.boUserid?.toInt() ?: 0,
                    PreferenceManager.getUserData(this@HomeActivity)?.boUserid?.toInt() ?: 0,
                    dutyStatus,
                    latitude ?: "",
                    address ?: "",
                    longitude ?: "",
                    "" + PreferenceManager.getServerDateUtc(),
                    PreferenceManager.getRequestNo().toInt(),
                    "" + PreferenceManager.getPhoneNo(this@HomeActivity)
                )
                mViewModel?.dutyStatus(request)
            }
            dialog2?.dismiss()
        }

        //ok button
        binding.cancle.setOnClickListener {
            if (dutyStatus == false) {
                dutyStatus = true
                onDutyToggleChange()
            } else {
                dutyStatus = false
                offDutyToggleChange()  }
            apiDutyStatus = false
            dialog2?.dismiss()
        }

        dialog2?.show()
    }

    private fun onDutyToggleChange() {
        this.binding.OnSwitchBtn.isChecked = true
        this.binding.txtOnDuty.text = "On Duty"
        this.binding.txtOnDuty.setTextColor(resources.getColor(R.color.on_duty_color))
        this.binding.OnSwitchBtn.trackTintList = resources.getColorStateList(R.color.on_duty_color)
    }

    private fun offDutyToggleChange() {
        this.binding.OnSwitchBtn.isChecked = false
        this.binding.txtOnDuty.text = "Off Duty"
        this.binding.txtOnDuty.setTextColor(resources.getColor(R.color.red))
        this.binding.OnSwitchBtn.trackTintList = resources.getColorStateList(R.color.red)
    }


    override fun onBackPressed() {
        // super.onBackPressed()

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            toast.cancel()
            super.onBackPressed()
        } else {
            toast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT)
            toast.show()
        }
        backPressedTime = System.currentTimeMillis()
    }

}