package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGpPersonalDetailUpdateBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.growthPartner.model.GPOnboardingData
import com.trucksup.field_officer.presenter.view.activity.growthPartner.vml.GPOnboardingVM
import com.trucksup.field_officer.presenter.view.activity.growth_partner.GPKYCActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.regex.Pattern


@AndroidEntryPoint
class GPPersonalDetailUpdateActivity : BaseActivity(), TrucksFOImageController,
    View.OnClickListener {
    private var launcher: ActivityResultLauncher<Intent>? = null
    private lateinit var binding: ActivityGpPersonalDetailUpdateBinding
    private var onboardViewModel: GPOnboardingVM? = null
    val businessTypeList = listOf(
        "Select",
        "Brokers",
        "Tyre Distributors",
        "Lubricant Distributors",
        "Auto Spare Parts Shops",
        "Body Builders/ Body Painters",
        "RTO Agents/ Bank Agents",
        "Insurance Agent",
        "Bank DSN / Finance Agent / Insurance Agent"
    )
    var selectedbusinessType: String = ""
    private var ImgKey: String? = ""
    private var ImgKeyUrl: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gp_personal_detail_update)
        val view = binding.root
        setContentView(view)
        onboardViewModel = ViewModelProvider(this)[GPOnboardingVM::class.java]


        setOnClicks()
        cameraLauncher()

        //test
        val gson = Gson()
        val json = intent.getStringExtra("userDataJson")
        val gpOnboardingData = gson.fromJson(json, GPOnboardingData::class.java)
        //set Text
        binding.ETSalesCode.setText(gpOnboardingData.SalesCodeofBO)
        binding.ETGPMobileNumber.setText(gpOnboardingData.GPMobileNumber)
        binding.ETGPName.setText(gpOnboardingData.GPName)
        binding.ETGpBusinessName.setText(gpOnboardingData.BusinessName)
        selectedbusinessType = gpOnboardingData.BusinessType
        binding.ETGpPincode.setText(gpOnboardingData.Pincode)
        //set Text



        //test
        binding.ETSalesCode.setText(PreferenceManager.getUserData(this)?.referralcode)
        val categoryAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, businessTypeList)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.businessTypeSpinner.adapter = categoryAdapter

        binding.businessTypeSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    selectedbusinessType = businessTypeList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Optional: Handle case when nothing is selected
                }
            }
        //test
    }


    private fun cameraLauncher() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    val imageUris: Uri = data!!.getStringExtra("result")!!.toUri()
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver,
                        Uri.parse(imageUris.toString())
                    )
                    // Set the image in imageview for display
                    val newBitmap: Bitmap = FileHelp().resizeImage(bitmap, 500)
                    val newFile: File = FileHelp().bitmapTofile(newBitmap, this)
                    uploadImage(newFile)

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }
    private fun launchCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 0)
        launcher?.launch(intent)
    }

    fun uploadImage(file: File) {
        showProgressDialog(this, false)

        onboardViewModel?.ImageUploadOnboard(
            PreferenceManager.getAuthToken(),
            "image",
            "GrowthPartner",
            PreferenceManager.prepareFilePartTrucksHum(file, "imageFile"),
            PreferenceManager.prepareFilePartTrucksHum(file, "watermarkFile"),
            this
        )
    }

    private fun setupObserver() {

    }


    fun CheckValidation() {
        if (ImgKey.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.cvCamera,
                resources.getString(R.string.PleaseSelectStorePhoto),
                this
            )
        } else if (binding.ETSalesCode.text.isEmpty()) {
            binding.ETSalesCode.requestFocus()
            binding.ETSalesCode.setError(getString(R.string.PleaseenterSalesCode))
        } else if (binding.ETGPMobileNumber.text.toString().isEmpty()) {
            binding.ETGPMobileNumber.requestFocus()
            binding.ETGPMobileNumber.setError(getString(R.string.PleaseenterGPMobile))
        } else if (binding.ETGPName.text.isEmpty()) {
            binding.ETGPName.requestFocus()
            binding.ETGPName.setError(getString(R.string.PleaseenterGPName))
        } else if (binding.ETGpBusinessName.text.isEmpty()) {
            binding.ETGpBusinessName.requestFocus()
            binding.ETGpBusinessName.setError(getString(R.string.PleaseEnterBusinessName))
        } else if (selectedbusinessType.isEmpty()) {
            LoggerMessage.onSNACK(
                binding.businessTypeSpinner,
                getString(R.string.input_business_type),
                this
            )
        } else if (binding.ETGpPincode.text.isEmpty()) {
            binding.ETGpPincode.requestFocus()
            binding.ETGpPincode.setError(getString(R.string.PleaseEnterBusinessPincode))
        } else if (binding.ETGpPincode.text.length < 6) {
            binding.ETGpPincode.requestFocus()
            binding.ETGpPincode.setError(getString(R.string.PleaseEnterRightPincode))
        } else if (binding.ETGpBusinessAddress.text.isEmpty()) {
            binding.ETGpBusinessAddress.requestFocus()
            binding.ETGpBusinessAddress.setError(getString(R.string.PleaseBusinessAddress))
        } else {

            //test
            val gson = Gson()
            val json = intent.getStringExtra("userDataJson")
            val gpOnboardingData = gson.fromJson(json, GPOnboardingData::class.java)


            val updatedUser = gpOnboardingData.copy(
                ProfilePhoto = ImgKey!!,
                ProfilePhotoURL = ImgKeyUrl.toString(),
                SalesCodeofBO = binding.ETSalesCode.text.toString(),
                GPMobileNumber = binding.ETGPMobileNumber.text.toString(),
                GPName = binding.ETGPName.text.toString(),
                BusinessName = binding.ETGpBusinessName.text.toString(),
                BusinessType = selectedbusinessType,
                Pincode = binding.ETGpPincode.text.toString(),
                City = binding.ETSalesCode.text.toString(),
                State = binding.ETSalesCode.text.toString()
            )

            val updatedJson = gson.toJson(updatedUser)

            //val intent = Intent(this, GpOnboardingPreviewActivity::class.java)
            val intent = Intent(this, GPOnBoardStoreProofActivity::class.java)
            intent.putExtra("userDataJson", updatedJson)
            startActivity(intent)
            //test


            //startActivity(Intent(this, GPKYCActivity::class.java))
            //binding.ETPanNumberNOB.requestFocus()
            /*val customErrorDrawable = resources.getDrawable(com.trucksup.fieldofficer.R.drawable.ic_phone)
            customErrorDrawable.setBounds(
                0,
                0,
                customErrorDrawable.intrinsicWidth,
                customErrorDrawable.intrinsicHeight
            )
            binding.ETPanNumberNOB?.setError("Enter Right PAN No" , customErrorDrawable)*/
            /*binding.ETPanNumberNOB?.setError("Enter Right PAN No")
            binding.ETPanNumberNOB?.setText("")*/
        }

    }

    private fun setOnClicks() {
        binding.btnContinue!!.setOnClickListener(this)
        binding.cvCamera!!.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        val item_id = v?.id
        when (item_id) {
            R.id.btnContinue -> CheckValidation()
            R.id.cvCamera -> launchCamera()
        }
    }

    private fun isValidMobiles(phone: String): Boolean {
        return if (!Pattern.matches("[a-zA-Z]+", phone)) {
            Toast.makeText(this, "HIBB", Toast.LENGTH_SHORT).show()
            phone.length > 6 && phone.length <= 10
        } else {
            Toast.makeText(this, "HIAA", Toast.LENGTH_SHORT).show()
            false
        }
    }

    override fun getImage(valuekey: String, url: String) {
        dismissProgressDialog()
        try {
            Glide.with(this)
                .load(url)
                .into(binding?.profileImage!!)
        } catch (e: Exception) {
        }
        binding?.profileImage?.tag = "y"

        ImgKey = valuekey
        ImgKeyUrl = url
    }

    override fun dataSubmitted(message: String) {
    }

    override fun imageError(error: String) {
        dismissProgressDialog()
        LoggerMessage.onSNACK(binding.profileImage, error, this)
    }

}