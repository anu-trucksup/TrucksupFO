package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGponboardingStoreproofBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.GPOnboardingData
import com.trucksup.field_officer.presenter.view.activity.growthPartner.vml.GPOnboardingVM
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
@AndroidEntryPoint
class GPOnBoardStoreProofActivity : BaseActivity(), TrucksFOImageController {
    private lateinit var binding: ActivityGponboardingStoreproofBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var onboardViewModel: GPOnboardingVM? = null
    private var ImgKey: String? = ""
    private var ImgKeyUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gponboarding_storeproof)
        val view = binding.root
        setContentView(view)

        onboardViewModel = ViewModelProvider(this)[GPOnboardingVM::class.java]

        binding.cvCamera.setOnClickListener {
            launchCamera()
        }
        binding.btnPreview.setOnClickListener {
            checkValidation()
        }
        cameraLauncher()
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
                        contentResolver,
                        Uri.parse(imageUris.toString())
                    )
                    // Set the image in imageview for display
                    val newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500)
                    val newFile: File = FileHelp().bitmapTofile(newBitmap, this)
                    uploadImage(newFile)

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }
    private fun launchCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 0)
        launcher?.launch(intent)
    }

    private fun checkValidation() {
        if (ImgKey.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.cvCamera,
                resources.getString(R.string.PleaseSelectStorePhoto),
                this
            )
        } else {
            val gson = Gson()
            val json = intent.getStringExtra("userDataJson")
            val gpOnboardingData = gson.fromJson(json, GPOnboardingData::class.java)


            val updatedUser = gpOnboardingData.copy(
                EstablishmentPhotoKey = ImgKey!!,
                EstablishmentPhotoURL = ImgKeyUrl.toString(),
            )

            val updatedJson = gson.toJson(updatedUser)

            //val intent = Intent(this, GpOnboardingPreviewActivity::class.java)
            val intent = Intent(this, GPOnboardingBankActivity::class.java)
            intent.putExtra("userDataJson", updatedJson)
            startActivity(intent)
            //startActivity(Intent(this, GPOnboardingBankActivity::class.java))
        }
        /*else if (binding.ETPanNumberNOB.length() == 10) {
            val s = binding.ETPanNumberNOB.toString() // get your editext value here
            val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
            val matcher: Matcher = pattern.matcher(s)
            // Check if pattern matches
            if (matcher.matches()) {
                panNumber = binding.ETPanNumberNOB.toString()
            } else {
                //LoggerMessage.tostPrint("Enter Right PAN No", this)
                binding.ETPanNumberNOB?.setError("Enter Right PAN Noss")
                // pan_no?.setText("")
            }
        } else {
            binding.ETPanNumberNOB.requestFocus()
            *//*val customErrorDrawable = resources.getDrawable(com.trucksup.fieldofficer.R.drawable.ic_phone)
            customErrorDrawable.setBounds(
                0,
                0,
                customErrorDrawable.intrinsicWidth,
                customErrorDrawable.intrinsicHeight
            )
            binding.ETPanNumberNOB?.setError("Enter Right PAN No" , customErrorDrawable)*//*
            binding.ETPanNumberNOB?.setError("Enter Right PAN No")
            binding.ETPanNumberNOB?.setText("")
        }*/

    }

    fun uploadImage(file: File) {
        showProgressDialog(this, false)

        onboardViewModel?.imageUploadOnboard(
            PreferenceManager.getAuthToken(),
            "image",
            "GrowthPartner",
            PreferenceManager.prepareFilePartTrucksHum(file, "imageFile"),
            PreferenceManager.prepareFilePartTrucksHum(file, "watermarkFile"),
            this
        )
    }

    override fun getImage(valuekey: String, url: String) {
        dismissProgressDialog()
        try {
            Glide.with(this)
                .load(url)
                .into(binding?.profileImage!!)
        } catch (e: Exception) {
        }
        binding?.profileImage?.tag = "y"

        ImgKey = valuekey
        ImgKeyUrl = url
    }

    override fun dataSubmitted(message: String) {
    }

    override fun imageError(error: String) {
        dismissProgressDialog()
        LoggerMessage.onSNACK(binding.profileImage, error, this)
    }
}