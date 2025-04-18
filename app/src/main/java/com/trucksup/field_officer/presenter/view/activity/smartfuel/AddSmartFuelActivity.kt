package com.trucksup.field_officer.presenter.view.activity.smartfuel

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputFilter
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
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
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.VehicleDetail
import com.trucksup.field_officer.data.model.smartfuel.AddSmartFuelLeadRequest
import com.trucksup.field_officer.data.model.smartfuel.CustomerDetailsSubmitRequest
import com.trucksup.field_officer.data.model.smartfuel.CustomerDocList
import com.trucksup.field_officer.databinding.ActivityAddSmartfuelBinding
import com.trucksup.field_officer.databinding.VehicleDetailsDialogLayoutBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.dialog.FinaceSubmitBox
import com.trucksup.field_officer.presenter.common.image_picker.GetImage
import com.trucksup.field_officer.presenter.common.image_picker.ImagePickerDailog
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.InsuranceController
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.InsuranceListAdapter
import com.trucksup.field_officer.presenter.view.activity.financeInsurance.vml.SubmitInsuranceInquiryRequest
import com.trucksup.field_officer.presenter.view.activity.other.ViewPdfScreen
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.regex.Pattern


@AndroidEntryPoint
class AddSmartFuelActivity : BaseActivity(), GetImage, TrucksFOImageController {
    private lateinit var binding: ActivityAddSmartfuelBinding
    private var list = ArrayList<CustomerDocList>()
    private var mViewModel: SmartFuelViewModel? = null
    private var adapter: InsuranceListAdapter? = null
    private val calendar = Calendar.getInstance()

    private var insuFor: String = "other"
    private var rcFrontImgKey: String? = ""
    private var rcFrontImgUrl: String? = ""
    private var rcBackImgKey: String? = ""
    private var rcBackImgUrl: String? = ""

    //add by me
    private var panImgKey: String? = ""
    private var panImgUrl: String? = ""

    private var AddressFrontImgKey: String? = ""
    private var AddressFrontImgUrl: String? = ""
    private var AddressBackImgKey: String? = ""
    private var AddressBackImgUrl: String? = ""

    private var cancelChequeImgKey: String? = ""
    private var cancelChequeImgUrl: String? = ""

    private var declarationImgKey: String? = ""
    private var declarationImgUrl: String? = ""
    //add by me

    private var prevPolicyDocsImgKey: String? = ""
    private var prevPolicyDocsImgUrl: String? = ""

    private var imageT: Int = 0 //0 default,1 front image,2 back image,3 previous policy docs image
    private var sourceValue: String? = "Trucksup"
    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        adjustFontScale(getResources().configuration, 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_smartfuel)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        mViewModel = ViewModelProvider(this)[SmartFuelViewModel::class.java]

        PreferenceManager.setPhoneNo("9870009988", this)
        binding.etCustomerMobile.setText(PreferenceManager.getPhoneNo(this))
        binding.etReferralCode.setText("7BGHJ9")

        //referral code or sales code
        // binding.etReferralCode.setText(PreferenceManager.getUserData(this)?.salesCode)

        disableEmojiInTitle()
        setListener()
        setupObserver()
        cameraLauncher()
    }

    private fun setupObserver() {
        mViewModel?.resultSubmitSmartFuelLD?.observe(this@AddSmartFuelActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@AddSmartFuelActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success != null) {
                    if (responseModel.success.statuscode == 200) {
                        val abx = FinaceSubmitBox(
                            this, responseModel.success.message,
                            responseModel.success.message, "cl"
                        )
                        abx.show()
                    }
                    else
                    {

                    }
                }
                else
                {

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


        //PAN Camera
        binding.imgPanDoc.setOnClickListener {
            if (panImgKey.isNullOrEmpty()) {
                imageT = 4
                getImage()
            }
        }

        //Address Front Camera
        binding.imgFrontAddressCamera.setOnClickListener {
            if (AddressFrontImgKey.isNullOrEmpty()) {
                imageT = 5
                getImage()
            }
        }

        //Address Back Camera
        binding.imgBackCamera1.setOnClickListener {
            if (AddressBackImgKey.isNullOrEmpty()) {
                imageT = 6
                getImage()
            }
        }

        //Cancel Cheque Camera
        binding.imgchequeDoc.setOnClickListener {
            if (cancelChequeImgKey.isNullOrEmpty()) {
                imageT = 7
                getImage()
            }
        }

        //Declaration From PDF
        binding.imgDeclareDoc.setOnClickListener {
            if (declarationImgKey.isNullOrEmpty()) {
                imageT = 8
                getImage()
            }
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

        //Remove PAN Img
        binding.cutPanBtn.setOnClickListener {
            //clear rc back image
            binding.cutPanBtn.visibility = View.GONE
            binding.imgPanDoc.setImageDrawable(getDrawable(R.drawable.camera_new))
            panImgKey = ""
            panImgUrl = ""
        }

        //Remove Address Front Img
        binding.cutFrontAddressBtn.setOnClickListener {
            //clear rc back image
            binding.cutFrontAddressBtn.visibility = View.GONE
            binding.imgFrontAddressCamera.setImageDrawable(getDrawable(R.drawable.camera_new))
            AddressFrontImgKey = ""
            AddressFrontImgUrl = ""
        }

        //Remove Address Back Img
        binding.cutAddressBackBtn.setOnClickListener {
            //clear rc back image
            binding.cutAddressBackBtn.visibility = View.GONE
            binding.imgBackCamera1.setImageDrawable(getDrawable(R.drawable.camera_new))
            AddressBackImgKey = ""
            AddressBackImgUrl = ""
        }


        //Remove Cancel Cheque Img
        binding.cutChequeImgCard.setOnClickListener {
            //clear rc back image
            binding.cutChequeImgCard.visibility = View.GONE
            binding.imgchequeDoc.setImageDrawable(getDrawable(R.drawable.camera_new))
            cancelChequeImgKey = ""
            cancelChequeImgUrl = ""
        }

        //Remove Declaration form Img
        binding.cutDeclareBtn.setOnClickListener {
            //clear rc back image
            binding.cutDeclareBtn.visibility = View.GONE
            binding.imgDeclareDoc.setImageDrawable(getDrawable(R.drawable.camera_new))
            declarationImgKey = ""
            declarationImgUrl = ""
        }

        //submit button
        binding.btnSumbit.setOnClickListener {
            checkValidation()
        }
    }

    private fun ApiOnSubmitted() {
        showProgressDialog(this, false)
        val request = AddSmartFuelLeadRequest(
            "" + binding.acsAddressProof.selectedItem ?: "",
            "" + AddressBackImgKey ?: "",
            "" + AddressFrontImgKey ?: "",
            "" + PreferenceManager.getPhoneNo(this),
            "" + cancelChequeImgKey ?: "",
            "" + binding.etCustomerEmail.text.toString(),
            "" + binding.etCustomerFullname.text.toString(),
            "" + binding.etCustomerMobile.text.toString(),
            "" + binding.etCustomerVehicleNumber.text.toString(),
            "" + declarationImgKey ?: "",
            "" + binding.etPanNumber.text.toString(),
            "" + panImgKey ?: "",
            "" + rcBackImgKey ?: "",
            "" + rcFrontImgKey ?: "",
            "" + PreferenceManager.getServerDateUtc(),
            "" + PreferenceManager.getRequestNo(),
            "" + PreferenceManager.getPhoneNo(this),
            "" + binding.etReferralCode.text.toString(),
            "BO",
            "" + PreferenceManager.getPhoneNo(this)
        )
        mViewModel?.submitSmartFuel(request)
    }

    private fun checkValidation() {
        if (binding.etCustomerFullname.text.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.etCustomerFullname,
                resources.getString(R.string.enterYourName),
                this
            )
        } else if (getSpecialCharacterCount(binding.etCustomerFullname?.text.toString()) == 0) {
            LoggerMessage.onSNACK(
                binding.etCustomerFullname,
                resources.getString(R.string.enterYourrightName),
                this
            )
        } else if (binding.etCustomerMobile.text.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.etCustomerMobile,
                resources.getString(R.string.enter_mobile_no),
                this
            )
        } else if (isValidPhoneNumber(binding.etCustomerMobile.text.toString()) == false) {
            LoggerMessage.onSNACK(
                binding.etCustomerMobile,
                resources.getString(R.string.enter_right_number_v),
                this
            )
        } else if (binding.etCustomerEmail.text.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.etCustomerEmail,
                resources.getString(R.string.email),
                this
            )
        } else if (isValidEmail(binding.etCustomerEmail.text) == false) {
            LoggerMessage.onSNACK(
                binding.etCustomerEmail,
                resources.getString(R.string.valid_email),
                this
            )
        } else if (binding.etCustomerVehicleNumber.text.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.etCustomerVehicleNumber,
                resources.getString(R.string.enterVehicle),
                this
            )
        } else if (checkVehicleNumber(binding.etCustomerVehicleNumber.text.toString()) == false) {
            LoggerMessage.onSNACK(
                binding.etCustomerVehicleNumber,
                resources.getString(R.string.enterVehicle),
                this
            )
        } else if (rcFrontImgKey.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.etCustomerVehicleNumber,
                resources.getString(R.string.valid_vehicle_front_photo),
                this
            )
        } else if (rcBackImgKey.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.etCustomerVehicleNumber,
                resources.getString(R.string.valid_vehicle_back_photo),
                this
            )
        } else if (binding.etPanNumber.text.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.etCustomerVehicleNumber,
                resources.getString(R.string.pan),
                this
            )
        } else if (isValidPanNumber(binding.etPanNumber.text.toString()) == false) {
            LoggerMessage.onSNACK(
                binding.etCustomerVehicleNumber,
                resources.getString(R.string.valid_pan),
                this
            )
        } else if (panImgKey.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.etCustomerVehicleNumber,
                resources.getString(R.string.pan_photo),
                this
            )
        }
//        else if (binding.acsAddressProof.selectedItem)
//        {
//
//        }
        else if (AddressFrontImgKey.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.etCustomerVehicleNumber,
                resources.getString(R.string.address_front_photo),
                this
            )
        } else if (AddressBackImgKey.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.etCustomerVehicleNumber,
                resources.getString(R.string.address_back_photo),
                this
            )
        } else {
            ApiOnSubmitted()
        }
    }

    fun isValidPanNumber(panNumber: String): Boolean {
        val pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        val matcher = pattern.matcher(panNumber)
        // Check if pattern matches
        if (matcher.matches()) {
            Log.i("Matching", "Yes")
            return true
        }
        return false
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun checkVehicleNumber(vehicleNumber: String): Boolean {
        val regex = "^[A-Z]{2}[ -]?[0-9|A-Z]{2}[ -]?[A-Z]{1,2}[ -]?[0-9]{4}$"
        return vehicleNumber.matches(regex.toRegex())
    }

    fun backScreen(v: View) {
        onBackPressed()
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
                    Character.SURROGATE.toInt(),
                        -> {
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
        binding.etCustomerFullname?.filters = arrayOf(emojiFilter)
    }

    private fun getImage() {
        if (ActivityCompat.checkSelfPermission(
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
        grantResults: IntArray,
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
                    } else {
                        val imagePickerDailog = ImagePickerDailog(this, this)
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
                var newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500, 500)!!
                var newFile: File = FileHelp().bitmapTofile(newBitmap, this)!!

                uploadImage(newFile, "")

//            var orFile: File = FileHelp().getFile(this, uri)!!
//            var bitmap: Bitmap = FileHelp().FileToBitmap(orFile)
//            var newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500, 500)!!
//            var newFile: File = FileHelp().bitmapTofile(newBitmap, this)!!


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
                var newFile: File = FileHelp().bitmapTofile(newBitmap, this)!!

//                getImageToken(newFile)
                uploadImage(newFile, "")
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
        launchCamera(true, 1, false)
    }

    fun uploadImage(file: File, token: String) {
        LoadingUtils.showDialog(this, false)
        if (imageT == 3) {
            mViewModel?.uploadImages(
                PreferenceManager.getAuthToken(),
                "pdf",
                PreferenceManager.prepareFilePartTrucksHum(file, "imageFile"),
                PreferenceManager.prepareFilePartTrucksHum(file, "watermarkFile"),
                this
            )
        } else {
            mViewModel?.uploadImages(
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
        } else if (imageT == 4) {
            panImgKey = value
            panImgUrl = url
            binding.cutPanBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.imgPanDoc)
            } catch (e: Exception) {
            }
        } else if (imageT == 5) {
            AddressFrontImgKey = value
            AddressFrontImgUrl = url
            binding.cutFrontAddressBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.imgFrontAddressCamera)
            } catch (e: Exception) {
            }
        } else if (imageT == 6) {
            AddressBackImgKey = value
            AddressBackImgUrl = url
            binding.cutAddressBackBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.imgBackCamera1)
            } catch (e: Exception) {
            }
        } else if (imageT == 7) {
            cancelChequeImgKey = value
            cancelChequeImgUrl = url
            binding.cutChequeImgCard.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.imgchequeDoc)
            } catch (e: Exception) {
            }
        } else if (imageT == 8) {
            declarationImgKey = value
            declarationImgUrl = url
            binding.cutDeclareBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(url)
                    .into(binding.imgDeclareDoc)
            } catch (e: Exception) {
            }
        } else if (imageT == 3) {
            prevPolicyDocsImgKey = value
            prevPolicyDocsImgUrl = url
            binding.cutDeclareBtn.visibility = View.VISIBLE
            try {
                Glide.with(this)
                    .load(R.drawable.pdf_icon)
                    .into(binding.imgchequeDoc)
            } catch (e: Exception) {
            }
        }
    }

    override fun dataSubmitted(message: String) {}

    override fun imageError(error: String) {
        LoadingUtils.hideDialog()
        LoggerMessage.onSNACK(binding.main, error, this)
    }

    fun viewPreviousEnquiry(v: View) {
        val intent = Intent(this, SmartFuelHistoryActivity::class.java)
        startActivity(intent)
    }

    private fun cameraLauncher() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    val imageUris: Uri = data!!.getStringExtra("result")!!.toUri()
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(),
                        Uri.parse(imageUris.toString())
                    )
                    // Set the image in imageview for display
                    val newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500, 500)!!
                    val newFile: File = FileHelp().bitmapTofile(newBitmap, this)!!
                    uploadImage(newFile, "")

                    //handleImageCapture(bitmap)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun launchCamera(flipCamera: Boolean, cameraOpen: Int, focusView: Boolean) {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", flipCamera)
        intent.putExtra("cameraOpen", cameraOpen)
        intent.putExtra("focusView", focusView)
        launcher?.launch(intent)
    }

}