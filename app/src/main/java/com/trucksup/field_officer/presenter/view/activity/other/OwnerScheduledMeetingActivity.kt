package com.trucksup.field_officer.presenter.view.activity.other

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.data.model.FromToModel
import com.trucksup.field_officer.databinding.ActivityOwnerScheduledMeetingBinding
import com.trucksup.field_officer.databinding.AddNewTruckLayoutBinding
import com.trucksup.field_officer.databinding.PreferredLaneDialogBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.view.adapter.TrucksDetailsAdap
import com.trucksup.fieldofficer.adapter.PreferredLaneAdap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.collections.ArrayList

class OwnerScheduledMeetingActivity : AppCompatActivity(), PreferredLaneAdap.ControllerListener,
    TrucksDetailsAdap.ControllerListener {

    private lateinit var binding: ActivityOwnerScheduledMeetingBinding
    private var preferredLaneList = ArrayList<FromToModel>()
    private var trucksDetailsList = ArrayList<String>()
    private var photo1:Boolean=false
    private var photo2:Boolean=false

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityOwnerScheduledMeetingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        setListener()
        camera()
    }

    private fun setListener() {
        //back button
        binding.ivBack.setOnClickListener {
            finish()
        }

        //add preferred lane
        binding.btnPreferredLane.setOnClickListener {
            addPreferredLane(this)
        }

        //add truck details
        binding.btnTrucksDetails.setOnClickListener {
            addNewTruckDialog()
        }



        //selfie pic image
        binding.selfiPic.setOnClickListener {
            photo1=true
            photo2=false
            launchCamera()
            /*val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForResult.launch(camera_intent)*/
        }

        //office pic image
        binding.officePic.setOnClickListener {
            photo2=true
            photo1=false
            /*val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForResult.launch(camera_intent)*/
        }

        //submit button
        binding.btnSubmit.setOnClickListener {
            finish()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    //add by me
    private fun camera() {
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
    private fun launchCamera(){
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", false)
        intent.putExtra("cameraOpen", 0)
        intent.putExtra("focusView", true)
        launcher!!.launch(intent)
    }
    //add by me

    /*val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
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

    private fun addPreferredLane(context: Context) {
        val builder = AlertDialog.Builder(context)
        val binding = PreferredLaneDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //submit button
        binding.btnSubmit.setOnClickListener {
            preferredLaneList.add(
                FromToModel(
                    binding.etFrom.getText().toString(),
                    binding.etTo.getText().toString()
                )
            )
            setRvPreferredLane()
            dialog.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setRvPreferredLane() {
        binding.rvPreferredLane.apply {
            layoutManager = LinearLayoutManager(this@OwnerScheduledMeetingActivity, RecyclerView.VERTICAL, false)
            adapter = PreferredLaneAdap(this@OwnerScheduledMeetingActivity, preferredLaneList,this@OwnerScheduledMeetingActivity)
            hasFixedSize()
        }
    }

    private fun setRvTruckDetails() {
        binding.rvTrucksDetails.apply {
            layoutManager = LinearLayoutManager(this@OwnerScheduledMeetingActivity, RecyclerView.VERTICAL, false)
            adapter = TrucksDetailsAdap(this@OwnerScheduledMeetingActivity, trucksDetailsList,this@OwnerScheduledMeetingActivity)
            hasFixedSize()
        }
    }

    private fun adjustFontScale(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }

    override fun onDelete(position: Int) {
        preferredLaneList.removeAt(position)
        setRvPreferredLane()
    }

    override fun onDeleteTruck(position: Int) {
        trucksDetailsList.removeAt(position)
        setRvTruckDetails()
    }

    private fun addNewTruckDialog() {
        val builder = AlertDialog.Builder(this)
        val binding= AddNewTruckLayoutBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //activate button
        binding.btnAddTruck.setOnClickListener {
            trucksDetailsList.add("")
            setRvTruckDetails()
            dialog.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}