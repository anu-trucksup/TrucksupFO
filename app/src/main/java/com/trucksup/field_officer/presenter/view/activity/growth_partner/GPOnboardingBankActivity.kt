package com.trucksup.field_officer.presenter.view.activity.growth_partner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGponboardBankBinding
import com.trucksup.field_officer.presenter.common.image_picker.GetImage
import com.trucksup.field_officer.presenter.common.image_picker.ImagePickerDailog
import com.trucksup.field_officer.presenter.utils.FileHelper
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import java.io.File
import java.util.regex.Matcher
import java.util.regex.Pattern


class GPOnboardingBankActivity : AppCompatActivity(), GetImage, View.OnClickListener {
    private lateinit var binding: ActivityGponboardBankBinding
    var panNumber: String = ""
    private var imageType: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gponboard_bank)
        val view = binding.root
        setContentView(view)

        setOnClicks()

        binding.ETUploadPassNOB.setOnClickListener {
            getImage()
        }
    }

    fun CheckValidation() {
        if (binding.etAccountHolderName.text.isEmpty()) {
            binding.etAccountHolderName.requestFocus()
            binding.etAccountHolderName.setError("")
        } else if (binding.ETAccountHolderNumber.text.isEmpty()) {
            binding.etAccountHolderName.requestFocus()
            binding.ETAccountHolderNumber.setError("")
        } else if (binding.ETReenterAccountNumber.text.isEmpty()) {
            binding.ETReenterAccountNumber.requestFocus()
            binding.ETReenterAccountNumber.setError("")
        } else if (binding.ETBankNameNOB.text.isEmpty()) {
            binding.ETBankNameNOB.requestFocus()
            binding.ETBankNameNOB.setError("")
        } else if (binding.ETIfscCodeNOB.text.isEmpty()) {
            binding.ETIfscCodeNOB.requestFocus()
            binding.ETIfscCodeNOB.setError("")
        } else if (binding.ETPanNumberNOB.length() == 10) {
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
            /*val customErrorDrawable = resources.getDrawable(com.trucksup.fieldofficer.R.drawable.ic_phone)
            customErrorDrawable.setBounds(
                0,
                0,
                customErrorDrawable.intrinsicWidth,
                customErrorDrawable.intrinsicHeight
            )
            binding.ETPanNumberNOB?.setError("Enter Right PAN No" , customErrorDrawable)*/
            binding.ETPanNumberNOB?.setError("Enter Right PAN No")
            binding.ETPanNumberNOB?.setText("")
        }

    }

    private fun setOnClicks() {
        binding.btnSubmit!!.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        val item_id = v?.id
        when (item_id) {
            R.id.btnSubmit -> CheckValidation()
        }
    }


    fun getImage() {
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
            val imagePickerDailog = ImagePickerDailog(this, this)
            imagePickerDailog.show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 11 && data != null) {
            if (data.extras?.get("data") != null) {
                var mainBitmap: Bitmap = data.extras?.get("data") as Bitmap
                //   image?.setImageBitmap(mainBitmap)
                if (mainBitmap != null) {
                    var newBitmap: Bitmap = FileHelper().resizeImage(mainBitmap, 500, 500)!!
                    var newFile: File = FileHelper().bitmapTofile(newBitmap, this)!!


                    //progressBarr?.show()
                    /*myResponse?.uploadImage(
                        "jpg", "DOC" + PreferenceManager.getRequestNo(), "" + name,
                        PreferenceManager.prepareFilePart(newFile!!), this, this
                    )*/

                } else {
                    LoggerMessage.onSNACK(
                        binding.image!!,
                        "Some Thing Wrong Please Retake Image",
                        this
                    )
                }
            } else {
                LoggerMessage.onSNACK(binding.image!!, "Some Thing Wrong Please Retake Image", this)
            }


        } else {
            //  LoggerMessage.tostPrint( "Request cancelled or something went wrong.",baseContext)
        }
    }

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            // image?.setImageURI(uri)

            var orFile: File = FileHelper().getFile(this, uri)!!
            var bitmap: Bitmap = FileHelper().FileToBitmap(orFile)
            var newBitmap: Bitmap = FileHelper().resizeImage(bitmap, 500, 500)!!
            var newFile: File = FileHelper().bitmapTofile(newBitmap, this)!!


            //progressBarr?.show()
            /*myResponse?.uploadImage(
                "jpg", "DOC" + PreferenceManager.getRequestNo(), "" + name,
                PreferenceManager.prepareFilePart(newFile!!), this, this
            )*/


        } else {
            Log.d("PhotoPicker", "No media selected")
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
}