package com.trucksup.field_officer.presenter.view.activity.miscellaneous

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.AddMiscLayoutBinding
import com.trucksup.field_officer.databinding.DateFilterBinding
import com.trucksup.field_officer.databinding.IncompleteDropItemBinding
import com.trucksup.field_officer.databinding.MiscActivityBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.utils.NetworkManager
import com.trucksup.field_officer.presenter.view.adapter.CompleteLead
import com.trucksup.field_officer.presenter.view.adapter.ICImageAdapter
import com.trucksup.field_officer.presenter.view.adapter.ImageAdapter
import com.trucksup.field_officer.presenter.view.interfaces.AddMiscInterface
import com.trucksup.fieldofficer.adapter.IncompleteLead
import com.trucksup.field_officer.presenter.common.dialog.DialogBoxes
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginActivity
import com.trucksup.field_officer.presenter.view.activity.auth.login.LoginViewModel
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.AddMiscLeadRequest
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.GetAllMiscLeadResponse
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.GetMiscLeadRequest
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.model.TrucksImageXML
import com.trucksup.field_officer.presenter.view.activity.miscellaneous.vml.MiscellaneousViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.regex.Pattern

@AndroidEntryPoint
class MiscActivity : BaseActivity(), AddMiscInterface, TrucksFOImageController {

    private lateinit var binding: MiscActivityBinding
    private var mViewModel: MiscellaneousViewModel? = null
    private var addMiscLayoutBinding: AddMiscLayoutBinding?=null
    private var imageList = ArrayList<TrucksImageXML>()
    private var bottomDialog: BottomSheetDialog?=null

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MiscActivityBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[MiscellaneousViewModel::class.java]

        setupObserver()
        setListener()
        cameraLauncher()

        getMiscLeads()
    }

    private fun getMiscLeads()
    {
        showProgressDialog(this, false)
        var request=GetMiscLeadRequest(PreferenceManager.getUserData(this@MiscActivity)?.boUserid?.toInt()?:0,PreferenceManager.getServerDateUtc(),PreferenceManager.getRequestNo().toInt(),PreferenceManager.getPhoneNo(this@MiscActivity))
        mViewModel?.getAllMiscellaneous(PreferenceManager.getAuthToken(),request)
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
            bottomDialog?.dismiss()
            bottomDialog=null
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

    private fun setIncompleteLead(list:ArrayList<GetAllMiscLeadResponse.IncompletedLead>) {
        binding.rvd1.apply {
            layoutManager = LinearLayoutManager(this@MiscActivity, RecyclerView.VERTICAL, false)
            adapter = IncompleteLead(this@MiscActivity, list)
            hasFixedSize()
        }
    }

    private fun setCompleteLead(list:ArrayList<GetAllMiscLeadResponse.IncompletedLead>) {
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

    private fun setupObserver() {
        //add misc
        mViewModel?.addMiscellaneousResultLD?.observe(this@MiscActivity) { responseModel ->
            if (responseModel.serverError != null) {
                bottomDialog?.dismiss()
                bottomDialog=null
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@MiscActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                bottomDialog?.dismiss()
                bottomDialog=null
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {
                    val abx = AlertBoxDialog(
                        this@MiscActivity, responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()

                } else {
                    val abx = AlertBoxDialog(
                        this@MiscActivity, responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }

            }
        }

        //get misc leads
        mViewModel?.getAllMiscResultLD?.observe(this@MiscActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@MiscActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {
                    var completeLeads=responseModel.success.completedLeads
                    var inCompleteLeads=responseModel.success.incompletedLeads
                    if (completeLeads.isNullOrEmpty() && inCompleteLeads.isNullOrEmpty())
                    {

                    }
                    else {
                        if (!inCompleteLeads.isNullOrEmpty()) {
                            setIncompleteLead(inCompleteLeads)
                        }
                        if (!completeLeads.isNullOrEmpty())
                        {
                            setCompleteLead(completeLeads)
                        }
                    }
                } else {
                    val abx =
                        AlertBoxDialog(
                            this@MiscActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }

        //update misc
        mViewModel?.updateMiscLeadsResultLD?.observe(this@MiscActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@MiscActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statuscode == 200) {
                    //setItemList(responseModel.success)
                } else {
                    val abx =
                        AlertBoxDialog(
                            this@MiscActivity,
                            responseModel.success?.message.toString(),
                            "m"
                        )
                    abx.show()
                }
            }
        }
    }

    //add by me
//    private fun cameraListener() {
//        launcher = registerForActivityResult<Intent, ActivityResult>(
//            ActivityResultContracts.StartActivityForResult()
//        ) { result: ActivityResult ->
//            if (result.resultCode == RESULT_OK) {
//                val data = result.data
//
//                try {
//                    val imageUris: Uri = data!!.getStringExtra("result")!!.toUri()
//                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
//                        getContentResolver(),
//                        Uri.parse(imageUris.toString())
//                    )
//                    // Set the image in imageview for display
//                    handleImageCapture(bitmap)
//                } catch (ex: Exception) {
//                    ex.printStackTrace()
//                }
//            }
//        }
//    }

    private fun cameraLauncher() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    val imageUris: Uri = data!!.getStringExtra("result")!!.toUri()
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(),
                        Uri.parse(imageUris.toString())
                    )
                    // Set the image in imageview for display
                    val newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500)
                    val newFile: File = FileHelp().bitmapTofile(newBitmap, this)
                    uploadImage(newFile, "")

                    //handleImageCapture(bitmap)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    private fun launchCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 1)
        intent.putExtra("focusView", false)
        launcher!!.launch(intent)
    }

    override fun addMisLayout(v: AddMiscLayoutBinding,dialog: BottomSheetDialog) {
        bottomDialog=dialog
        addMiscLayoutBinding=null
        imageList.clear()
        addMiscLayoutBinding = v

        var categoryType = ArrayList<String>()
        categoryType.add("Truck Supplier")
        categoryType.add("Business Associate")
        val arrayAdapter = ArrayAdapter(this, R.layout.simple_text_item, categoryType)
        addMiscLayoutBinding?.categorySpinner?.setAdapter(arrayAdapter)
        addMiscLayoutBinding?.categorySpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val textView: TextView = view as TextView
//                  textView.setPadding(0,0,0,0)
                val typeface = getFont(this@MiscActivity, R.font.bai_jamjuree_medium)
                textView.setTypeface(typeface)
                textView.setTextColor(getColor(R.color.text_grey))
                textView.setTextSize(13f)

                if (addMiscLayoutBinding?.categorySpinner?.selectedItem.toString().lowercase()=="truck supplier")
                {
                    addMiscLayoutBinding?.etTruckNo?.text?.clear()
                    addMiscLayoutBinding?.etBusinessName?.text?.clear()
                    addMiscLayoutBinding?.businessNameLay?.visibility=View.GONE
                    addMiscLayoutBinding?.truckNoLay?.visibility=View.VISIBLE
                    addMiscLayoutBinding?.tvUploadImage?.text=getString(R.string.upload_truck_image)
                }
                else if (addMiscLayoutBinding?.categorySpinner?.selectedItem.toString().lowercase()=="business associate")
                {
                    addMiscLayoutBinding?.etTruckNo?.text?.clear()
                    addMiscLayoutBinding?.etBusinessName?.text?.clear()
                    addMiscLayoutBinding?.truckNoLay?.visibility=View.GONE
                    addMiscLayoutBinding?.businessNameLay?.visibility=View.VISIBLE
                    addMiscLayoutBinding?.tvUploadImage?.text=getString(R.string.upload_image)
                }

                visibilityHandle()
                addMiscLayoutBinding?.errorMsgLay?.visibility=View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        var list = ArrayList<TrucksImageXML>()
        addMiscLayoutBinding?.rvImage?.apply {
            layoutManager = LinearLayoutManager(this@MiscActivity, RecyclerView.HORIZONTAL, false)
            this.adapter = ImageAdapter(context, list)
            hasFixedSize()
        }

        //business name
        addMiscLayoutBinding?.etBusinessName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                visibilityHandle()
                addMiscLayoutBinding?.errorMsgLay?.visibility=View.GONE
            }
        })

        //truck number
        addMiscLayoutBinding?.etTruckNo?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                visibilityHandle()
                addMiscLayoutBinding?.errorMsgLay?.visibility=View.GONE
            }
        })

        //mobile number
        addMiscLayoutBinding?.etMobileNumber?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                visibilityHandle()
                addMiscLayoutBinding?.errorMsgLay?.visibility=View.GONE
            }
        })

        //name
        addMiscLayoutBinding?.etName?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                visibilityHandle()
                addMiscLayoutBinding?.errorMsgLay?.visibility=View.GONE
            }
        })

        //add image
        addMiscLayoutBinding?.btnAddImage?.setOnClickListener {
            addMiscLayoutBinding?.errorMsgLay?.visibility=View.GONE
            visibilityHandle()
            launchCamera()
        }

        //submit button
        addMiscLayoutBinding?.btnSubmit?.setOnClickListener {
            checkValidation()
//            dialog.dismiss()
        }

        //cancel button
        addMiscLayoutBinding?.btnCancel?.setOnClickListener {
            dialog.dismiss()
        }

        //save as draft button
        addMiscLayoutBinding?.btnSaveAsDraft?.setOnClickListener {
            checkValidation()
//            dialog.dismiss()
        }

        //close error msg
        addMiscLayoutBinding?.btnErrorOk?.setOnClickListener {
            addMiscLayoutBinding?.errorMsgLay?.visibility=View.GONE
        }
    }

//    /*val startForResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//            try {
//                val photo = result.data?.extras?.get("data") as Bitmap?
//                // Set the image in imageview for display
//                handleImageCapture(photo!!)
//            } catch (e: Exception) {
//            }
//        }*/

//    private fun handleImageCapture(bitmap: Bitmap) {
//        try {
//            bitmap?.let {
//                val resizedBitmap = resizeBitmap(it, 800)
//                uploadBitmap(resizedBitmap)
//            }
//        } catch (e: Exception) {
//            Log.e("ImageCapture", "Error handling image capture", e)
//        }
//    }

//    private fun resizeBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
//        var width = bitmap.width
//        var height = bitmap.height
//
//        val bitmapRatio = width.toFloat() / height.toFloat()
//        if (bitmapRatio > 1) {
//            width = maxSize
//            height = (width / bitmapRatio).toInt()
//        } else {
//            height = maxSize
//            width = (height * bitmapRatio).toInt()
//        }
//        return Bitmap.createScaledBitmap(bitmap, width, height, true)
//    }

//    private fun uploadBitmap(bitmap: Bitmap) {
//        val file = bitmapToFile(bitmap)
//        if (file != null) {
//            imageList.add(bitmap)
//            if (imageList.isNullOrEmpty()) {
//                addMiscLayoutBinding.rvImage.visibility = View.GONE
//                addMiscLayoutBinding.placeHolderImages.visibility = View.VISIBLE
//            } else {
//                addMiscLayoutBinding.rvImage.visibility = View.VISIBLE
//                addMiscLayoutBinding.placeHolderImages.visibility = View.GONE
//
//                addMiscLayoutBinding.rvImage.apply {
//                    layoutManager =
//                        LinearLayoutManager(this@MiscActivity, RecyclerView.HORIZONTAL, false)
//                    adapter = ImageAdapter(this@MiscActivity, imageList)
//                    hasFixedSize()
//                }
//            }
//        }
//    }

//    private fun bitmapToFile(bitmap: Bitmap): File {
//        val file =
//            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "temp_image.jpg")
//        try {
//            val outputStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            outputStream.flush()
//            outputStream.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return file
//    }

    override fun getImage(valuekey: String, url: String) {
        dismissProgressDialog()
        imageList.add(TrucksImageXML(valuekey,url))
        if (imageList.isNullOrEmpty()) {
            addMiscLayoutBinding?.rvImage?.visibility = View.GONE
            addMiscLayoutBinding?.placeHolderImages?.visibility = View.VISIBLE
        } else {
            addMiscLayoutBinding?.rvImage?.visibility = View.VISIBLE
            addMiscLayoutBinding?.placeHolderImages?.visibility = View.GONE

            addMiscLayoutBinding?.rvImage?.apply {
                layoutManager =
                    LinearLayoutManager(this@MiscActivity, RecyclerView.HORIZONTAL, false)
                adapter = ImageAdapter(this@MiscActivity, imageList)
                hasFixedSize()
            }
        }

        visibilityHandle()

        /*  try {
              Glide.with(this)
                  .load(url)
                  .into(binding?.profileImage!!)
          } catch (e: Exception) {
          }
          binding?.profileImage?.tag = "y"

          frontImgKey = valuekey*/
    }

    override fun imageError(error: String) {
        dismissProgressDialog()
        visibilityHandle()
        //  LoggerMessage.onSNACK(binding?.profileImage!!, error, this)
    }

    override fun dataSubmitted(message: String) {
    }

    fun uploadImage(file: File, token: String) {
        showProgressDialog(this, false)
        mViewModel?.uploadImages(
            PreferenceManager.getAuthToken(),
            "image",
            PreferenceManager.prepareFilePartTrucksHum(file!!, "imageFile"),
            PreferenceManager.prepareFilePartTrucksHum(file!!, "watermarkFile"),
            this
        )
    }

    private fun visibilityHandle() {
        if (addMiscLayoutBinding?.categorySpinner?.selectedItem.toString().lowercase() != "truck supplier" && addMiscLayoutBinding?.categorySpinner?.selectedItem.toString().lowercase() != "business associate")
        {
            addMiscLayoutBinding?.cancelSaveAsDraftLay?.visibility=View.VISIBLE
            addMiscLayoutBinding?.btnSubmit?.visibility=View.GONE
        }
        else if (addMiscLayoutBinding?.categorySpinner?.selectedItem.toString().lowercase() == "business associate" && addMiscLayoutBinding?.etBusinessName?.text.toString().isNullOrEmpty())
        {
            addMiscLayoutBinding?.cancelSaveAsDraftLay?.visibility=View.VISIBLE
            addMiscLayoutBinding?.btnSubmit?.visibility=View.GONE
        }
        else if (addMiscLayoutBinding?.categorySpinner?.selectedItem.toString().lowercase() == "truck supplier" && addMiscLayoutBinding?.etTruckNo?.text.toString().isNullOrEmpty())
        {
            addMiscLayoutBinding?.cancelSaveAsDraftLay?.visibility=View.VISIBLE
            addMiscLayoutBinding?.btnSubmit?.visibility=View.GONE
        }
        else if (addMiscLayoutBinding?.etMobileNumber?.text.toString().isNullOrEmpty())
        {
            addMiscLayoutBinding?.cancelSaveAsDraftLay?.visibility=View.VISIBLE
            addMiscLayoutBinding?.btnSubmit?.visibility=View.GONE
        }
        else if (addMiscLayoutBinding?.etName?.text.toString().isNullOrEmpty())
        {
            addMiscLayoutBinding?.cancelSaveAsDraftLay?.visibility=View.VISIBLE
            addMiscLayoutBinding?.btnSubmit?.visibility=View.GONE
        }
        else if (imageList.isNullOrEmpty())
        {
            addMiscLayoutBinding?.cancelSaveAsDraftLay?.visibility=View.VISIBLE
            addMiscLayoutBinding?.btnSubmit?.visibility=View.GONE
        }
        else
        {
            addMiscLayoutBinding?.cancelSaveAsDraftLay?.visibility=View.GONE
            addMiscLayoutBinding?.btnSubmit?.visibility=View.VISIBLE
        }
    }

    private fun checkValidation() {
        addMiscLayoutBinding?.errorMsgLay?.visibility=View.GONE
        if (isOnline(this)) {
            //business name
            if (!TextUtils.isEmpty(addMiscLayoutBinding?.etBusinessName?.text.toString().trim())) {
                if (isValidName(addMiscLayoutBinding?.etBusinessName?.text.toString().trim())) {
                    addMiscLayoutBinding?.errorMsgLay?.visibility=View.VISIBLE
                    addMiscLayoutBinding?.tvErrormsg?.text=getString(R.string.business_valid_name)
                    return
                }
                if (getSpecialCharacterCount(addMiscLayoutBinding?.etBusinessName?.text.toString()) == 0) {
                    addMiscLayoutBinding?.errorMsgLay?.visibility=View.VISIBLE
                    addMiscLayoutBinding?.tvErrormsg?.text=getString(R.string.business_valid_name)
                    return
                }
            }

            //truck number
            if (!TextUtils.isEmpty(addMiscLayoutBinding?.etTruckNo?.text.toString().trim())) {
                if (checkVehicleNumber(addMiscLayoutBinding?.etTruckNo?.text.toString()) == false) {
                    addMiscLayoutBinding?.errorMsgLay?.visibility=View.VISIBLE
                    addMiscLayoutBinding?.tvErrormsg?.text=getString(R.string.enter_truck)
                    return
                }
            }

            //mobile number
            if (TextUtils.isEmpty(addMiscLayoutBinding?.etMobileNumber?.text.toString().trim())) {
                addMiscLayoutBinding?.errorMsgLay?.visibility=View.VISIBLE
                addMiscLayoutBinding?.tvErrormsg?.text=getString(R.string.enter_mobile_number)
                return
            }

            //mobile number
            if (!isValidMobile(addMiscLayoutBinding?.etMobileNumber?.text.toString())) {
                addMiscLayoutBinding?.errorMsgLay?.visibility=View.VISIBLE
                addMiscLayoutBinding?.tvErrormsg?.text=getString(R.string.mobile_no_validation)
                return
            }

            //name
            if (!TextUtils.isEmpty(addMiscLayoutBinding?.etName?.text.toString().trim())) {
                if (isValidName(addMiscLayoutBinding?.etName?.text.toString().trim())) {
                    addMiscLayoutBinding?.errorMsgLay?.visibility=View.VISIBLE
                    addMiscLayoutBinding?.tvErrormsg?.text=getString(R.string.valid_name)
                    return
                }
                if (getSpecialCharacterCount(addMiscLayoutBinding?.etName?.text.toString()) == 0) {
                    addMiscLayoutBinding?.errorMsgLay?.visibility=View.VISIBLE
                    addMiscLayoutBinding?.tvErrormsg?.text=getString(R.string.valid_name)
                    return
                }
            }

            addMiscLayoutBinding?.errorMsgLay?.visibility=View.GONE
            showProgressDialog(this, false)
            var request= AddMiscLeadRequest(
                actionType = "Add",
                address="",
                businessName = addMiscLayoutBinding?.etBusinessName?.text.toString(),
                category = addMiscLayoutBinding?.categorySpinner?.selectedItem.toString(),
                createdBy = PreferenceManager.getPhoneNo(this@MiscActivity),
                mobileNo=addMiscLayoutBinding?.etMobileNumber?.text.toString(),
                name=addMiscLayoutBinding?.etName?.text.toString(),
                requestDatetime=PreferenceManager.getServerDateUtc(),
                requestId=PreferenceManager.getRequestNo(),
                requestedBy = PreferenceManager.getPhoneNo(this@MiscActivity),
                truckNumber=addMiscLayoutBinding?.etTruckNo?.text.toString(),
                trucksImageXML=imageList,
                userId = PreferenceManager.getUserData(this)?.boUserid?.toInt()?:0
            )
            mViewModel?.addMiscellaneous(PreferenceManager.getAuthToken(),request)

        }
        else
        {
            addMiscLayoutBinding?.errorMsgLay?.visibility=View.VISIBLE
            addMiscLayoutBinding?.tvErrormsg?.text=getString(R.string.no_internet)
        }
    }

    private fun isValidName(phone: String): Boolean {

        val p = Pattern.compile("[_0-9](.*)")
        val m = p.matcher(phone)
        return (m.find() && m.group() == phone)
    }

    private fun checkVehicleNumber(vehicleNumber: String): Boolean {
        val regex = "^[A-Z]{2}[ -]?[0-9|A-Z]{2}[ -]?[A-Z]{1,2}[ -]?[0-9]{4}$"
        return vehicleNumber.matches(regex.toRegex())
    }

}