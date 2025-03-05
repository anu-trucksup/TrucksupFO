package com.trucksup.field_officer.presenter.view.fragment.ms

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.AddMiscLayoutBinding
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.FragmentMiscBinding
import com.trucksup.field_officer.databinding.IncompleteDropItemBinding
import com.trucksup.field_officer.presenter.utils.NetworkManager
import com.trucksup.field_officer.presenter.view.adapter.CompleteLead
import com.trucksup.field_officer.presenter.view.adapter.ICImageAdapter
import com.trucksup.field_officer.presenter.view.adapter.ImageAdapter
import com.trucksup.field_officer.presenter.view.interfaces.AddMiscInterface
import com.trucksup.fieldofficer.adapter.IncompleteLead
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MiscFragment : Fragment(), AddMiscInterface {

    private lateinit var binding: FragmentMiscBinding
    private var aContext: Context? = null
    private lateinit var addMiscLayoutBinding: AddMiscLayoutBinding
    private lateinit var incompleteDropItemBinding: IncompleteDropItemBinding
    private var imageList = ArrayList<Bitmap>()
    private var imageList2 = ArrayList<Bitmap>()
    private var imageType: Int = -1

//    0=add misc image
//    1=add incomplete mics image

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            aContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMiscBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setIncompleteLead()
        setCompleteLead()
        setListener()

    }

    private fun setListener() {
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
            if (NetworkManager.isConnect(aContext!!)) {
                DialogBoxes.addMiscDisc(aContext!!, this)
            }
            else
            {
                DialogBoxes.messageDialog(aContext!!,"You are offline . Please check your internet connection")
            }
        }
    }

    private fun setIncompleteLead() {
        var list = ArrayList<String>()
        list.add("")
        list.add("")
        binding.rvd1.apply {
            layoutManager = LinearLayoutManager(aContext, RecyclerView.VERTICAL, false)
            adapter = IncompleteLead(aContext!!, list).apply {
                setOnControllerListeners(object : IncompleteLead.OnControllerListeners {
                    override fun incompleteAddImage(incompleteDropItemBinding: IncompleteDropItemBinding) {
                        this@MiscFragment.incompleteDropItemBinding = incompleteDropItemBinding
                        imageType = 1
                        val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startForResult.launch(camera_intent)
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
            layoutManager = LinearLayoutManager(aContext, RecyclerView.VERTICAL, false)
            adapter = CompleteLead(aContext!!, list)
            hasFixedSize()
        }
    }

    private fun dateFilterDialog() {
        val builder = AlertDialog.Builder(aContext)
        val binding = DateFilterBinding.inflate(LayoutInflater.from(aContext))
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

    override fun addImage(v: AddMiscLayoutBinding) {
        addMiscLayoutBinding = v
        imageType = 0
        val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startForResult.launch(camera_intent)
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            try {
                val photo = result.data?.extras?.get("data") as Bitmap?
                // Set the image in imageview for display
                handleImageCapture(photo!!)
            } catch (e: Exception) {
            }
        }

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
                            LinearLayoutManager(aContext, RecyclerView.HORIZONTAL, false)
                        adapter = ImageAdapter(aContext!!, imageList)
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
            File(aContext?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg")
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