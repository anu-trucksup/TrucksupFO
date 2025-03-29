package com.trucksup.field_officer.presenter.view.activity.financeInsurance

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.VehicleDetail
import com.trucksup.field_officer.databinding.ActivityInsuranceScreenBinding
import com.trucksup.field_officer.presenter.common.MyAlartBox
import com.trucksup.field_officer.presenter.common.dialog.FinaceSubmitBox
import com.trucksup.field_officer.presenter.common.image_picker.GetImage
import com.trucksup.field_officer.presenter.common.image_picker.ImagePickerDailog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.FileHelper
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.FinanceHistoryViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.InsuranceViewModel
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryRequest
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.truckMenu.TruckMenu
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.Locale
import java.util.regex.Pattern

@AndroidEntryPoint
class InsuranceActivity : BaseActivity(), InsuranceController, GetImage {

    private lateinit var binding: ActivityInsuranceScreenBinding
    private var list = ArrayList<VehicleDetail>()
    private var mViewModel: InsuranceViewModel? = null
    private var adapter: InsuranceListAdapter? = null
    private val calendar = Calendar.getInstance()
    private var insuFor: String = "self"
    private var rcFrontImgKey: String? = ""
    private var rcFrontImgUrl: String? = ""
    private var rcBackImgKey: String? = ""
    private var rcBackImgUrl: String? = ""
    private var prevPolicyDocsImgKey: String? = ""
    private var prevPolicyDocsImgUrl: String? = ""
    private var imageT: Int = 0//0 default,1 front image,2 back image,3 previous policy docs image
    var sourceValue: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_insurance_screen)
        sourceValue = intent.getStringExtra("SOURCE_VALUE")
        //binding.etFullName.setText(PreferenceManager.getUserData(this)?.profileName)
        mViewModel = ViewModelProvider(this)[InsuranceViewModel::class.java]
        binding.etMobile.setText(PreferenceManager.getPhoneNo(this))

        //referral code or sales code
        //binding.etReferralCode.setText(PreferenceManager.getUserData(this)?.salesCode)

        disableEmojiInTitle()
        setListener()
        setupObserver()
    }

    private fun setRecyclerView() {
        adapter = InsuranceListAdapter(this, list, this).apply {
            /*setOnControllerListener2(object : InsListAdap.ControllerListener2 {
                override fun onDeleteTruck(position: Int) {
                    list.removeAt(position)
                    setRecyclerView()
                }

                override fun onShowTruckDetails(data: VehicleDetail) {
                    viewVehicleDetails(data)
                }
            })*/
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

                if (responseModel.success?.message != null ) {
                    val abx = FinaceSubmitBox(this, responseModel.success.message,
                        responseModel.success.message1, "cl")
                    abx.show()

                } else {

                }
            }
        }
    }

    private var activity1Launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
//            val data = result.data
//            val valueFromActivity1 = data?.getStringExtra("data")

                var orFile: File = FileHelper().getFile(this, result.data?.data)!!
//            var bitmap: Bitmap = FileHelp().FileToBitmap(orFile)
//            var newBitmap: Bitmap = FileHelp().resizeImage(bitmap,500,500)!!
//            var newFile: File = FileHelp().bitmapTofile(newBitmap,this)!!

//            getImageToken(newFile)

                getImageToken(orFile)

//            LoadingUtils.showDialog(this,false)
//            MyResponse().uploadImage("jpg","DOC"+ PreferenceManager.getRequestNo(),"",
//                PreferenceManager.prepareFilePart(orFile!!),this,this)
            }
        }

    private fun setListener() {
        //rc front camera
        binding.imgFrontCamera.setOnClickListener {
            imageT = 1
            getImage()
        }

        //rc back camera
        binding.imgBackCamera.setOnClickListener {
            imageT = 2
            getImage()
        }

        //previous policy
        binding.imgPrevPolicyDoc.setOnClickListener {
            imageT = 3
            getImage()
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
            binding.imgFrontCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcFrontImgKey = ""
            rcFrontImgUrl = ""

            //clear rc back image
            binding.imgBackCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcBackImgKey = ""
            rcBackImgUrl = ""

            //clear previous policy docs image image
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
            binding.imgFrontCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcFrontImgKey = ""
            rcFrontImgUrl = ""

            //clear rc back image
            binding.imgBackCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcBackImgKey = ""
            rcBackImgUrl = ""

            //clear previous policy docs image image
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
            binding.imgFrontCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcFrontImgKey = ""
            rcFrontImgUrl = ""

            //clear rc back image
            binding.imgBackCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcBackImgKey = ""
            rcBackImgUrl = ""

            //clear previous policy docs image image
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
            binding.imgFrontCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcFrontImgKey = ""
            rcFrontImgUrl = ""

            //clear rc back image
            binding.imgBackCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            rcBackImgKey = ""
            rcBackImgUrl = ""

            //clear previous policy docs image image
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
                    } else {
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

                        val requestData = SubmitInsuranceInquiryRequest(
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

                        } else {
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
                //                            showProgressDialog()
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
                //                            showProgressDialog()
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
                            val requestData = SubmitInsuranceInquiryRequest(
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
                    //                                showProgressDialog()
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
                    //                                showProgressDialog()
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

        if (isValidPhoneNumber(binding.etMobile?.text.toString()) == false) {
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
        binding.imgFrontCamera.setImageResource(R.drawable.camera_new)
        rcFrontImgKey = ""
        rcFrontImgUrl = ""

        //clear rc back image
        binding.imgBackCamera.setImageResource(R.drawable.camera_new)
        rcBackImgKey = ""
        rcBackImgUrl = ""

        //clear previous policy docs image image
        binding.imgPrevPolicyDoc.setImageResource(R.drawable.camera_new)
        prevPolicyDocsImgKey = ""
        prevPolicyDocsImgUrl = ""

        binding.vehicleDetailsCard.visibility = View.GONE
        binding.btnAddImage.visibility = View.VISIBLE
    }

    private fun showDatePicker() {
        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this, { datePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
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
/*
    override fun dataSubmitted(message: String, message1: String) {
        dismissProgressDialog()
        val abx = FinaceSubmitBox(this, message, message1, "cl")
        abx.show()

    }

    override fun dataError(error: String) {
        dismissProgressDialog()
    }*/

    fun backScreen(v: View) {
        finish()
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

    fun viewPreviousEnquery(v: View) {
        //finance
        val intent = Intent(this, FinanceHistoryActivity::class.java)
        intent.putExtra("HISTORY_TYPE", "Insurance")
        startActivity(intent)
    }

    private fun getImage() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this!!,
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
                activity1Launcher.launch(chooseFile)
            } else {
                val imagePickerDailog = ImagePickerDailog(this, this)
                imagePickerDailog.show()
            }
        }
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
                        activity1Launcher.launch(chooseFile)
                    } else {
                        var imagePickerDailog: ImagePickerDailog = ImagePickerDailog(this, this)
                        imagePickerDailog.show()
                    }
                } else {

                    divPer()
                }
            } else {
                divPer()
            }

        }
    }

    private fun divPer() {
//        val intent = Intent(this, DevicePermitioins::class.java)
//        startActivity(intent)
    }

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                val orFile: File = FileHelper().getFile(this, uri)!!
                val bitmap: Bitmap = FileHelper().FileToBitmap(orFile)
                val newBitmap: Bitmap = FileHelper().resizeImage(bitmap, 500, 500)!!
                val newFile: File = FileHelper().bitmapTofile(newBitmap, this)!!


                getImageToken(newFile)
//            LoadingUtils.showDialog(this,false)
//            MyResponse().uploadImage("jpg","DOC"+ PreferenceManager.getRequestNo(),"",
//                PreferenceManager.prepareFilePart(newFile!!),this,this)


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
                var newFile: File = FileHelper().bitmapTofile(newBitmap, this)!!

                getImageToken(newFile)
//                LoadingUtils.showDialog(this,false)
//                MyResponse().uploadImage("jpg","DOC"+ PreferenceManager.getRequestNo(),"",
//                    PreferenceManager.prepareFilePart(newFile!!),this,this)
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
        val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Start the activity with camera_intent, and request pic id
        // Start the activity with camera_intent, and request pic id
        startActivityForResult(camera_intent, 11)
    }

    /* private fun viewVehicleDetails(data: VehicleDetail) {
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
                     .load(R.drawable.camera_new)
                     .placeholder(R.drawable.placeholder_image2)
                     .error(R.drawable.placeholder_image2)
                     .into(binding.imgPrevPolicyDoc)
             } catch (e: Exception) {
             }
         }

         binding.imgPrevPolicyDoc.setOnClickListener {
             *//* val intent = Intent(this, ViewPdfScreen::class.java)
             intent.putExtra(
                 "pdf",
                 data.policyDocUrl
             )
             intent.putExtra("button", "n")
             startActivity(intent)*//*
        }

        //ok button
        binding.btnOk.setOnClickListener {
            dialog.dismiss()
        }
    }*/

    private fun getImageToken(file: File) {
        /*  showProgressDialog()
          val apiInterface: ApiInterface =
              ApiClient(PreferenceManager.getServerUrl(this)).getClient
          val req: TrucksHubAuthJWTtokenRequest = TrucksHubAuthJWTtokenRequest(
              PreferenceManager.trucksHubAuthData(this)?.apiSecreteKey.toString(),
              PreferenceManager.trucksHubAuthData(this)?.issuer.toString(),
              PreferenceManager.trucksHubAuthData(this)?.password.toString(),
              PreferenceManager.trucksHubAuthData(this)?.userAgent.toString(),
              PreferenceManager.trucksHubAuthData(this)?.userName.toString()
          )

          apiInterface.generateTrucksHubJWTtoken(
              PreferenceManager.trucksHubAuthData(this)?.headerKey.toString().toString(),
              req,
              "JwtAuth/api/Auth/GenerateJWTtoken"
          )
              ?.enqueue(object : Callback<TrucksHubAuthJWTtokenResponse> {
                  override fun onResponse(
                      call: Call<TrucksHubAuthJWTtokenResponse>,
                      response: Response<TrucksHubAuthJWTtokenResponse>
                  ) {
                      if (response.isSuccessful) {

                          if (response.body()?.statusCode == 200) {
                              uploadImage(file, "Bearer " + response.body()!!.accessToken)
                          } else {

                              dismissProgressDialog()
                              var abx: MyAlartBox =
                                  MyAlartBox(
                                      this@InsuranceScreen,
                                      response.body()?.message.toString(),
                                      "m"
                                  )
                              abx?.show()

                          }
                      } else {
                          dismissProgressDialog()
                          var abx: MyAlartBox =
                              MyAlartBox(
                                  this@InsuranceScreen,
                                  resources.getString(R.string.no_data_found),
                                  "m"
                              )
                          abx?.show()
                      }


                  }

                  override fun onFailure(call: Call<TrucksHubAuthJWTtokenResponse>, t: Throwable) {
                      LoggerMessage.LogErrorMsg("Error", "" + t.message)
                      dismissProgressDialog()
                      val data: ErrorModel = ErrorModel(
                          "" + t.message,
                          "" + PreferenceManager.getPhoneNo(this@InsuranceScreen),
                          "" + PreferenceManager.getUserData(this@InsuranceScreen)?.profileName,
                          "" + DeviceInfoUtils.getDeviceModel(this@InsuranceScreen),
                          "API",
                          "JwtAuth/api/Auth/GenerateJWTtoken"
                      )
                      ErrorStore().StoreError(data)
                  }
              })
  */
    }

    fun uploadImage(file: File, token: String) {
        /* MyResponse().uploadTrucksHubImage(
             token,
             "image",
             PreferenceManager.prepareFilePartTrucksHum(file!!, "imageFile"),
             PreferenceManager.prepareFilePartTrucksHum(file!!, "watermarkFile"),
             this,
             this
         )*/
    }

    fun getImage(value: String, url: String) {

        dismissProgressDialog()
        if (imageT == 1) {
            rcFrontImgKey = value
            rcFrontImgUrl = url
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.imgFrontCamera)
            } catch (e: Exception) {
            }
        } else if (imageT == 2) {
            rcBackImgKey = value
            rcBackImgUrl = url
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.imgBackCamera)
            } catch (e: Exception) {
            }
        } else if (imageT == 3) {
            prevPolicyDocsImgKey = value
            prevPolicyDocsImgUrl = url
            try {
//                Glide.with(this)
//                    .load(MyResponse.imagePathUrl+value+"&Position=1")
//                    .into(binding.imgPrevPolicyDoc)
                Glide.with(this)
                    .load(R.drawable.camera_new)
                    .into(binding.imgPrevPolicyDoc)
            } catch (e: Exception) {
            }
        }
    }

    fun imageError(error: String) {
        dismissProgressDialog()
        LoggerMessage?.onSNACK(binding.main, error, this)
    }

}