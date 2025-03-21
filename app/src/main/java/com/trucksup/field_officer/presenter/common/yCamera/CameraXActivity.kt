package com.trucksup.field_officer.presenter.common.yCamera

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityCameraXactivityBinding
import com.trucksup.field_officer.presenter.common.DevicePermission
import com.trucksup.field_officer.presenter.common.parent.BaseActivity

class CameraXActivity : BaseActivity() {

    private lateinit var binding: ActivityCameraXactivityBinding

    private val TAG = "CameraXBasic"
    private var imageCapture: ImageCapture? = null
    private var frontSide: String? = null
    private var backSide: String? = null
    private var cameraSelector: CameraSelector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_xactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        frontSide = intent.getStringExtra("FRONT")
        backSide = intent.getStringExtra("BACK")

        // Select back camera as a default
        if (frontSide == "n" && backSide == "y") {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            binding.flashBtn.visibility = View.VISIBLE
            binding.flipBtn.visibility = View.GONE
        } else if (frontSide == "y" && backSide == "n") {
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            binding.flashBtn.visibility = View.GONE
            binding.flipBtn.visibility = View.GONE
        } else if (frontSide == "y" && backSide == "y") {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            binding.flashBtn.visibility = View.VISIBLE
            binding.flipBtn.visibility = View.VISIBLE
        }

        setListener()

    }

    private fun setListener() {
        //camera click button
        binding.cameraBtn.setOnClickListener {
            takePhoto()
        }

        //flash button
        binding.flashBtn.setOnClickListener {
            if (imageCapture != null) {
                if (imageCapture?.flashMode == ImageCapture.FLASH_MODE_OFF) {
                    binding.imgFlash.setImageResource(R.drawable.flash_on)
                    imageCapture?.setFlashMode(ImageCapture.FLASH_MODE_ON)
                } else {
                    binding.imgFlash.setImageResource(R.drawable.flash_off)
                    imageCapture?.setFlashMode(ImageCapture.FLASH_MODE_OFF)
                }
            }
        }

        //flip camera button
        binding.flipBtn.setOnClickListener {
            if (imageCapture != null) {
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                    binding.flashBtn.visibility = View.GONE
                } else {
                    cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    binding.flashBtn.visibility = View.VISIBLE
                }
                startCamera()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkPer()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {

            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().setFlashMode(ImageCapture.FLASH_MODE_OFF).build()

//            // Select back camera as a default
//            if (frontSide=="n" && backSide=="y")
//            {
//                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//                binding.flashBtn.visibility=View.VISIBLE
//                binding.flipBtn.visibility=View.GONE
//            }
//            else if (frontSide=="y" && backSide=="n")
//            {
//                cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
//                binding.flashBtn.visibility=View.GONE
//                binding.flipBtn.visibility=View.GONE
//            }
//            else if (frontSide=="y" && backSide=="y")
//            {
//                cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//                binding.flashBtn.visibility=View.VISIBLE
//                binding.flipBtn.visibility=View.VISIBLE
//            }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector!!, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

    }

    private fun takePhoto() {
//        LoadingUtils.showDialog(this, false)
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = System.currentTimeMillis()
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TrucksUp")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                   // LoadingUtils.hideDialog()
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    if (output.savedUri != null) {
                        var uri = output.savedUri
                        var intent = Intent()
                        intent.putExtra("image", uri.toString())
                        setResult(100, intent)
                        finish()
                    }
                   // LoadingUtils.hideDialog()

//                    val msg = "Photo capture succeeded: ${output.savedUri}"
//
//                    val uriFilePath = output.savedUri?.path
//                    val path: String = uriFilePath.toString()
////                    Toast.makeText(this@CameraXActivity, path, Toast.LENGTH_SHORT).show()
//
////                    viewBinding.apply {
////
////                        image.setImageURI(output.savedUri)
////                        image.visibility = View.VISIBLE
////
////                    }
//
//                    val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, output.savedUri!!))
//                    } else {
//                        MediaStore.Images.Media.getBitmap(contentResolver, output.savedUri)
//                    }

//                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, msg)
                }
            }
        )
// if you don't want to store captured photo in  external storage, just need a image
        /*  imageCapture.takePicture(cameraExecutor, object :
               ImageCapture.OnImageCapturedCallback() {
               override fun onCaptureSuccess(image: ImageProxy) {
                   //get bitmap from image
                   Handler(Looper.getMainLooper()).post(Runnable {

                       val bitmap = imageProxyToBitmap(image)
                       viewBinding.image.setImageBitmap(bitmap)
                       viewBinding.image.visibility = View.VISIBLE
                    })
                   super.onCaptureSuccess(image)
               }

               override fun onError(exception: ImageCaptureException) {
                   super.onError(exception)
               }

           })*/


    }
//    fun BitMapToString(bitmap: Bitmap): String {
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
//        val b = baos.toByteArray()
//        return Base64.encodeToString(b, Base64.DEFAULT)
//    }


    fun checkPer() {
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
            grantPermission()
        } else {
            startCamera()
        }
    }

    private fun grantPermission() {
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

                if (locationAccepted == true) {

                    startCamera()

                } else {

                    divPer()
                }
            } else {
                divPer()
            }

        }
    }

    private fun divPer() {
        val intent = Intent(this, DevicePermission::class.java)
        startActivity(intent)
    }


}