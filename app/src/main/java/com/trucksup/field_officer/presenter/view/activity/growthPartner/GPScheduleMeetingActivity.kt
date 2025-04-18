package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.trucksup.field_officer.databinding.ActivityGpSchedulemeetingBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.BAFollowupActivity
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.CompleteMeetingBARequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAScheduleMeetingVM
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.CompleteMeetingGPRequest
import com.trucksup.field_officer.presenter.view.activity.growthPartner.vml.GPScheduleMeetingVM
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class GPScheduleMeetingActivity : BaseActivity() {

    private lateinit var binding: ActivityGpSchedulemeetingBinding
    private var photo1: Boolean = false
    private var photo2: Boolean = false
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var mViewModel: GPScheduleMeetingVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpSchedulemeetingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[GPScheduleMeetingVM::class.java]
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

        binding.btnSubmit.setOnClickListener {
            val request = CompleteMeetingGPRequest(
                boid = 1,
                contact_MobileNo = "7355689120",
                contact_Person = "test",
                currAddress = "test",
                currLocation = "test",
                cust_Meeting = true,
                face_ImageKey = "test",
                fastTag = true,
                finance = true,
                followUpDate = "2025-04-17T04:33:07.250Z",
                gift = true,
                gps = true,
                id = 2,
                insurance = true,
                isLoadGiven = true,
                latitude = "122.12",
                longitude = "222.22",
                office_ImageKey = "test",
                remarks = "test",
                requestDatetime = "2025-04-17T04:33:07.250Z",
                requestId = 12,
                requestedBy = "8527257606",
                smartFuel = true,
                subscription_Plan = true,
                trucksHub = true
            )
            mViewModel?.onCompleteMeetingBA(request)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun setupObserver() {
        mViewModel?.onCompleteMeetingGPResponseLD?.observe(this@GPScheduleMeetingActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@GPScheduleMeetingActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {

                    Toast.makeText(this, "Meeting Scheduled Successfully.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@GPScheduleMeetingActivity, BAFollowupActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    val abx = AlertBoxDialog(
                        this@GPScheduleMeetingActivity,
                        responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }

            }
        }
    }

    /*private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            try {
                val photo = result.data?.extras?.get("data") as Bitmap?
                // Set the image in imageview for display
                handleImageCapture(photo!!)
            } catch (e: Exception) {

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


    //add by me
    private fun cameraListener() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    val imageUris: Uri = data!!.getStringExtra("result")!!.toUri()
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imageUris.toString()))
                    // Set the image in imageview for display
                    handleImageCapture(bitmap)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun launchCamera(flipCamera: Boolean, cameraOpen: Int, focusView: Boolean){
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", flipCamera)
        intent.putExtra("cameraOpen", cameraOpen)
        intent.putExtra("focusView", focusView)
        launcher!!.launch(intent)
    }

    //add by me
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