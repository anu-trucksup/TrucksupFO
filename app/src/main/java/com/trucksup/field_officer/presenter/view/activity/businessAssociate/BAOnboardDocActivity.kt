package com.trucksup.field_officer.presenter.view.activity.businessAssociate

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityBaonboardDocBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.image_picker.GetImage
import com.trucksup.field_officer.presenter.common.image_picker.ImagePickerDialog
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.FileHelper
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAOnboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class BAOnboardDocActivity : BaseActivity(), GetImage, TrucksFOImageController {
    private lateinit var binding: ActivityBaonboardDocBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var baonboardViewModel: BAOnboardViewModel? = null
    private var docType: String = ""
    private var imageUrl: String = ""
    private var frontImgKey: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaonboardDocBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
        baonboardViewModel = ViewModelProvider(this)[BAOnboardViewModel::class.java]

        binding.vc.setOnClickListener {
            docType = "visiting card"
            binding.vc.background = resources.getDrawable(R.drawable.edit_box_blue)
            binding.vc.setTextColor(resources.getColor(R.color.white))
            binding.tl.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.tl.setTextColor(resources.getColor(R.color.black))
            binding.sb.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.sb.setTextColor(resources.getColor(R.color.black))
            binding.gc.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.gc.setTextColor(resources.getColor(R.color.black))
            binding.se.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.se.setTextColor(resources.getColor(R.color.black))
        }
        binding.tl.setOnClickListener {
            docType = "trade licence"
            binding.tl.background = resources.getDrawable(R.drawable.edit_box_blue)
            binding.tl.setTextColor(resources.getColor(R.color.white))
            binding.vc.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.vc.setTextColor(resources.getColor(R.color.black))
            binding.sb.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.sb.setTextColor(resources.getColor(R.color.black))
            binding.gc.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.gc.setTextColor(resources.getColor(R.color.black))
            binding.se.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.se.setTextColor(resources.getColor(R.color.black))
        }
        binding.sb.setOnClickListener {
            docType = "signage board"
            binding.sb.background = resources.getDrawable(R.drawable.edit_box_blue)
            binding.sb.setTextColor(resources.getColor(R.color.white))
            binding.vc.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.vc.setTextColor(resources.getColor(R.color.black))
            binding.tl.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.tl.setTextColor(resources.getColor(R.color.black))
            binding.gc.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.gc.setTextColor(resources.getColor(R.color.black))
            binding.se.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.se.setTextColor(resources.getColor(R.color.black))
        }
        binding.gc.setOnClickListener {
            docType = "GSTIN Certificate"
            binding.gc.background = resources.getDrawable(R.drawable.edit_box_blue)
            binding.gc.setTextColor(resources.getColor(R.color.white))
            binding.vc.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.vc.setTextColor(resources.getColor(R.color.black))
            binding.tl.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.tl.setTextColor(resources.getColor(R.color.black))
            binding.sb.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.sb.setTextColor(resources.getColor(R.color.black))
            binding.se.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.se.setTextColor(resources.getColor(R.color.black))
        }
        binding.se.setOnClickListener {
            docType = "shop and establishment"
            binding.se.background = resources.getDrawable(R.drawable.edit_box_blue)
            binding.se.setTextColor(resources.getColor(R.color.white))
            binding.vc.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.vc.setTextColor(resources.getColor(R.color.black))
            binding.tl.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.tl.setTextColor(resources.getColor(R.color.black))
            binding.sb.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.sb.setTextColor(resources.getColor(R.color.black))
            binding.gc.background = resources.getDrawable(R.drawable.edit_new_box)
            binding.gc.setTextColor(resources.getColor(R.color.black))
        }

        binding.tvUploadImage.setOnClickListener {
            getImage()
        }

        binding.tvProceed.setOnClickListener {

            if (!submitValidation()) {
                return@setOnClickListener
            }

            val intent1 = Intent(this, BAnOnboardingActivity::class.java)
            intent1.putExtra("data", "no")
            intent1.putExtra("docType", docType)
            intent1.putExtra("imgUrl", imageUrl)
            startActivity(intent1)

        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        cameraActivityResult()
    }

    private fun cameraActivityResult() {
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
                    val newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500)
                    val newFile: File = FileHelp().bitmapTofile(newBitmap, this)
                    uploadImage(newFile)

                    //handleImageCapture(bitmap)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun submitValidation(): Boolean {
        if (TextUtils.isEmpty(docType)) {
            LoggerMessage.onSNACK(
                binding.tvProceed,
                getString(R.string.select_doc_type),
                this
            )
            return false
        }
        if (TextUtils.isEmpty(imageUrl)) {
            LoggerMessage.onSNACK(
                binding.tvProceed,
                getString(R.string.select_doc_img),
                this
            )
            return false
        }
        return true
    }


    fun getImage() {
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
            val imagePickerDialog = ImagePickerDialog(this, this)
            imagePickerDialog.show()
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
                val newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500)
                val newFile: File = FileHelp().bitmapTofile(newBitmap, this)

                uploadImage(newFile)

            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun fromGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun fromCamara() {
        launchCamera(true, 1, false)
    }

    private fun launchCamera(flipCamera: Boolean, cameraOpen: Int, focusView: Boolean) {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", flipCamera)
        intent.putExtra("cameraOpen", cameraOpen)
        intent.putExtra("focusView", focusView)
        launcher?.launch(intent)
    }


    fun uploadImage(file: File) {
        showProgressDialog(this, false)

        baonboardViewModel?.uploadProfileImage(
            PreferenceManager.getAuthToken(),
            "image",
            PreferenceManager.prepareFilePartTrucksHum(file, "imageFile"),
            PreferenceManager.prepareFilePartTrucksHum(file, "watermarkFile"),
            this
        )
    }

    override fun getImage(valuekey: String, url: String) {
        dismissProgressDialog()
        binding.image.visibility = View.VISIBLE
        imageUrl = valuekey
        try {
            Glide.with(this)
                .load(url)
                .into(binding.image)
        } catch (_: Exception) {
        }
        binding.image.tag = "y"

        frontImgKey = valuekey
    }

    override fun dataSubmitted(message: String) {
    }

    override fun imageError(error: String) {
        dismissProgressDialog()
        LoggerMessage.onSNACK(binding.image, error, this)
    }

}