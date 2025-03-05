package com.trucksup.field_officer.presenter.view.activity.other

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.trucksup.field_officer.databinding.ActivityLoadMeetingBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class LoadMeetingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadMeetingBinding
    private var photo1:Boolean=false
    private var photo2:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadMeetingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.selfiPic.setOnClickListener {
            photo1=true
            photo2=false
            val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForResult.launch(camera_intent)
        }

        binding.officePic.setOnClickListener {
            photo2=true
            photo1=false
            val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForResult.launch(camera_intent)
        }

        binding.btnSubmit.setOnClickListener {
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            try {
                val photo = result.data?.extras?.get("data") as Bitmap?
                // Set the image in imageview for display
                handleImageCapture(photo!!)
            }
            catch (e:Exception)
            {

            }
        }

    private fun handleImageCapture(bitmap: Bitmap) {
        try {
            if (photo1==true)
            {
                binding.selfiPic.setImageBitmap(bitmap)
                photo1=false
                photo2=false
            }
            if (photo2==true)
            {
                binding.officePic.setImageBitmap(bitmap)
                photo1=false
                photo2=false
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

    private fun adjustFontScale(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }

}