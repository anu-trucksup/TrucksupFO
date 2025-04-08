package com.trucksup.field_officer.presenter.view.activity.miscellaneous

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.AddMiscLayoutBinding
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.IncompleteDropItemBinding
import com.trucksup.field_officer.databinding.MiscActivityBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.utils.NetworkManager
import com.trucksup.field_officer.presenter.view.adapter.CompleteLead
import com.trucksup.field_officer.presenter.view.adapter.ICImageAdapter
import com.trucksup.field_officer.presenter.view.adapter.ImageAdapter
import com.trucksup.field_officer.presenter.view.interfaces.AddMiscInterface
import com.trucksup.fieldofficer.adapter.IncompleteLead
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class MiscActivity : BaseActivity(), AddMiscInterface {

    private lateinit var binding: MiscActivityBinding

    private lateinit var addMiscLayoutBinding: AddMiscLayoutBinding
    private lateinit var incompleteDropItemBinding: IncompleteDropItemBinding
    private var imageList = ArrayList<Bitmap>()
    private var imageList2 = ArrayList<Bitmap>()
    private var imageType: Int = -1

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MiscActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        setIncompleteLead()
        setCompleteLead()
        setListener()
        camera()
    }

    private fun setListener() {
        //back button
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.d1.setOnClickListener {
            if (binding.rvd1.visibility == View.GONE) {
                binding.rvd1.visibility = View.VISIBLE
                binding.imgD1.setImageDrawable(resources.getDrawable(R.drawable.drop_down_more))
            } else {
                binding.rvd1.visibility = View.GONE
                binding.imgD1.setImageDrawable(resources.getDrawable(R.drawable.drop_down_less_icon))
            }
        }

        binding.d2.setOnClickListener {
            if (binding.rvd2.visibility == View.GONE) {
                binding.rvd2.visibility = View.VISIBLE
                binding.imgD2.setImageDrawable(resources.getDrawable(R.drawable.drop_down_more))
            } else {
                binding.rvd2.visibility = View.GONE
                binding.imgD2.setImageDrawable(resources.getDrawable(R.drawable.drop_down_less_icon))
            }
        }

        //date picker
        binding.imgCalender.setOnClickListener {
            dateFilterDialog()
        }

        binding.btnAddMisc.setOnClickListener {
            if (NetworkManager.isConnect(this)) {
                DialogBoxes.addMiscDisc(this, this)
            } else {
                DialogBoxes.messageDialog(
                    this,
                    "You are offline . Please check your internet connection"
                )
            }
        }
    }

    private fun setIncompleteLead() {
        val list = ArrayList<String>()
        list.add("")
        list.add("")
        binding.rvd1.apply {
            layoutManager = LinearLayoutManager(this@MiscActivity, RecyclerView.VERTICAL, false)
            adapter = IncompleteLead(this@MiscActivity, list).apply {
                setOnControllerListeners(object : IncompleteLead.OnControllerListeners {
                    override fun incompleteAddImage(incompleteDropItemBinding: IncompleteDropItemBinding) {
                        this@MiscActivity.incompleteDropItemBinding = incompleteDropItemBinding
                        imageType = 1
                        launchCamera()
                        /*val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startForResult.launch(camera_intent)*/
                    }
                })
            }
            hasFixedSize()
        }
    }

    private fun setCompleteLead() {
        var list = ArrayList<String>()
        list.add("")
        list.add("")
        binding.rvd2.apply {
            layoutManager = LinearLayoutManager(this@MiscActivity, RecyclerView.VERTICAL, false)
            adapter = CompleteLead(this@MiscActivity, list)
            hasFixedSize()
        }
    }

    private fun dateFilterDialog() {
        val builder = AlertDialog.Builder(this)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.show()

        //apply button
        binding.btnApply.setOnClickListener {
            dialog.dismiss()
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
    }


    //add by me
    fun camera() {
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
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 1)
        intent.putExtra("focusView", false)
        launcher!!.launch(intent)
    }
    //add by me


    override fun addImage(v: AddMiscLayoutBinding) {
        addMiscLayoutBinding = v
        imageType = 0
        launchCamera()
        /*val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startForResult.launch(camera_intent)*/
    }

    /*val startForResult =
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
            bitmap?.let {
                val resizedBitmap = resizeBitmap(it, 800)
                uploadBitmap(resizedBitmap)
            }
        } catch (e: Exception) {
            Log.e("ImageCapture", "Error handling image capture", e)
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

    private fun uploadBitmap(bitmap: Bitmap) {
        val file = bitmapToFile(bitmap)
        if (file != null) {
            if (imageType == 0) {
                imageList.add(bitmap)
                if (imageList.isNullOrEmpty()) {
                    addMiscLayoutBinding.rvImage.visibility = View.GONE
                    addMiscLayoutBinding.placeHolderImages.visibility = View.VISIBLE
                } else {
                    addMiscLayoutBinding.rvImage.visibility = View.VISIBLE
                    addMiscLayoutBinding.placeHolderImages.visibility = View.GONE

                    addMiscLayoutBinding.rvImage.apply {
                        layoutManager =
                            LinearLayoutManager(this@MiscActivity, RecyclerView.HORIZONTAL, false)
                        adapter = ImageAdapter(this@MiscActivity, imageList)
                        hasFixedSize()
                    }
                }
                imageType = -1
            } else {
                imageList2.add(bitmap)
                incompleteDropItemBinding.rvImage.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    adapter = ICImageAdapter(context, imageList2)
                    hasFixedSize()
                }
                imageType = -1
            }
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

    private fun bitmapToFile(bitmap: Bitmap): File? {
        val file =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg")
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