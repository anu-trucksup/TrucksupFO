package com.trucksup.field_officer.presenter.view.activity.businessAssociate

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityBaSchedulemeetingBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.CommonApplication
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginViewModel
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.CompleteMeetingBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.ScheduleMeetingBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAScheduleMeetingVM
import com.trucksup.field_officer.presenter.view.activity.other.WelcomeLocationActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class BAScheduleMeetingActivity : BaseActivity() {

    private lateinit var binding: ActivityBaSchedulemeetingBinding
    private var photo1: Boolean = false
    private var photo2: Boolean = false
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var mViewModel: BAScheduleMeetingVM? = null

    private var isMeetBAToday: Boolean = false
    private var isLoadAddedToday: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaSchedulemeetingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this)[BAScheduleMeetingVM::class.java]

        binding.firmName.text = "" + PreferenceManager.getUserData(this)?.profilename
        binding.mobile.text = "" + PreferenceManager.getPhoneNo(this)

        setListeners()
        setupObserver()
        cameraListener()
    }

    private fun setListeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.selfiPic.setOnClickListener {
            photo1 = true
            photo2 = false
            launchCamera(false, 0, true)
            /* val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
             startForResult.launch(camera_intent)*/
        }

        binding.officePic.setOnClickListener {
            photo2 = true
            photo1 = false
            launchCamera(true, 1, false)
            /* val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
             startForResult.launch(camera_intent)*/
        }

        binding.metWithShipperYes.setOnClickListener {
            isMeetBAToday = true
            resetSelectionMeetBA()
            binding.metWithShipperYes.setBackgroundColor(resources.getColor(R.color.green_1))
        }

        binding.metWithShipperNo.setOnClickListener {
            isMeetBAToday = false
            resetSelectionMeetBA()
            binding.metWithShipperNo.setBackgroundColor(resources.getColor(R.color.green_1))
        }

        binding.loadYesButton.setOnClickListener {
            isLoadAddedToday = true
            resetLoadAdded()
            binding.loadYesButton.setBackgroundColor(resources.getColor(R.color.green_1))
        }

        binding.loadNoButton.setOnClickListener {
            isLoadAddedToday = false
            resetLoadAdded()
            binding.loadNoButton.setBackgroundColor(resources.getColor(R.color.green_1))
        }

        binding.btnSubmit.setOnClickListener {

         val profileDetail = PreferenceManager.getUserData(this)
            if (validateFields()) {
                val request = CompleteMeetingBARequest(
                    boid = profileDetail?.boUserid?.toInt()!!,
                    contact_MobileNo = binding.etMobileNumber.text.toString(),
                    contact_Person = binding.etName.text.toString(),
                    currAddress = "test",
                    currLocation = "test",
                    cust_Meeting = true,
                    face_ImageKey = "test",
                    fastTag = true,
                    finance = true,
                    followUpDate = PreferenceManager.getServerDateUtc(),
                    gift = true,
                    gps = true,
                    id = 2,
                    insurance = true,
                    isLoadGiven = true,
                    latitude = "122.12",
                    longitude = "222.22",
                    office_ImageKey = "test",
                    remarks = binding.eTRemark.text.toString(),
                    requestDatetime = PreferenceManager.getServerDateUtc(),
                    requestId = PreferenceManager.getRequestNo().toInt(),
                    requestedBy = PreferenceManager.getPhoneNo(this),
                    smartFuel = true,
                    subscription_Plan = true,
                    trucksHub = true
                )
                mViewModel?.onCompleteMeetingBA(request)
            }

        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun validateFields(): Boolean {
        if (TextUtils.isEmpty(binding.etName.text)) {
            LoggerMessage.onSNACK(
                binding.etName,
                resources.getString(R.string.enterYourName),
                this
            )
            return false
        }


        if (getSpecialCharacterCount(binding.etName?.text.toString()) == 0) {
            LoggerMessage.onSNACK(
                binding.etName,
                resources.getString(R.string.enterYourrightName),
                this
            )
            return false
        }

        if (TextUtils.isEmpty(binding.etMobileNumber.text)) {
            LoggerMessage.onSNACK(
                binding.etMobileNumber,
                resources.getString(R.string.enter_mobile_no),
                this
            )
            return false
        }

        if (binding.etMobileNumber.text.length < 10) {
            LoggerMessage.onSNACK(
                binding.etMobileNumber,
                resources.getString(R.string.enter_right_number_v),
                this
            )
            return false
        }

        if (!isValidMobile(binding.etMobileNumber.text.toString())) {
            LoggerMessage.onSNACK(
                binding.etMobileNumber,
                resources.getString(R.string.enter_right_number_v),
                this
            )
            return false
        }

        if (TextUtils.isEmpty(binding.eTRemark.text)) {
            LoggerMessage.onSNACK(
                binding.eTRemark,
                resources.getString(R.string.enterCommercialVehical),
                this
            )
            return false
        }

     /*   if (TextUtils.isEmpty(binding.etInsValidity.text) && list.size == 0) {
            LoggerMessage.onSNACK(
                binding.etInsValidity,
                resources.getString(R.string.insurance_validity_error),
                this
            )
            return false
        }*/


        return true
    }

    private fun resetSelectionMeetBA() {
        binding.metWithShipperYes.setBackgroundColor(resources.getColor(R.color.white))
        binding.metWithShipperNo.setBackgroundColor(resources.getColor(R.color.white))
    }

    private fun resetLoadAdded() {
        binding.loadYesButton.setBackgroundColor(resources.getColor(R.color.white))
        binding.loadNoButton.setBackgroundColor(resources.getColor(R.color.white))
    }

    private fun setupObserver() {
        mViewModel?.onCompleteMeetingBAResponseLD?.observe(this@BAScheduleMeetingActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@BAScheduleMeetingActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {

                    Toast.makeText(this, "Meeting Scheduled Successfully.", Toast.LENGTH_SHORT)
                        .show()
                    val intent =
                        Intent(this@BAScheduleMeetingActivity, BAFollowupActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    val abx = AlertBoxDialog(
                        this@BAScheduleMeetingActivity,
                        responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }

            }
        }
    }

    //add by me
    private fun cameraListener() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    val imageUris: Uri = data!!.getStringExtra("result")!!.toUri()
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver, Uri.parse(imageUris.toString())
                    )

                    // Set the image in imageview for display
                    handleImageCapture(bitmap)
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
        launcher!!.launch(intent)
    }
    //add by me


    /* val startForResult =
         registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
             try {
                 val photo = result.data?.extras?.get("data") as Bitmap?
                 // Set the image in imageview for display
                 handleImageCapture(photo!!)
             }
             catch (e:Exception)
             {

             }
         }*/

    private fun handleImageCapture(bitmap: Bitmap) {
        try {
            if (photo1 == true) {
                binding.selfiPic.setImageBitmap(bitmap)
                photo1 = false
                photo2 = false
            }
            if (photo2 == true) {
                binding.officePic.setImageBitmap(bitmap)
                photo1 = false
                photo2 = false
            }
            bitmap?.let {
                val resizedBitmap = resizeBitmap(it, 800)
                uploadBitmap(resizedBitmap)
            }
        } catch (e: Exception) {
            Log.e("ImageCapture", "Error handling image capture", e)
        }
    }

    private fun uploadBitmap(bitmap: Bitmap) {
        val file = bitmapToFile(bitmap)
        if (file != null) {
//            if (imageType == "s") {
//                viewModel.uploadFrontImage(file, frontImageView, frontImageProgressBar)
//                viewModel.frontImageKey.observe(viewLifecycleOwner) { key ->
//                    frontImage = key
//                }
//            } else {
//                viewModel.uploadOfficeImage(file, officeImageView, officeImageProgressBar)
//                viewModel.officeImageKey.observe(viewLifecycleOwner) { key ->
//                    officeImage = key
//                }
//            }
        }
    }

    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    private fun bitmapToFile(bitmap: Bitmap): File? {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

}