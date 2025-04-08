package com.trucksup.field_officer.presenter.view.activity.financeInsurance

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.VehicleDetail
import com.trucksup.field_officer.databinding.ActivityInsuranceScreenBinding
import com.trucksup.field_officer.databinding.VehicleDetailsDialogLayoutBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.common.dialog.FinaceSubmitBox
import com.trucksup.field_officer.presenter.common.image_picker.GetImage
import com.trucksup.field_officer.presenter.common.image_picker.ImagePickerDailog
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.utils.truckMenu.TruckMenu
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InsuranceViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryRequest
import com.trucksup.field_officer.presenter.view.activity.other.ViewPdfScreen
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.Locale
import java.util.regex.Pattern

@AndroidEntryPoint
class InsuranceActivity : BaseActivity(), InsuranceController, GetImage, TrucksFOImageController {
    private lateinit var binding: ActivityInsuranceScreenBinding
    private var list = ArrayList<VehicleDetail>()
    private var mViewModel: InsuranceViewModel? = null
    private var adapter: InsuranceListAdapter? = null
    private val calendar = Calendar.getInstance()

    private var insuFor: String = "other"
    private var rcFrontImgKey: String? = ""
    private var rcFrontImgUrl: String? = ""
    private var rcBackImgKey: String? = ""
    private var rcBackImgUrl: String? = ""
    private var prevPolicyDocsImgKey: String? = ""
    private var prevPolicyDocsImgUrl: String? = ""
    private var imageT: Int = 0//0 default,1 front image,2 back image,3 previous policy docs image
    private var sourceValue: String? = "Trucksup"

    private var launcher: ActivityResultLauncher<Intent>? = null
    private var imageUri:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insurance_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mViewModel = ViewModelProvider(this)[InsuranceViewModel::class.java]
        PreferenceManager.setPhoneNo("8527257632", this)
        binding.etFullName.setText("Anupam")

        binding.etMobile.setText(PreferenceManager.getPhoneNo(this))

        //referral code or sales code
        // binding.etReferralCode.setText(PreferenceManager.getUserData(this)?.salesCode)
        binding.etReferralCode.setText("7BGHJ9")
        disableEmojiInTitle()
        setListener()
        setupObserver()
        cameraActivityresult()
    }

    private fun setRecyclerView() {
        adapter = InsuranceListAdapter(this, list, this).apply {
            setOnControllerListener2(object : InsuranceListAdapter.ControllerListener2 {
                override fun onDeleteTruck(position: Int) {
                    list.removeAt(position)
                    setRecyclerView()
                }

                override fun onShowTruckDetails(data: VehicleDetail) {
                    viewVehicleDetails(data)
                }
            })
        }
        binding.rvInsList.layoutManager =
            LinearLayoutManager(this@InsuranceActivity, RecyclerView.VERTICAL, false)
        binding.rvInsList.adapter = adapter
    }


    private fun setupObserver() {
        mViewModel?.resultsubmitInsuranceLD?.observe(this@InsuranceActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@InsuranceActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.message != null) {
                    val abx = FinaceSubmitBox(
                        this, responseModel.success.message,
                        responseModel.success.message1, "cl"
                    )
                    abx.show()

                } else {

                }
            }
        }

        mViewModel?.imgUploadResultLD?.observe(this@InsuranceActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    MyAlartBox(
                        this@InsuranceActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.imagekey != null) {


                } else {

                }
            }
        }
    }

    private var activitypdfLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {

                val orFile: File = FileHelp().getFile(this, result.data?.data)!!

                uploadImage(orFile, "")

            }
        }

    private fun setListener() {
        //rc front camera
        binding.imgFrontCamera.setOnClickListener {
            if (rcFrontImgKey.isNullOrEmpty()) {
                imageT = 1
                getImage()
            }
        }

        //rc back camera
        binding.imgBackCamera.setOnClickListener {
            if (rcBackImgKey.isNullOrEmpty()) {
                imageT = 2
                getImage()
            }
        }

        //previous policy
        binding.imgPrevPolicyDoc.setOnClickListener {
            if (prevPolicyDocsImgKey.isNullOrEmpty()) {
                imageT = 3
                getImage()
            }
        }

        //plus button
        binding.btnAddImage.setOnClickListener {
            if (binding.vehicleDetailsCard.visibility == View.GONE) {
                binding.vehicleDetailsCard.visibility = View.VISIBLE
                binding.btnAddImage.visibility = View.GONE
            }

            binding.etVehicleNo.getText().clear()
            binding.etInsValidity.text = ""

            //clear rc front image
            binding.cutFrontBtn.visibility = View.GONE
            binding.imgFrontCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcFrontImgKey = ""
            rcFrontImgUrl = ""

            //clear rc back image
            binding.cutBackBtn.visibility = View.GONE
            binding.imgBackCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcBackImgKey = ""
            rcBackImgUrl = ""

            //clear previous policy docs image image
            binding.cutPrevPolicyBtn.visibility = View.GONE
            binding.imgPrevPolicyDoc.setImageDrawable(getDrawable(R.drawable.camera_new))
            prevPolicyDocsImgKey = ""
            prevPolicyDocsImgUrl = ""
        }

        //cut button
        binding.btnCut.setOnClickListener {

            binding.vehicleDetailsCard.visibility = View.GONE
            binding.btnAddImage.visibility = View.VISIBLE
            binding.etVehicleNo.getText().clear()
            binding.etInsValidity.text = ""

            //clear rc front image
            binding.cutFrontBtn.visibility = View.GONE
            binding.imgFrontCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcFrontImgKey = ""
            rcFrontImgUrl = ""

            //clear rc back image
            binding.cutBackBtn.visibility = View.GONE
            binding.imgBackCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcBackImgKey = ""
            rcBackImgUrl = ""

            //clear previous policy docs image image
            binding.cutPrevPolicyBtn.visibility = View.GONE
            binding.imgPrevPolicyDoc.setImageDrawable(getDrawable(R.drawable.camera_new))
            prevPolicyDocsImgKey = ""
            prevPolicyDocsImgUrl = ""
        }

        //cut front
        binding.cutFrontBtn.setOnClickListener {
            //clear rc front image
            binding.cutFrontBtn.visibility = View.GONE
            binding.imgFrontCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcFrontImgKey = ""
            rcFrontImgUrl = ""
        }

        //cut back
        binding.cutBackBtn.setOnClickListener {
            //clear rc back image
            binding.cutBackBtn.visibility = View.GONE
            binding.imgBackCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcBackImgKey = ""
            rcBackImgUrl = ""
        }

        //cut previous policy docs
        binding.cutPrevPolicyBtn.setOnClickListener {
            //clear previous policy docs image image
            binding.cutPrevPolicyBtn.visibility = View.GONE
            binding.imgPrevPolicyDoc.setImageDrawable(getDrawable(R.drawable.camera_new))
            prevPolicyDocsImgKey = ""
            prevPolicyDocsImgUrl = ""
        }

        //add button
        binding.btnAdd.setOnClickListener {
            if (TextUtils.isEmpty(binding.etVehicleNo.getText().toString())) {
                LoggerMessage.onSNACK(
                    binding.etVehicleNo,
                    resources.getString(R.string.enterCommercialVehical),
                    this
                )
            } else if (!checkVehicleNumber(binding.etVehicleNo.text.toString())) {
                LoggerMessage.onSNACK(
                    binding.etVehicleNo,
                    resources.getString(R.string.enterRightCommercialVehical),
                    this
                )
            } else if (binding.etInsValidity.text.toString().isNullOrEmpty()) {
                LoggerMessage.onSNACK(
                    binding.etInsValidity,
                    resources.getString(R.string.insurance_validity_error),
                    this
                )
            }
//            else if (rcFrontImgKey.isNullOrEmpty())
//            {
//                LoggerMessage.onSNACK(
//                    binding.imgFrontCamera,
//                    resources.getString(R.string.rc_front_image_error),
//                    this
//                )
//            }
//            else if (rcBackImgKey.isNullOrEmpty())
//            {
//                LoggerMessage.onSNACK(
//                    binding.imgBackCamera,
//                    resources.getString(R.string.rc_back_image_error),
//                    this
//                )
//            }
            else {
                addListItem()
            }
        }

        //self button
        binding.btnSelf.setOnClickListener {
            //self
            insuFor = "self"
            binding.btnSelf.apply {
                setStrokeColor(resources.getColor(R.color.blue))
                setCardBackgroundColor(resources.getColor(R.color.blue))
            }
            binding.tvSelf.setTextColor(resources.getColor(R.color.white))

            //other
            binding.btnOther.apply {
                setStrokeColor(resources.getColor(R.color.border_color))
                setCardBackgroundColor(resources.getColor(R.color.chipColor))
            }
            binding.tvOther.setTextColor(resources.getColor(R.color.secondry_text))

            //clear full name
            // binding.etFullName.setText(PreferenceManager.getUserData(this)?.profileName)
            binding.etMobile.setText(PreferenceManager.getPhoneNo(this))
            // binding.etReferralCode.setText(PreferenceManager.getUserData(this)?.salesCode)

            binding.etInsValidity.text = ""
            binding.etFullName.isFocusable = false
            binding.etFullName.isClickable = false  // optional, to disable click events
            binding.etFullName.isFocusableInTouchMode = false
            binding.etFullName.isCursorVisible = false  // optional, hide the cursor
            binding.etFullName.keyListener = null

            //referral code or sales code
            binding.etReferralCode.isFocusable = false
            binding.etReferralCode.isClickable = false  // optional, to disable click events
            binding.etReferralCode.isFocusableInTouchMode = false
            binding.etReferralCode.isCursorVisible = false  // optional, hide the cursor
//            binding.etReferralCode.keyListener = null

            //clear rc front image
            binding.cutFrontBtn.visibility = View.GONE
            binding.imgFrontCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcFrontImgKey = ""
            rcFrontImgUrl = ""

            //clear rc back image
            binding.cutBackBtn.visibility = View.GONE
            binding.imgBackCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcBackImgKey = ""
            rcBackImgUrl = ""

            //clear previous policy docs image image
            binding.cutPrevPolicyBtn.visibility = View.GONE
            binding.imgPrevPolicyDoc.setImageDrawable(getDrawable(R.drawable.camera_new))
            prevPolicyDocsImgKey = ""
            prevPolicyDocsImgUrl = ""

            list.clear()
            setRecyclerView()
        }

        //other button
        binding.btnOther.setOnClickListener {
            //other
            insuFor = "other"
            binding.btnOther.apply {
                setStrokeColor(resources.getColor(R.color.blue))
                setCardBackgroundColor(resources.getColor(R.color.blue))
            }
            binding.tvOther.setTextColor(resources.getColor(R.color.white))

            //self
            binding.btnSelf.apply {
                setStrokeColor(resources.getColor(R.color.border_color))
                setCardBackgroundColor(resources.getColor(R.color.chipColor))
            }
            binding.tvSelf.setTextColor(resources.getColor(R.color.secondry_text))

            //clear full name
            binding.etFullName.getText().clear()
            //clear mobile
            binding.etMobile.getText().clear()
            //clear vehicle no.
            binding.etVehicleNo.getText().clear()
            //clear Insurance Validity
            binding.etInsValidity.text = ""
            //clear referral code
            binding.etReferralCode.getText().clear()

            binding.etFullName.isFocusable = true
            binding.etFullName.isClickable = true  // optional, to re-enable click events
            binding.etFullName.isFocusableInTouchMode = true
            binding.etFullName.isCursorVisible = true  // optional, show the cursor
            binding.etFullName.keyListener = EditText(this).keyListener

            //referral code or sales code
            binding.etReferralCode.isFocusable = true
            binding.etReferralCode.isClickable = true  // optional, to re-enable click events
            binding.etReferralCode.isFocusableInTouchMode = true
            binding.etReferralCode.isCursorVisible = true  // optional, show the cursor
//            binding.etReferralCode.keyListener = EditText(this).keyListener

            //clear rc front image
            binding.cutFrontBtn.visibility = View.GONE
            binding.imgFrontCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcFrontImgKey = ""
            rcFrontImgUrl = ""

            //clear rc back image
            binding.cutBackBtn.visibility = View.GONE
            binding.imgBackCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcBackImgKey = ""
            rcBackImgUrl = ""

            //clear previous policy docs image image
            binding.cutPrevPolicyBtn.visibility = View.GONE
            binding.imgPrevPolicyDoc.setImageDrawable(getDrawable(R.drawable.camera_new))
            prevPolicyDocsImgKey = ""
            prevPolicyDocsImgUrl = ""

            list.clear()
            setRecyclerView()
        }

        //insurance validity button
        binding.etInsValidity.setOnClickListener {
            showDatePicker()
        }

        //submit button
        binding.btnSumbit.setOnClickListener {
            if (list.size == 0 && binding.vehicleDetailsCard.visibility == View.GONE) {
                binding.vehicleDetailsCard.visibility = View.VISIBLE
                binding.btnAddImage.visibility = View.GONE
            }

            if (checkValidation()) {

                if (!TextUtils.isEmpty(binding.etVehicleNo.text) && list.size == 0) {
                    if (!checkVehicleNumber(binding.etVehicleNo.text.toString())) {
                        LoggerMessage.onSNACK(
                            binding.etVehicleNo,
                            resources.getString(R.string.enterRightCommercialVehical),
                            this
                        )

                    } else if (binding.etInsValidity.text.toString().isNullOrEmpty()) {
                        LoggerMessage.onSNACK(
                            binding.etInsValidity,
                            resources.getString(R.string.insurance_validity_error),
                            this
                        )
                    }
//                    else if (rcFrontImgKey.isNullOrEmpty())
//                    {
//                        LoggerMessage.onSNACK(
//                            binding.imgFrontCamera,
//                            resources.getString(R.string.rc_front_image_error),
//                            this
//                        )
//                    }
//                    else if (rcBackImgKey.isNullOrEmpty())
//                    {
//                        LoggerMessage.onSNACK(
//                            binding.imgBackCamera,
//                            resources.getString(R.string.rc_back_image_error),
//                            this
//                        )
//                    }
                    else {
                        list.add(
                            VehicleDetail(
                                binding.etInsValidity.text.toString(),
                                binding.etVehicleNo.text.toString(),
                                rcFrontImgKey ?: "",
                                rcBackImgKey ?: "",
                                prevPolicyDocsImgKey ?: "",
                                rcFrontImgUrl ?: "",
                                rcBackImgUrl ?: "",
                                prevPolicyDocsImgUrl ?: ""
                            )
                        )

                        var requestData = SubmitInsuranceInquiryRequest(
                            PreferenceManager.getRequestNo(),
                            PreferenceManager.getServerDateUtc(""),
                            PreferenceManager.getPhoneNo(this),
                            binding.etFullName.text.toString(),
                            PreferenceManager.getProfileType(this).toString(),
                            binding.etMobile.getText().toString(),
                            "commercial",
                            "NA",
                            PreferenceManager.getPhoneNo(this),
                            insuFor,
                            PreferenceManager.getPhoneNo(this),
                            binding.etReferralCode.getText().toString(),
                            list,
                            sourceValue ?: ""
                        )
                        showProgressDialog(this, false)

                        mViewModel?.submitInsuranceData(requestData)
                    }

                } else if (!binding.etInsValidity.text.isNullOrEmpty() && list.size == 0) {
                    if (!TextUtils.isEmpty(binding.etVehicleNo.text)) {
                        if (!checkVehicleNumber(binding.etVehicleNo.text.toString())) {
                            LoggerMessage.onSNACK(
                                binding.etVehicleNo,
                                resources.getString(R.string.enterRightCommercialVehical),
                                this
                            )

                        }
//                        else if (rcFrontImgKey.isNullOrEmpty())
//                        {
//                            LoggerMessage.onSNACK(
//                                binding.imgFrontCamera,
//                                resources.getString(R.string.rc_front_image_error),
//                                this
//                            )
//                        }
//                        else if (rcBackImgKey.isNullOrEmpty())
//                        {
//                            LoggerMessage.onSNACK(
//                                binding.imgBackCamera,
//                                resources.getString(R.string.rc_back_image_error),
//                                this
//                            )
//                        }
                        else {
                            list.add(
                                VehicleDetail(
                                    binding.etInsValidity.text.toString(),
                                    binding.etVehicleNo.text.toString(),
                                    rcFrontImgKey ?: "",
                                    rcBackImgKey ?: "",
                                    prevPolicyDocsImgKey ?: "",
                                    rcFrontImgUrl ?: "",
                                    rcBackImgUrl ?: "",
                                    prevPolicyDocsImgUrl ?: ""
                                )
                            )

                            var requestData = SubmitInsuranceInquiryRequest(
                                PreferenceManager.getRequestNo(),
                                PreferenceManager.getServerDateUtc(""),
                                PreferenceManager.getPhoneNo(this),
                                binding.etFullName.text.toString(),
                                PreferenceManager.getProfileType(this).toString(),
                                binding.etMobile.getText().toString(),
                                "commercial",
                                "NA",
                                PreferenceManager.getPhoneNo(this),
                                insuFor,
                                PreferenceManager.getPhoneNo(this),
                                binding.etReferralCode.getText().toString(),
                                list,
                                sourceValue ?: ""
                            )
                            showProgressDialog(this, false)

                            mViewModel?.submitInsuranceData(requestData)
                        }
                    } else {
                        LoggerMessage.onSNACK(
                            binding.etVehicleNo,
                            resources.getString(R.string.enterCommercialVehical),
                            this
                        )
                    }
                }
//                else if (!rcFrontImgKey.isNullOrEmpty() && list.size==0)
//                {
//                    if (!TextUtils.isEmpty(binding.etVehicleNo.text)) {
//                        if (!checkVehicleNumber(binding.etVehicleNo.text.toString())) {
//                            LoggerMessage.onSNACK(
//                                binding.etVehicleNo,
//                                resources.getString(R.string.enterRightCommercialVehical),
//                                this
//                            )
//
//                        }
//                        else if (binding.etInsValidity.text.toString().isNullOrEmpty()) {
//                            LoggerMessage.onSNACK(
//                                binding.etInsValidity,
//                                resources.getString(R.string.insurance_validity_error),
//                                this
//                            )
//                        }
//                        else if (rcBackImgKey.isNullOrEmpty())
//                        {
//                            LoggerMessage.onSNACK(
//                                binding.imgBackCamera,
//                                resources.getString(R.string.rc_back_image_error),
//                                this
//                            )
//                        }
//                        else {
//                            list.add(
//                                VehicleDetail(
//                                    binding.etInsValidity.text.toString(),
//                                    binding.etVehicleNo.text.toString(),
//                                    rcFrontImgKey?:"",
//                                    rcBackImgKey?:"",
//                                    prevPolicyDocsImgKey?:""
//
//                                )
//                            )
//
//                            var requestData = SubmitInsuranceInquiryRequest(
//                                PreferenceManager.getRequestNo(),
//                                PreferenceManager.getServerDateUtc(""),
//                                PreferenceManager.getPhoneNo(this),
//                                binding.etFullName.text.toString(),
//                                PreferenceManager.getProfileType(this).toString(),
//                                binding.etMobile.getText().toString(),
//                                "commercial",
//                                "NA",
//                                PreferenceManager.getPhoneNo(this),
//                                insuFor,
//                                PreferenceManager.getPhoneNo(this),
//                                binding.etReferralCode.getText().toString(),
//                                list
//                            )
//                            progressDailogBox?.show()
//
//                            MyResponse().submitInsuranceData(requestData, this, this)
//                        }
//                    }
//                    else
//                    {
//                        LoggerMessage.onSNACK(
//                            binding.etVehicleNo,
//                            resources.getString(R.string.enterCommercialVehical),
//                            this
//                        )
//                    }
//                }
//                else if (!rcBackImgKey.isNullOrEmpty() && list.size==0)
//                {
//                    if (!TextUtils.isEmpty(binding.etVehicleNo.text))
//                    {
//                        if (!checkVehicleNumber(binding.etVehicleNo.text.toString())) {
//                            LoggerMessage.onSNACK(
//                                binding.etVehicleNo,
//                                resources.getString(R.string.enterRightCommercialVehical),
//                                this
//                            )
//
//                        }
//                        else if (binding.etInsValidity.text.toString().isNullOrEmpty()) {
//                            LoggerMessage.onSNACK(
//                                binding.etInsValidity,
//                                resources.getString(R.string.insurance_validity_error),
//                                this
//                            )
//                        }
//                        else if (rcFrontImgKey.isNullOrEmpty())
//                        {
//                            LoggerMessage.onSNACK(
//                                binding.imgFrontCamera,
//                                resources.getString(R.string.rc_front_image_error),
//                                this
//                            )
//                        }
//                        else {
//                            list.add(
//                                VehicleDetail(
//                                    binding.etInsValidity.text.toString(),
//                                    binding.etVehicleNo.text.toString(),
//                                    rcFrontImgKey?:"",
//                                    rcBackImgKey?:"",
//                                    prevPolicyDocsImgKey?:""
//
//                                )
//                            )
//
//                            var requestData = SubmitInsuranceInquiryRequest(
//                                PreferenceManager.getRequestNo(),
//                                PreferenceManager.getServerDateUtc(""),
//                                PreferenceManager.getPhoneNo(this),
//                                binding.etFullName.text.toString(),
//                                PreferenceManager.getProfileType(this).toString(),
//                                binding.etMobile.getText().toString(),
//                                "commercial",
//                                "NA",
//                                PreferenceManager.getPhoneNo(this),
//                                insuFor,
//                                PreferenceManager.getPhoneNo(this),
//                                binding.etReferralCode.getText().toString(),
//                                list
//                            )
//                            progressDailogBox?.show()
//
//                            MyResponse().submitInsuranceData(requestData, this, this)
//                        }
//                    }
//                    else
//                    {
//                        LoggerMessage.onSNACK(
//                            binding.etVehicleNo,
//                            resources.getString(R.string.enterCommercialVehical),
//                            this
//                        )
//                    }
//                }
                else {
                    if (!TextUtils.isEmpty(binding.etVehicleNo.text)) {
                        if (!checkVehicleNumber(binding.etVehicleNo.text.toString())) {
                            LoggerMessage.onSNACK(
                                binding.etVehicleNo,
                                resources.getString(R.string.enterRightCommercialVehical),
                                this
                            )

                        } else if (binding.etInsValidity.text.toString().isNullOrEmpty()) {
                            LoggerMessage.onSNACK(
                                binding.etInsValidity,
                                resources.getString(R.string.insurance_validity_error),
                                this
                            )
                        }
//                        else if (rcFrontImgKey.isNullOrEmpty())
//                        {
//                            LoggerMessage.onSNACK(
//                                binding.imgFrontCamera,
//                                resources.getString(R.string.rc_front_image_error),
//                                this
//                            )
//                        }
//                        else if (rcBackImgKey.isNullOrEmpty())
//                        {
//                            LoggerMessage.onSNACK(
//                                binding.imgBackCamera,
//                                resources.getString(R.string.rc_back_image_error),
//                                this
//                            )
//                        }
                        else {
                            list.add(
                                VehicleDetail(
                                    binding.etInsValidity.text.toString(),
                                    binding.etVehicleNo.text.toString(),
                                    rcFrontImgKey ?: "",
                                    rcBackImgKey ?: "",
                                    prevPolicyDocsImgKey ?: "",
                                    rcFrontImgUrl ?: "",
                                    rcBackImgUrl ?: "",
                                    prevPolicyDocsImgUrl ?: ""
                                )
                            )
                            var requestData = SubmitInsuranceInquiryRequest(
                                PreferenceManager.getRequestNo(),
                                PreferenceManager.getServerDateUtc(""),
                                PreferenceManager.getPhoneNo(this),
                                binding.etFullName.text.toString(),
                                PreferenceManager.getProfileType(this).toString(),
                                binding.etMobile.getText().toString(),
                                "commercial",
                                "NA",
                                PreferenceManager.getPhoneNo(this),
                                insuFor,
                                PreferenceManager.getPhoneNo(this),
                                binding.etReferralCode.getText().toString(),
                                list,
                                sourceValue ?: ""
                            )
                            showProgressDialog(this, false)

                            mViewModel?.submitInsuranceData(requestData)
                        }
                    } else if (!binding.etInsValidity.text.isNullOrEmpty()) {
                        if (!TextUtils.isEmpty(binding.etVehicleNo.text)) {
                            if (!checkVehicleNumber(binding.etVehicleNo.text.toString())) {
                                LoggerMessage.onSNACK(
                                    binding.etVehicleNo,
                                    resources.getString(R.string.enterRightCommercialVehical),
                                    this
                                )

                            }
//                            else if (rcFrontImgKey.isNullOrEmpty())
//                            {
//                                LoggerMessage.onSNACK(
//                                    binding.imgFrontCamera,
//                                    resources.getString(R.string.rc_front_image_error),
//                                    this
//                                )
//                            }
//                            else if (rcBackImgKey.isNullOrEmpty())
//                            {
//                                LoggerMessage.onSNACK(
//                                    binding.imgBackCamera,
//                                    resources.getString(R.string.rc_back_image_error),
//                                    this
//                                )
//                            }
                            else {
                                list.add(
                                    VehicleDetail(
                                        binding.etInsValidity.text.toString(),
                                        binding.etVehicleNo.text.toString(),
                                        rcFrontImgKey ?: "",
                                        rcBackImgKey ?: "",
                                        prevPolicyDocsImgKey ?: "",
                                        rcFrontImgUrl ?: "",
                                        rcBackImgUrl ?: "",
                                        prevPolicyDocsImgUrl ?: ""
                                    )
                                )

                                var requestData = SubmitInsuranceInquiryRequest(
                                    PreferenceManager.getRequestNo(),
                                    PreferenceManager.getServerDateUtc(""),
                                    PreferenceManager.getPhoneNo(this),
                                    binding.etFullName.text.toString(),
                                    PreferenceManager.getProfileType(this).toString(),
                                    binding.etMobile.getText().toString(),
                                    "commercial",
                                    "NA",
                                    PreferenceManager.getPhoneNo(this),
                                    insuFor,
                                    PreferenceManager.getPhoneNo(this),
                                    binding.etReferralCode.getText().toString(),
                                    list,
                                    sourceValue ?: ""
                                )
                                showProgressDialog(this, false)

                                mViewModel?.submitInsuranceData(requestData)
                            }
                        } else {
                            LoggerMessage.onSNACK(
                                binding.etVehicleNo,
                                resources.getString(R.string.enterCommercialVehical),
                                this
                            )
                        }
                    }
//                    else if (!rcFrontImgKey.isNullOrEmpty())
//                    {
//                        if (!TextUtils.isEmpty(binding.etVehicleNo.text)) {
//                            if (!checkVehicleNumber(binding.etVehicleNo.text.toString())) {
//                                LoggerMessage.onSNACK(
//                                    binding.etVehicleNo,
//                                    resources.getString(R.string.enterRightCommercialVehical),
//                                    this
//                                )
//
//                            }
//                            else if (binding.etInsValidity.text.toString().isNullOrEmpty()) {
//                                LoggerMessage.onSNACK(
//                                    binding.etInsValidity,
//                                    resources.getString(R.string.insurance_validity_error),
//                                    this
//                                )
//                            }
//                            else if (rcBackImgKey.isNullOrEmpty())
//                            {
//                                LoggerMessage.onSNACK(
//                                    binding.imgBackCamera,
//                                    resources.getString(R.string.rc_back_image_error),
//                                    this
//                                )
//                            }
//                            else {
//                                list.add(
//                                    VehicleDetail(
//                                        binding.etInsValidity.text.toString(),
//                                        binding.etVehicleNo.text.toString(),
//                                        rcFrontImgKey?:"",
//                                        rcBackImgKey?:"",
//                                        prevPolicyDocsImgKey?:""
//
//                                    )
//                                )
//
//                                var requestData = SubmitInsuranceInquiryRequest(
//                                    PreferenceManager.getRequestNo(),
//                                    PreferenceManager.getServerDateUtc(""),
//                                    PreferenceManager.getPhoneNo(this),
//                                    binding.etFullName.text.toString(),
//                                    PreferenceManager.getProfileType(this).toString(),
//                                    binding.etMobile.getText().toString(),
//                                    "commercial",
//                                    "NA",
//                                    PreferenceManager.getPhoneNo(this),
//                                    insuFor,
//                                    PreferenceManager.getPhoneNo(this),
//                                    binding.etReferralCode.getText().toString(),
//                                    list
//                                )
//                                progressDailogBox?.show()
//
//                                MyResponse().submitInsuranceData(requestData, this, this)
//                            }
//                        }
//                        else
//                        {
//                            LoggerMessage.onSNACK(
//                                binding.etVehicleNo,
//                                resources.getString(R.string.enterCommercialVehical),
//                                this
//                            )
//                        }
//                    }
//                    else if (!rcBackImgKey.isNullOrEmpty())
//                    {
//                        if (!TextUtils.isEmpty(binding.etVehicleNo.text))
//                        {
//                            if (!checkVehicleNumber(binding.etVehicleNo.text.toString())) {
//                                LoggerMessage.onSNACK(
//                                    binding.etVehicleNo,
//                                    resources.getString(R.string.enterRightCommercialVehical),
//                                    this
//                                )
//
//                            }
//                            else if (binding.etInsValidity.text.toString().isNullOrEmpty()) {
//                                LoggerMessage.onSNACK(
//                                    binding.etInsValidity,
//                                    resources.getString(R.string.insurance_validity_error),
//                                    this
//                                )
//                            }
//                            else if (rcFrontImgKey.isNullOrEmpty())
//                            {
//                                LoggerMessage.onSNACK(
//                                    binding.imgFrontCamera,
//                                    resources.getString(R.string.rc_front_image_error),
//                                    this
//                                )
//                            }
//                            else {
//                                list.add(
//                                    VehicleDetail(
//                                        binding.etInsValidity.text.toString(),
//                                        binding.etVehicleNo.text.toString(),
//                                        rcFrontImgKey?:"",
//                                        rcBackImgKey?:"",
//                                        prevPolicyDocsImgKey?:""
//
//                                    )
//                                )
//
//                                var requestData = SubmitInsuranceInquiryRequest(
//                                    PreferenceManager.getRequestNo(),
//                                    PreferenceManager.getServerDateUtc(""),
//                                    PreferenceManager.getPhoneNo(this),
//                                    binding.etFullName.text.toString(),
//                                    PreferenceManager.getProfileType(this).toString(),
//                                    binding.etMobile.getText().toString(),
//                                    "commercial",
//                                    "NA",
//                                    PreferenceManager.getPhoneNo(this),
//                                    insuFor,
//                                    PreferenceManager.getPhoneNo(this),
//                                    binding.etReferralCode.getText().toString(),
//                                    list
//                                )
//                                progressDailogBox?.show()
//
//                                MyResponse().submitInsuranceData(requestData, this, this)
//                            }
//                        }
//                        else
//                        {
//                            LoggerMessage.onSNACK(
//                                binding.etVehicleNo,
//                                resources.getString(R.string.enterCommercialVehical),
//                                this
//                            )
//                        }
//                    }
                    else {
                        var requestData = SubmitInsuranceInquiryRequest(
                            PreferenceManager.getRequestNo(),
                            PreferenceManager.getServerDateUtc(""),
                            PreferenceManager.getPhoneNo(this),
                            binding.etFullName.text.toString(),
                            PreferenceManager.getProfileType(this).toString(),
                            binding.etMobile.getText().toString(),
                            "commercial",
                            "NA",
                            PreferenceManager.getPhoneNo(this),
                            insuFor,
                            PreferenceManager.getPhoneNo(this),
                            binding.etReferralCode.getText().toString(),
                            list,
                            sourceValue ?: ""
                        )
                        showProgressDialog(this, false)

                        mViewModel?.submitInsuranceData(requestData)
                    }
                }

            }
        }
    }

    private fun checkValidation(): Boolean {
        if (TextUtils.isEmpty(binding.etFullName.text)) {
            LoggerMessage.onSNACK(
                binding.etFullName,
                resources.getString(R.string.enterYourName),
                this
            )
            return false
        }

        if (insuFor == "other") {
            if (getSpecialCharacterCount(binding.etFullName?.text.toString()) == 0) {
                LoggerMessage.onSNACK(
                    binding.etFullName,
                    resources.getString(R.string.enterYourrightName),
                    this
                )
                return false
            }
        }
        if (TextUtils.isEmpty(binding.etMobile.text)) {
            LoggerMessage.onSNACK(
                binding.etMobile,
                resources.getString(R.string.enter_mobile_no),
                this
            )
            return false
        }

        if (binding.etMobile.text.length < 10) {
            LoggerMessage.onSNACK(
                binding.etMobile,
                resources.getString(R.string.enter_right_number_v),
                this
            )
            return false
        }

        if (!isValidPhoneNumber(binding.etMobile.text.toString())) {
            LoggerMessage.onSNACK(
                binding.etMobile,
                resources.getString(R.string.enter_right_number_v),
                this
            )
            return false
        }

        if (TextUtils.isEmpty(binding.etVehicleNo.text) && list.size == 0) {
            LoggerMessage.onSNACK(
                binding.etVehicleNo,
                resources.getString(R.string.enterCommercialVehical),
                this
            )
            return false
        }

        if (TextUtils.isEmpty(binding.etInsValidity.text) && list.size == 0) {
            LoggerMessage.onSNACK(
                binding.etInsValidity,
                resources.getString(R.string.insurance_validity_error),
                this
            )
            return false
        }

//        if (rcFrontImgKey.isNullOrEmpty() && list.size==0)
//        {
//            LoggerMessage.onSNACK(
//                binding.imgFrontCamera,
//                resources.getString(R.string.rc_front_image_error),
//                this
//            )
//            return false
//        }
//
//        if (rcBackImgKey.isNullOrEmpty() && list.size==0)
//        {
//            LoggerMessage.onSNACK(
//                binding.imgBackCamera,
//                resources.getString(R.string.rc_back_image_error),
//                this
//            )
//            return false
//        }

        return true
    }

    private fun checkVehicleNumber(vehicleNumber: String): Boolean {
        val regex = "^[A-Z]{2}[ -]?[0-9|A-Z]{2}[ -]?[A-Z]{1,2}[ -]?[0-9]{4}$"
        return vehicleNumber.matches(regex.toRegex())
    }

    private fun addListItem() {
        list.add(
            0,
            VehicleDetail(
                binding.etInsValidity.text.toString(),
                binding.etVehicleNo.text.toString(),
                rcFrontImgKey ?: "",
                rcBackImgKey ?: "",
                prevPolicyDocsImgKey ?: "",
                rcFrontImgUrl ?: "",
                rcBackImgUrl ?: "",
                prevPolicyDocsImgUrl ?: ""
            )
        )
        setRecyclerView()
        binding.etVehicleNo.getText().clear()
        binding.etInsValidity.text = ""

        //clear rc front image
        binding.cutFrontBtn.visibility = View.GONE
        binding.imgFrontCamera.setImageResource(R.drawable.camera_new)
        rcFrontImgKey = ""
        rcFrontImgUrl = ""

        //clear rc back image
        binding.cutBackBtn.visibility = View.GONE
        binding.imgBackCamera.setImageResource(R.drawable.camera_new)
        rcBackImgKey = ""
        rcBackImgUrl = ""

        //clear previous policy docs image image
        binding.cutPrevPolicyBtn.visibility = View.GONE
        binding.imgPrevPolicyDoc.setImageResource(R.drawable.camera_new)
        prevPolicyDocsImgKey = ""
        prevPolicyDocsImgUrl = ""

        binding.vehicleDetailsCard.visibility = View.GONE
        binding.btnAddImage.visibility = View.VISIBLE
    }

    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, { datepicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                // Create a new Calendar instance to hold the selected date
                val selectedDate = Calendar.getInstance()
                // Set the selected date using the values received from the DatePicker dialog
                selectedDate.set(year, monthOfYear, dayOfMonth)
                // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                // Format the selected date into a string
                val formattedDate = dateFormat.format(selectedDate.time)
                // Update the TextView to display the selected date with the "Selected Date: " prefix
                binding.etInsValidity.text = formattedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog

        val minDate = Calendar.getInstance()
        minDate.add(Calendar.YEAR, -1);
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.YEAR, 1);

        datePickerDialog.datePicker.minDate = minDate.getTimeInMillis();
        datePickerDialog.datePicker.maxDate = maxDate.getTimeInMillis()
        datePickerDialog.show()
    }

    override fun deleteTruck(po: Int) {
        list.removeAt(po)
        setRecyclerView()
    }

    fun backScreen(v: View) {
        onBackPressed()
    }

    fun mobileInfo(v: View) {
        val menu = TruckMenu
        menu.aboutPlan(this, binding.info, "", resources.getString(R.string.alt_mobile_info))
    }

    private fun isValidPhoneNumber(phone: String): Boolean {

        val p = Pattern.compile("([6,7,8,9][0-9]{0,9})")
        val m = p.matcher(phone)
        return (m.find() && m.group() == phone)
    }

    private fun getSpecialCharacterCount(s: String?): Int {
        val blockCharacterSet =
            "1234567890~#^&|$%*!@/()[]-'\":;,?{}+=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪"
        blockCharacterSet.toCharArray()

        for (b in blockCharacterSet) {
            if (s!!.contains(b)) {

                return 0
                break
            }
        }
        return 1
    }

    private fun disableEmojiInTitle() {
        val emojiFilter = InputFilter { source, start, end, dest, dstart, dend ->
            for (index in start until end) {
                val type = Character.getType(source[index])

                when (type) {
                    '*'.toInt(),
                    Character.OTHER_SYMBOL.toInt(),
                    Character.SURROGATE.toInt() -> {
                        return@InputFilter ""
                    }

                    Character.LOWERCASE_LETTER.toInt() -> {
                        val index2 = index + 1
                        if (index2 < end && Character.getType(source[index + 1]) == Character.NON_SPACING_MARK.toInt())
                            return@InputFilter ""
                    }

                    Character.DECIMAL_DIGIT_NUMBER.toInt() -> {
                        val index2 = index + 1
                        val index3 = index + 2
                        if (index2 < end && index3 < end &&
                            Character.getType(source[index2]) == Character.NON_SPACING_MARK.toInt() &&
                            Character.getType(source[index3]) == Character.ENCLOSING_MARK.toInt()
                        )
                            return@InputFilter ""
                    }

                    Character.OTHER_PUNCTUATION.toInt() -> {
                        val index2 = index + 1

                        if (index2 < end && Character.getType(source[index2]) == Character.NON_SPACING_MARK.toInt()) {
                            return@InputFilter ""
                        }
                    }

                    Character.MATH_SYMBOL.toInt() -> {
                        val index2 = index + 1
                        if (index2 < end && Character.getType(source[index2]) == Character.NON_SPACING_MARK.toInt())
                            return@InputFilter ""
                    }
                }
            }
            return@InputFilter null
        }
        binding.etFullName?.filters = arrayOf(emojiFilter)
    }


    private fun getImage() {
        if (imageT == 3) {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.setType("application/pdf");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            activitypdfLauncher.launch(chooseFile)
        } else {
            val imagePickerDialog = ImagePickerDailog(this, this)
            imagePickerDialog.show()
        }
        /*if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Click", "Click opn location ask  ")
            checkLocationPermission()


        } else {
            if (imageT == 3) {
                var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                chooseFile.setType("application/pdf");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file")
                activitypdfLauncher.launch(chooseFile)
            } else {
                val imagePickerDialog = ImagePickerDailog(this, this)
                imagePickerDialog.show()
            }
        }*/
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.CAMERA
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_MEDIA_IMAGES,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_MEDIA_IMAGES,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                }

            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1010) {
            if (grantResults.isNotEmpty()) {
                val locationAccepted: Boolean =
                    grantResults[0] === PackageManager.PERMISSION_GRANTED
                // val cameraAccepted:Boolean = grantResults[1] === PackageManager.PERMISSION_GRANTED

                if (locationAccepted == true) {
                    if (imageT == 3) {
                        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                        chooseFile.setType("application/pdf");
                        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
                        activitypdfLauncher.launch(chooseFile)
                    } /*else {
                        val imagePickerDailog = ImagePickerDailog(this, this)
                        imagePickerDailog.show()
                    }*/
                } else {

                    divPer()
                }
            } else {
                divPer()
            }

        }
    }

    private fun divPer() {
        /* val intent = Intent(this, DevicePermitioins::class.java)
         startActivity(intent)*/
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {

                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }
                val newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500, 500)!!
                val newFile: File = FileHelp().bitmapTofile(newBitmap, this)!!

                uploadImage(newFile, "")


            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 11 && data != null) {

            if (data.extras?.get("data") != null) {
                var newBitmap: Bitmap =
                    data.extras?.get("data") as Bitmap  // FileHelp().resizeImage(data.extras?.get("data") as Bitmap, 500, 500)!!
                val newFile: File = FileHelp().bitmapTofile(newBitmap, this)!!

                uploadImage(newFile, "")
            } else {
                LoggerMessage.toastPrint("Please Retake", this)
            }


        } else {
            //  LoggerMessage.tostPrint( "Request cancelled or something went wrong.",baseContext)
        }
    }

    override fun fromGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun fromCamara() {

        //new 2
//        var intent=Intent(this,CameraXActivity::class.java)
//        startActivity(intent)

        //new 6
        /*val intent = Intent(this, CameraXActivity::class.java)
        intent.putExtra("FRONT", "n")
        intent.putExtra("BACK", "y")
        startForResult.launch(intent)*/
        launchCamera(false, 0)
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == 100) {

                if (Uri.parse(result.data?.getStringExtra("image")) != null) {
//                    var orFile: File =
//                        FileHelp().getFile(this, Uri.parse(result.data?.getStringExtra("image")))!!
//                    var bitmap: Bitmap = FileHelp().FileToBitmap(orFile)
//                    var newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500, 500)!!
//                    var newFile: File = FileHelp().bitmapTofile(newBitmap, this)!!

                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                contentResolver,
                                Uri.parse(result.data?.getStringExtra("image"))
                            )
                        )
                    } else {
                        MediaStore.Images.Media.getBitmap(
                            contentResolver,
                            Uri.parse(result.data?.getStringExtra("image"))
                        )
                    }
                    val newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500, 500)!!
                    val newFile: File = FileHelp().bitmapTofile(newBitmap, this)!!

                    uploadImage(newFile, "")
                }
            }
        }


    private fun viewVehicleDetails(data: VehicleDetail) {
        val builder = AlertDialog.Builder(this@InsuranceActivity)
        val binding =
            VehicleDetailsDialogLayoutBinding.inflate(LayoutInflater.from(this@InsuranceActivity))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        //vehicle number
        binding.etVehicleNo.text = data.vehicleNumber

        //insurance validity
        binding.etInsValidity.text = data.insuranceValidity

        //rc front image
        if (data.rcFrontImgUrl.isNullOrEmpty()) {
            binding.lRcFrontImage.visibility = View.GONE
        } else {
            binding.lRcFrontImage.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(data.rcFrontImgUrl).placeholder(R.drawable.placeholder_image2)
                    .error(R.drawable.placeholder_image2)
                    .into(binding.imgFrontCamera)
            } catch (e: Exception) {

            }
        }

        //rc back image
        if (data.rcBackImgUrl.isNullOrEmpty()) {
            binding.lRcBackImage.visibility = View.GONE
        } else {
            binding.lRcBackImage.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(data.rcBackImgUrl).placeholder(R.drawable.placeholder_image2)
                    .error(R.drawable.placeholder_image2)
                    .into(binding.imgBackCamera)
            } catch (e: Exception) {

            }
        }

        //previous policy docs image
        if (data.policyDocUrl.isNullOrEmpty()) {
            binding.lPrevPolicyDocImg.visibility = View.GONE
        } else {
            binding.lPrevPolicyDocImg.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(R.drawable.pdf_icon)
                    .placeholder(R.drawable.placeholder_image2)
                    .error(R.drawable.placeholder_image2)
                    .into(binding.imgPrevPolicyDoc)
            } catch (e: Exception) {
            }
        }

        binding.imgPrevPolicyDoc.setOnClickListener {
            val intent = Intent(this, ViewPdfScreen::class.java)
            intent.putExtra(
                "pdf",
                data.policyDocUrl
            )
            intent.putExtra("button", "n")
            startActivity(intent)
        }

        //ok button
        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun uploadImage(file: File, token: String) {
        LoadingUtils.showDialog(this, false)
        if (imageT == 3) {
            mViewModel?.trucksupImageUpload(
                PreferenceManager.getAuthToken(),
                "pdf",
                PreferenceManager.prepareFilePartTrucksHum(file, "imageFile"),
                PreferenceManager.prepareFilePartTrucksHum(file, "watermarkFile"),
                this
            )
        } else {
            mViewModel?.trucksupImageUpload(
                PreferenceManager.getAuthToken(),
                "image",
                PreferenceManager.prepareFilePartTrucksHum(file!!, "imageFile"),
                PreferenceManager.prepareFilePartTrucksHum(file!!, "watermarkFile"),
                this
            )
        }
    }

    override fun getImage(value: String, url: String) {
        LoadingUtils.hideDialog()
        if (imageT == 1) {
            rcFrontImgKey = value
            rcFrontImgUrl = url
            binding.cutFrontBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.imgFrontCamera)
            } catch (e: Exception) {
            }
        } else if (imageT == 2) {
            rcBackImgKey = value
            rcBackImgUrl = url
            binding.cutBackBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.imgBackCamera)
            } catch (e: Exception) {
            }
        } else if (imageT == 3) {
            prevPolicyDocsImgKey = value
            prevPolicyDocsImgUrl = url
            binding.cutPrevPolicyBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(R.drawable.pdf_icon)
                    .into(binding.imgPrevPolicyDoc)
            } catch (e: Exception) {
            }
        }
    }

    override fun dataSubmitted(message: String) {}

    override fun imageError(error: String) {
        LoadingUtils.hideDialog()
        LoggerMessage.onSNACK(binding.main, error, this)
    }

    //test
    private fun cameraActivityresult() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    imageUri = data!!.getStringExtra("result").toString()
                    binding.imgFrontCamera.let {
                        Glide.with(applicationContext)
                            .load(data.getStringExtra("result")?.toUri())
                            .into(it)
                    }
                    //profileImage?.setRotation(270F)
                    val orFile: File =
                        FileHelp().getFile(this, data!!.getStringExtra("result")?.toUri())!!
                    val newBitmap: Bitmap = FileHelp().FileToBitmap(orFile)


                    val name = "trucksUp_image" + System.currentTimeMillis() + ".jpg"
                    val pt = Environment.DIRECTORY_PICTURES //+  "/trucksUp";
                    val MEDIA_PATH = Environment.getExternalStorageDirectory().absolutePath + "/" + pt + "/"

                    val filesDir: File = getFilesDir()
                    val imageFile = File(filesDir, name)

                    val os: OutputStream
                    os = FileOutputStream(imageFile)
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 99, os)
                    os.flush()
                    os.close()

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }
    private fun launchCamera(flipCamera: Boolean, cameraOpen: Int){
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", flipCamera)
        intent.putExtra("cameraOpen", cameraOpen)
        launcher!!.launch(intent)
    }
    //test

    fun viewPreviousEnquiry(v: View) {
        //Insurance
        val intent = Intent(this, FinanceHistoryActivity::class.java)
        intent.putExtra("HISTORY_TYPE", "Insurance")
        startActivity(intent)
    }

}