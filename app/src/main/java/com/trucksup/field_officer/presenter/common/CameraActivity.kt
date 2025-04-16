package com.trucksup.field_officer.presenter.common

import android.Manifest
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.drawable.Drawable
import android.hardware.camera2.CameraCharacteristics
import android.location.LocationManager
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.MotionEvent
import android.view.OrientationEventListener
import android.view.ScaleGestureDetector
import android.view.Surface
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraState
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.ResolutionFilter
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityCameraBinding
import com.trucksup.field_officer.presenter.common.Utils.ORIENT_LANDSCAPE_LEFT
import com.trucksup.field_officer.presenter.common.Utils.ORIENT_LANDSCAPE_RIGHT
import com.trucksup.field_officer.presenter.common.Utils.ORIENT_PORTRAIT
import com.trucksup.field_officer.presenter.common.Utils.appSettingOpen
import com.trucksup.field_officer.presenter.common.Utils.warningPermissionDialog
import com.trucksup.field_officer.presenter.view.activity.auth.signup.SignUpActivity
import com.trucksup.field_officer.presenter.view.activity.dashboard.HomeActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CameraActivity: AppCompatActivity() {

    private val camera_binding: ActivityCameraBinding by lazy {
        ActivityCameraBinding.inflate(layoutInflater)
    }

   // private val graphicOverlay by lazy { findViewById<GraphicOverlay>(R.id.graphicOverlay_finder) }
   // private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    //test

    private val multiplePermissionId = 14
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf(
            Manifest.permission.CAMERA,
            //Manifest.permission.ACCESS_FINE_LOCATION,
            //Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    } else {
        arrayListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            //Manifest.permission.ACCESS_FINE_LOCATION,
            //Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }

    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var cameraSelector: CameraSelector
    private var mLastHandledOrientation = 0
    private var orientationEventListener: OrientationEventListener? = null
    //private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var selectedResolution: Size? = null
    private lateinit var cameraExecutor: ExecutorService
    private var imageAnalyzer: ImageAnalysis? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //test
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContentView(camera_binding.root)

        //getLocation()

        if (checkMultiplePermission()) {
            startCamera()
        }

        camera_binding.captureIB.setOnClickListener {
            takePhoto()
        }


        val cameraOpen = intent.getIntExtra("cameraOpen", 0)
        if(cameraOpen == 1){
            lensFacing = CameraSelector.LENS_FACING_BACK
        }else{
            lensFacing = CameraSelector.LENS_FACING_FRONT
        }

        //check flip action on camera
        val flipCamera = intent.getBooleanExtra("flipCamera", false)
        val focusViews = intent.getBooleanExtra("focusView", true)
        if(focusViews){
            camera_binding.focusView.visibility = View.VISIBLE
        }else{
            camera_binding.focusView.visibility = View.GONE
        }
        if(flipCamera){
            camera_binding.flipCameraIB.visibility = View.VISIBLE
            camera_binding.flipCameraIB.setOnClickListener {
                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_FRONT) {
                    CameraSelector.LENS_FACING_BACK
                } else {
                    CameraSelector.LENS_FACING_FRONT
                }
                bindCameraUserCases()
            }
        }else{
            camera_binding.flipCameraIB.visibility = View.GONE

        }


        camera_binding.flashToggleIB.setOnClickListener {
            setFlashIcon(camera)
        }

    }

    private fun setFlashIcon(camera: Camera) {
        if (camera.cameraInfo.hasFlashUnit()) {
            if (camera.cameraInfo.torchState.value == 0) {
                camera.cameraControl.enableTorch(true)
                camera_binding.flashToggleIB.setImageResource(R.drawable.flash_off)
            } else {
                camera.cameraControl.enableTorch(false)
                camera_binding.flashToggleIB.setImageResource(R.drawable.flash_on)
            }
        } else {
            Toast.makeText(
                this,
                "Flash is Not Available",
                Toast.LENGTH_LONG
            ).show()
            camera_binding.flashToggleIB.isEnabled = false
        }
    }

    /* private fun setAspectRatio(ratio: String) {
         camera_binding.previewview.layoutParams =
             camera_binding.previewview.layoutParams.apply {
                 if (this is ConstraintLayout.LayoutParams) {
                     dimensionRatio = ratio
                 }
             }
     }*/

    private fun checkMultiplePermission(): Boolean {
        val listPermissionNeeded = arrayListOf<String>()
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this, permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
               // Toast.makeText(this, "ok",Toast.LENGTH_SHORT).show()
                listPermissionNeeded.add(permission)
            }
        }
        if (listPermissionNeeded.isNotEmpty()) {
            //Toast.makeText(this, "not",Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(
                this, listPermissionNeeded.toTypedArray(), multiplePermissionId
            )
            return false
        }
       // Toast.makeText(this, "ooh",Toast.LENGTH_SHORT).show()

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == multiplePermissionId) {
            if (grantResults.isNotEmpty()) {
                var isGrant = true
                for (element in grantResults) {
                    if (element == PackageManager.PERMISSION_DENIED) {
                        isGrant = false
                    }
                }
                if (isGrant) {
                    // here all permission granted successfully
                    startCamera()
                    //getLocation()
                } else {
                    var someDenied = false
                    for (permission in permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this, permission
                            )
                        ) {
                            if (ActivityCompat.checkSelfPermission(
                                    this, permission
                                ) == PackageManager.PERMISSION_DENIED
                            ) {
                                someDenied = true
                            }
                        }
                    }
                    if (someDenied) {
                        // here app Setting open because all permission is not granted
                        // and permanent denied
                        appSettingOpen(this)
                        finish()
                    } else {
                        // here warning permission show
                        warningPermissionDialog(this) { _: DialogInterface, which: Int ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> checkMultiplePermission()
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }


    private fun startCamera() {
        createNewExecutor()
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)


        cameraProviderFuture.addListener({
            Log.e("CameraResolutionsTAG", "startCamera() selectedResolution: $selectedResolution")
            cameraProvider = cameraProviderFuture.get()
            val resolutions = getSizeResolutions(cameraProvider)
            Log.e("CameraResolutionsTAG", "getAvailableResolutions resolutions: $resolutions")
            setupResolutionSpinner(resolutions)
            selectedResolution = resolutions.firstOrNull() ?: Size(800, 480)
            Log.e(
                "CameraResolutionsTAG",
                "getAvailableResolutions selectedResolution: $selectedResolution"
            )

            imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
               /* .also {
                    it.setAnalyzer(cameraExecutor)
                }*/
            bindCameraUserCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun createNewExecutor() {
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

   /* private fun selectAnalyzer(): ImageAnalysis.Analyzer {
        return FaceContourDetectionProcessor(graphicOverlay) {
            onSuccessCallback(
                it
            )
        }
    }

    private fun onSuccessCallback(faceStatus: FaceStatus) {
        if (faceStatus.name == FaceStatus.VALID.name) {
            takePhoto()
            DialogBoxes.messageDialog(this, "Valid Face")
            Log.e("facestatus", "This is it ${faceStatus.name}")
        } else {
            Log.e("facestatus", "This is it ${faceStatus.name}")
        }
    }*/

    private fun bindCameraUserCases() {


        val rotatedResolution = getRotatedResolution(
            selectedResolution!!,
            //camera_binding.previewview.display.rotation
            0
        )
        val resolutionFilter = object : ResolutionFilter {
            override fun filter(
                supportedSizes: List<Size>,
                rotationDegrees: Int
            ): List<Size?> {
                // Only return the selected resolution in the list
                if (supportedSizes.contains(rotatedResolution)) {
                    return listOf(rotatedResolution)
                }
                return supportedSizes
            }
        }
        val resolutionSelector = ResolutionSelector.Builder().setResolutionFilter(
            resolutionFilter
        ).build()


        val preview = Preview.Builder()
            .setResolutionSelector(
                resolutionSelector
            )
            .build().also {
                it.surfaceProvider = camera_binding.previewview.surfaceProvider
            }

        Log.e(
            "CameraResolutionsTAG",
            "ImageCapture.Builder() selectedResolution: $rotatedResolution"
        )
        imageCapture = ImageCapture.Builder()
            .setResolutionSelector(resolutionSelector)
            //.setTargetRotation(camera_binding.previewview.display.rotation)
            .setTargetRotation(Surface.ROTATION_0)
            .build()
        Log.e(
            "CameraResolutionsTAG",
            "ImageCapture.Builder() imageCapture.resolutionInfo?.resolution: ${imageCapture.resolutionInfo?.resolution}"
        )
        cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        orientationEventListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                // Monitors orientation values to determine the target rotation value
                val myRotation = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture.targetRotation = myRotation
                val currOrient = when (orientation) {
                    in 75..134 -> ORIENT_LANDSCAPE_RIGHT
                    in 225..289 -> ORIENT_LANDSCAPE_LEFT
                    else -> ORIENT_PORTRAIT
                }
                if (currOrient != mLastHandledOrientation) {
                    val degrees = when (currOrient) {
                        ORIENT_LANDSCAPE_LEFT -> 90
                        ORIENT_LANDSCAPE_RIGHT -> -90
                        else -> 0
                    }

                    camera_binding.captureIB.animate()
                        .rotation(degrees.toFloat()).start()
                    mLastHandledOrientation = currOrient
                }
            }
        }
        orientationEventListener?.enable()

        try {
            cameraProvider.unbindAll()

            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture, imageAnalyzer
            )
            camera.cameraInfo.cameraState.observe(this) { cameraState ->
                val type = cameraState.type
                val error = cameraState.error

                if (error == null && type == CameraState.Type.OPEN) {
                    val resolutionInfo = imageCapture.resolutionInfo
                    if (resolutionInfo != null) {
                        val (width, height) = resolutionInfo.resolution.run { width to height }
                        Log.e(
                            "CameraResolutionsTAG",
                            "cameraState: Real resolution: ${width}x${height}"
                        )
                    } else {
                        Log.e("CameraResolutionsTAG", "cameraState: resolutionInfo is still null")
                    }
                }
            }
            setUpZoomTapToFocus()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getRotatedResolution(resolution: Size, rotationDegrees: Int): Size {
        return if (rotationDegrees == Surface.ROTATION_0 || rotationDegrees == Surface.ROTATION_180) {
            Size(resolution.height, resolution.width)
        } else {
            Size(resolution.width, resolution.height)
        }
    }

    private fun setUpZoomTapToFocus() {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio = camera.cameraInfo.zoomState.value?.zoomRatio ?: 1f
                val delta = detector.scaleFactor
                camera.cameraControl.setZoomRatio(currentZoomRatio * delta)
                return true
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(this, listener)

        camera_binding.previewview.setOnTouchListener { view, event ->
            scaleGestureDetector.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_DOWN) {
                val factory = camera_binding.previewview.meteringPointFactory
                val point = factory.createPoint(event.x, event.y)
                val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                    .setAutoCancelDuration(2, TimeUnit.SECONDS)
                    .build()
//                val x = event.x
//                val y = event.y
//
//                val focusCircle = RectF(x-50,y-50, x+50,y+50)
//
//                mainBinding.focusCircleView.focusCircle = focusCircle
//                mainBinding.focusCircleView.invalidate()
                camera.cameraControl.startFocusAndMetering(action)

                view.performClick()
            }
            true
        }
    }

    private fun takePhoto() {
        val fileName = SimpleDateFormat(
            "yyyy_MM_dd HH_mm_ss", Locale.getDefault()
        ).format(System.currentTimeMillis()) + " ${selectedResolution!!.width} x ${selectedResolution!!.height}" + ".jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val imageUri: Uri?
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            // Android 10+ (Scoped Storage)
            Log.e("CameraResolutionsTAG", "Saving in Scoped Storage")

            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Images")
            imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        } else {
            // Android 9 and Below (Direct File Path)
            val state = Environment.getExternalStorageState()
            if (state == Environment.MEDIA_MOUNTED) {
                val imageFolder = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "Images"
                )

                if (!imageFolder.exists()) {
                    imageFolder.mkdirs() // Ensure the directory exists
                }

                val imageFile = File(imageFolder, fileName)
                imageUri = Uri.fromFile(imageFile)
            } else {
                Log.e("CameraResolutionsTAG", "Storage is not mounted")
                return
            }
        }

        if (imageUri == null) {
            Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show()
            return
        }
        val metadata = ImageCapture.Metadata().apply {
            isReversedHorizontal = (lensFacing == CameraSelector.LENS_FACING_FRONT)
        }

        val outputOption = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            ImageCapture.OutputFileOptions.Builder(
                contentResolver, imageUri, contentValues
            ).setMetadata(metadata).build()
        } else {
            val imageFolder = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Images"
            )

            if (!imageFolder.exists()) {
                imageFolder.mkdirs() // Ensure the directory exists
            }

            val imageFile = File(imageFolder, fileName)
            ImageCapture.OutputFileOptions.Builder(imageFile).setMetadata(metadata).build()
        }
        Log.e(
            "CameraResolutionsTAG",
            "imageCapture.takePicture imageCapture.resolutionInfo?.resolution: ${imageCapture.resolutionInfo?.resolution}"
        )

        imageCapture.takePicture(outputOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {

                @RequiresApi(Build.VERSION_CODES.N)
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {

                    val savedUri = outputFileResults.savedUri ?: return
                    val inputStream = contentResolver.openInputStream(savedUri) ?: return
                    val exif = ExifInterface(inputStream)
                    inputStream.close()

                    val width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1)
                    val height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1)
                    Log.e("CameraResolutionsTAG", "ExifInterface width: $width height: $height")

                    val intents = Intent(this@CameraActivity, HomeActivity::class.java)
                    intents.putExtra("result", savedUri.toString())
                    setResult(RESULT_OK, intents)
                    finish()


                    Glide.with(this@CameraActivity).load(savedUri)
                        .addListener(object :
                            RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                model: Any,
                                target: Target<Drawable?>?,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {
                                //viewHolder.progress.visibility =View.GONE
                                return false
                            }
                        }).into(camera_binding.getCaptureImg)
                   /* val message = "Photo Capture Succeeded: ${outputFileResults.savedUri}"
                    Toast.makeText(
                        this@CameraActivity, message, Toast.LENGTH_LONG
                    ).show()*/

                    val intent = Intent(this@CameraActivity, SignUpActivity::class.java)
                    intent.putExtra("result", savedUri.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    Toast.makeText(
                        this@CameraActivity,
                        exception.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        camera_binding.getCaptureImg.setOnClickListener {
            /*var intent:Intent = Intent(this, UserLocationUpdationActivity::class.java)
            intent.putExtra("lat",camera_binding.latitudeTextview.text.toString())
            intent.putExtra("lat",camera_binding.longitudeTextview.text.toString())
            startActivity(intent)*/
            /* val intent = Intent(this, UserLocationUpdationService::class.java)
             intent.putExtra("name", "Geek for Geeks")
             startService(intent)*/
            //startService(Intent(applicationContext, UserLocationUpdationService::class.java))
        }

    }


    @OptIn(ExperimentalCamera2Interop::class)
    private fun getSizeResolutions(cameraProvider: ProcessCameraProvider): List<Size> {
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        val cameraInfo = cameraProvider.bindToLifecycle(
            this@CameraActivity,
            cameraSelector
        ).cameraInfo
        val camera2CameraInfo = Camera2CameraInfo.from(cameraInfo)
        val characteristics = camera2CameraInfo.getCameraCharacteristic(
            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
        )
        val sizes = characteristics?.getOutputSizes(ImageFormat.JPEG)
        sizes?.forEach { size ->
            Log.e("CameraResolutions", "Available size: ${size.width} x ${size.height}")
        }
        return sizes?.toList() ?: emptyList()
    }

    private fun setupResolutionSpinner(resolutions: List<Size>) {
        Log.e("CameraResolutionsTAG", "setupResolutionSpinner resolutions: $resolutions")
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            resolutions.map { "${it.width} x ${it.height}" })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        camera_binding.resolutionSpinner.adapter = adapter

        camera_binding.resolutionSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View?, position: Int, id: Long
                ) {
                    val newResolution = resolutions[position]
                    if (selectedResolution != newResolution) {
                        selectedResolution = resolutions[position]
                        bindCameraUserCases()
                    }
                    Log.e(
                        "CameraResolutionsTAG",
                        "onItemSelectedListener selectedResolution: $selectedResolution"
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    override fun onResume() {
        super.onResume()
        orientationEventListener?.enable()
    }

    override fun onPause() {
        orientationEventListener?.disable()
        super.onPause()
    }


   /* //test
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation(): Pair<String, String> {
        if (checkMultiplePermission()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val location: Location? = task.result
                        if (location != null) {
                            val geocoder = Geocoder(this, Locale.getDefault())
                            val list: List<Address> =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                            Log.e("teete==", "" + geocoder.toString())

                            camera_binding.apply {
                                latitudeTextview.setText(
                                    "Lat: " + list[0].latitude.toString().trim()
                                )
                                longitudeTextview.setText(
                                    "Long: " + list[0].longitude.toString().trim()
                                )
                            }
                        }
                    } else {
                        Log.e("Failed==", "" + task.exception.toString())

                    }

                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        return Pair(
            camera_binding.latitudeTextview.text.toString(),
            camera_binding.latitudeTextview.text.toString()
        )
    }*/

    /*private fun getLocation() {
        if(checkMultiplePermission()){
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        val location: Location? = task.result
                        if (location != null) {
                            val geocoder = Geocoder(this, Locale.getDefault())
                            val list: List<Address> =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                            Log.e("teete==",""+geocoder.toString())

                            camera_binding.apply {
                                latitudeTextview.setText("Lat: "+list[0].latitude.toString().trim())
                                longitudeTextview.setText("Long: "+list[0].longitude.toString().trim())
                            }
                        }
                    }else{
                        Log.e("Failed==",""+task.exception.toString())

                    }

                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }

    }*/
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    /*private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }*/
    /*@SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }*/
    //test
}