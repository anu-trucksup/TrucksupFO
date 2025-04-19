package com.trucksup.field_officer.presenter.view.activity.truckSupplier

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.logistics.trucksup.activities.preferre.modle.Preflane
import com.logistics.trucksup.activities.preferre.modle.TrucksDetail
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.FromToModel
import com.trucksup.field_officer.databinding.ActivityOwnerScheduledMeetingBinding
import com.trucksup.field_officer.databinding.AddNewTruckLayoutBinding
import com.trucksup.field_officer.databinding.PreferredLaneDialogBinding
import com.trucksup.field_officer.presenter.cityPicker.CityPicker
import com.trucksup.field_officer.presenter.cityPicker.CityStateDialog
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.add_truck.AddTruckInterface
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.TSOnboard2ViewModel
import com.trucksup.field_officer.presenter.view.adapter.TrucksDetailsAdap
import com.trucksup.field_officer.presenter.view.adapter.PreferredLaneAdap
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.collections.ArrayList
@AndroidEntryPoint
class TSScheduledMeetingActivity : BaseActivity(), CityPicker, AddTruckInterface, PreferredLaneAdap.ControllerListener,
    TrucksDetailsAdap.ControllerListener {

    private lateinit var binding: ActivityOwnerScheduledMeetingBinding
    private var preferredLaneList = ArrayList<FromToModel>()
    private var trucksDetailsList = ArrayList<TrucksDetail>()
    private var photo1: Boolean = false
    private var photo2: Boolean = false
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var isfromCity: Boolean = true
    private lateinit var PreferredLaneBindings: PreferredLaneDialogBinding
    private lateinit var PreferredLaneDialog: AlertDialog
    private var mViewModel: TSOnboard2ViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerScheduledMeetingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        PreferredLaneBindings = PreferredLaneDialogBinding.inflate(layoutInflater)
        mViewModel = ViewModelProvider(this)[TSOnboard2ViewModel::class.java]

        setListener()
        cameraListener()
        setupObserver()
    }

    private fun setListener() {
        //back button
        binding.ivBack.setOnClickListener {
           onBackPressed()
        }

        //add preferred lane
        binding.btnPreferredLane.setOnClickListener {
            //addPreferredLane(this)
            if (!TextUtils.isEmpty(binding.etFromCity.text.trim()) && !TextUtils.isEmpty(binding.etToCity.text.trim())) {
                preferredLaneList.add(
                    FromToModel(
                        binding.etFromCity.getText().toString(),
                        binding.etToCity.getText().toString()
                    )
                )
                setRvPreferredLane()
            }
        }

        binding.etFromCity.setOnClickListener {
            isfromCity = true
            val cityDialog = CityStateDialog(this, this, "cs", binding.etFromCity, "M", true)
            cityDialog.show()
            //binding.vehicalNo.clearFocus()
        }

        binding.etToCity.setOnClickListener {
            isfromCity = false
            val cityDialog = CityStateDialog(this, this, "cs", binding.etToCity, "M", true)
            cityDialog.show()
            //binding.vehicalNo.clearFocus()
        }


        //add truck details
        /*binding.btnTrucksDetails.setOnClickListener {
            addNewTruckDialog()
        }*/
        binding.btnAddTrucksDetails.setOnClickListener {

            binding.vehicalNo.clearFocus()
            if (TextUtils.isEmpty(binding.vehicalNo.text)) {
                binding.vehicalNo.error = getString(R.string.enter_truckno)
                return@setOnClickListener
            }
            if (getSpecialCharacterCount(binding.vehicalNo.text.toString()) == 0) {
                binding.vehicalNo.error = getString(R.string.enter_truckno)
            }

            showProgressDialog(this, false)
            verifyTruck()
            //addNewTruckDialog()
        }

        //selfie pic image
        binding.selfiPic.setOnClickListener {
            photo1 = true
            photo2 = false
            launchCamera(false, 0, true)
          /*  val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForResult.launch(camera_intent)*/
        }

        //office pic image
        binding.officePic.setOnClickListener {
            photo2 = true
            photo1 = false
            launchCamera(true, 1, false)
         /*   val camera_intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForResult.launch(camera_intent)*/
        }

        //submit button
        binding.btnSubmit.setOnClickListener {
            startActivity(Intent(this, TSFollowupActivity::class.java))
        }

        //cancel button
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    //add by me
    private fun cameraListener() {
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
    private fun launchCamera(flipCamera: Boolean, cameraOpen: Int, focusView: Boolean){
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", flipCamera)
        intent.putExtra("cameraOpen", cameraOpen)
        intent.putExtra("focusView", focusView)
        launcher!!.launch(intent)
    }
    //add by me

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
            if (photo1 == true) {
                binding.selfiPic.setImageBitmap(bitmap)
                photo1 = false
                photo2 = false
            }
            if (photo2 == true) {
                binding.officePic.setImageBitmap(bitmap)
                photo1 = false
                photo2 = false
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
        /*val builder = AlertDialog.Builder(context)
        val binding = PreferredLaneDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)*/

        PreferredLaneDialog = AlertDialog.Builder(this)
            .setView(PreferredLaneBindings.root)
            .setCancelable(true)
            .setPositiveButton("OK", null)
            .create()
        PreferredLaneDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        //text

        PreferredLaneBindings.etFrom.setOnClickListener {
            isfromCity = true
            val cityDialog = CityStateDialog(this, this, "cs", PreferredLaneBindings.etFrom, "M", true)
            cityDialog.show()
            //binding.vehicalNo.clearFocus()
        }
        PreferredLaneBindings.etTo.setOnClickListener {
            isfromCity = false
            val cityDialog = CityStateDialog(this, this, "cs", PreferredLaneBindings.etTo, "M", true)
            cityDialog.show()
            //binding.vehicalNo.clearFocus()
        }

        //text
        //submit button
        PreferredLaneBindings.btnSubmit.setOnClickListener {
            preferredLaneList.add(
                FromToModel(
                    PreferredLaneBindings.etFrom.getText().toString(),
                    PreferredLaneBindings.etTo.getText().toString()
                )
            )
            setRvPreferredLane()
            PreferredLaneDialog.dismiss()
        }

        //cancel button
        PreferredLaneBindings.btnCancel.setOnClickListener {
            PreferredLaneDialog.dismiss()
        }

        PreferredLaneDialog.show()
    }

    private fun setRvPreferredLane() {
        binding.rvPreferredLane.apply {
            layoutManager =
                LinearLayoutManager(this@TSScheduledMeetingActivity, RecyclerView.VERTICAL, false)
            adapter = PreferredLaneAdap(
                this@TSScheduledMeetingActivity,
                preferredLaneList,
                this@TSScheduledMeetingActivity
            )
            hasFixedSize()
        }
    }

    private fun setupObserver() {


        mViewModel?.resultVerifyTruckLD?.observe(this@TSScheduledMeetingActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@TSScheduledMeetingActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success != null) {
                    if (responseModel.success.statusCode == 200) {
                        val request = RcRequest(
                            "I",
                            PreferenceManager.getPhoneNo(this),
                            binding.vehicalNo.text.toString()
                        )
                        mViewModel?.getRcDetails(PreferenceManager.getAuthToken(), request)
                    } else {
                        val abx = AlertBoxDialog(
                            this@TSScheduledMeetingActivity,
                            responseModel.success.message,
                            "m"
                        )
                        abx.show()
                    }
                } else {
                    val abx = AlertBoxDialog(
                        this@TSScheduledMeetingActivity,
                        "No data found",
                        "m"
                    )
                    abx.show()
                }
            }
        }

    }

    private fun verifyTruck() {
        mViewModel?.verifyTruckDetails(
            PreferenceManager.getAuthToken(),
            binding.vehicalNo.text.toString(),
            "9479330184"
        )

    }

    private fun setRvTruckDetails() {
        binding.rvTrucksDetails.apply {
            layoutManager =
                LinearLayoutManager(this@TSScheduledMeetingActivity, RecyclerView.VERTICAL, false)
            adapter = TrucksDetailsAdap(
                this@TSScheduledMeetingActivity,
                trucksDetailsList,
                this@TSScheduledMeetingActivity
            )
            hasFixedSize()
        }
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
        val binding = AddNewTruckLayoutBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //activate button
        /*binding.btnAddTruck.setOnClickListener {
            trucksDetailsList.add("")
            setRvTruckDetails()
            dialog.dismiss()
        }*/

        //cancel button
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getSpecialCharacterCount(s: String?): Int {

        val blockCharacterSet =
            "~#^&|$%*!@/()[]-'\":;,?{}+=!$^';,?×÷<>{}€£¥₩%~`¤♡♥_|《》¡¿°•○●□■◇◆♧♣▲▼▶◀↑↓←→☆★▪"
        blockCharacterSet.toCharArray()

        for (b in blockCharacterSet) {
            if (!TextUtils.isEmpty(s) && s!!.contains(b)) {
                return 0
                break
            }
        }
        return 1
    }

    override fun setTruckDetails(vehicleDetail: TrucksDetail) {
        binding.vehicalNo.setText("")
        trucksDetailsList.add(vehicleDetail)
        setRvTruckDetails()
    }

    override fun fromCity(
        value: String,
        valueState: String,
        id: String,
        type: String,
        valueHi: String,
        valueStateHi: String
    ) {
    }

    override fun toCity(
        value: String,
        valueState: String,
        id: String,
        type: String,
        valueHi: String,
        valueStateHi: String
    ) {
    }

    override fun cityState(
        cityEng: String,
        cityHi: String,
        stateEn: String,
        stateHi: String,
        id: String,
        type: String
    ) {
        if (isfromCity) {
            binding.etFromCity.text = cityEng
            binding.etFromCity.error = null
        } else {
            binding.etToCity.text = cityEng
            binding.etToCity.error = null
        }

    }

}