package com.trucksup.field_officer.presenter.view.activity.businessAssociate

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityBaonboardDocBinding
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.image_picker.GetImage
import com.trucksup.field_officer.presenter.common.image_picker.ImagePickerDailog
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.FileHelper
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class BAOnboardDocActivity : BaseActivity(), GetImage {
    private lateinit var binding: ActivityBaonboardDocBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var imageUri: String = ""
    private var docType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaonboardDocBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)


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

        binding.tvProcees.setOnClickListener {
            val intent1 = Intent(this, BAnOnboardingActivity::class.java)
            intent1.putExtra("data", "no")
            intent1.putExtra("doc", docType)
            intent1.putExtra("img", "")
            startActivity(intent1)

        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }


    }

    fun camera() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    imageUri = data!!.getStringExtra("result").toString()
                    binding.image.visibility = View.VISIBLE
                    binding.image.let {
                        Glide.with(getApplicationContext())
                            .load(data!!.getStringExtra("result")?.toUri())
                            .into(it)
                    }
                    //profileImage?.setRotation(270F)
                    var orFile: File =
                        FileHelp().getFile(this, data!!.getStringExtra("result")?.toUri())!!
                    var newBitmap: Bitmap = FileHelp().FileToBitmap(orFile)


                    val name = "trucksUp_image" + System.currentTimeMillis() + ".jpg"
                    val pt = Environment.DIRECTORY_PICTURES //+  "/trucksUp";
                    val MEDIA_PATH =
                        Environment.getExternalStorageDirectory().absolutePath + "/" + pt + "/"

                    val filesDir: File = getFilesDir()
                    val imageFile = File(filesDir, name)

                    val os: OutputStream
                    os = FileOutputStream(imageFile)
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 99, os)
                    os.flush()
                    os.close()

                    //LoadingUtils?.showDialog(this, false)
                    //LoadingUtils.showDialog(this, false)
                    /*MyResponse()?.uploadImage(
                        "jpg",
                        "DOC" + PreferenceManager.getRequestNo(),
                        "" + PreferenceManager.getPhoneNo(this),
                        PreferenceManager.prepareFilePart(imageFile!!),
                        this,
                        this
                    )*/
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
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
            val imagePickerDialog = ImagePickerDailog(this, this)
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

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
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