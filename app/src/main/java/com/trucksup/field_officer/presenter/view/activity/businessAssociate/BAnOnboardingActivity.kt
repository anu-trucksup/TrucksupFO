package com.trucksup.field_officer.presenter.view.activity.businessAssociate

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PnData
import com.trucksup.field_officer.databinding.ActivityBaNewOnboardingBinding
import com.trucksup.field_officer.databinding.VerifyOtpDialogBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.model.AddBrokerRequest
import com.trucksup.field_officer.presenter.view.activity.businessAssociate.vml.BAOnboardViewModel
import com.trucksup.field_officer.presenter.view.adapter.SubCityAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
class BAnOnboardingActivity : BaseActivity() {
    private lateinit var binding: ActivityBaNewOnboardingBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var mViewModel: BAOnboardViewModel? = null
    private var imageUri: String = ""
    private var docType: String = ""
    private var imageUrl: String = ""

    var subCityValue: String = ""
    var tempSubCityList = ArrayList<PnData>()
    private var subCity: String = ""
    var isOtherSubCity: String="N"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaNewOnboardingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        docType = intent.getStringExtra("doc").toString()
        imageUrl = intent.getStringExtra("img").toString()
        mViewModel = ViewModelProvider(this)[BAOnboardViewModel::class.java]

        binding.btnAdd.setOnClickListener() {
            checkValidation()
        }

        binding.ivBack.setOnClickListener() {
            onBackPressed()
        }

        binding.cvCamera.setOnClickListener {
            launcher
        }

        setPinCode()
        setupObserver()
        cameraListener()
    }

    private fun setPinCode() {
        binding.eTPinCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.eTPinCode.text.length == 6) {
                    showProgressDialog(this@BAnOnboardingActivity, false)
                    getPinData(PreferenceManager.getAuthToken())

                } else {
                    binding.eTCity.setText("")
                    binding.eTState.setText("")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })

        binding.spinnerSubCity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
//                val selectedItem = parent.getItemAtPosition(position).toString()
                    var tv: TextView = findViewById(view.id)
                    tv.setBackgroundColor(Color.parseColor("#00007fff"))
                    tv.setTextColor(resources.getColor(R.color.secondry_text))
                    if (!tempSubCityList.isNullOrEmpty()) {
                        if (!tempSubCityList[position].officeName.isNullOrEmpty()) {
                            subCityValue = tempSubCityList[position].officeName.toString()
                            isOtherSubCity = "N"

                            if (subCityValue == "Select City") {

                                tv.setTextColor(Color.parseColor("#c0c0c0"))
                            } else {
                                tv.setTextColor(resources.getColor(R.color.secondry_text))

                            }
                        }
                    }
                    if (subCityValue == "Other") {

                        isOtherSubCity = "Y"

                        // do your stuff
                        binding.otherSubCityLay.visibility = View.VISIBLE
                        binding.etOtherCity.setText("")
                    } else {
                        binding.otherSubCityLay.visibility = View.GONE
                        binding.etOtherCity.setText("")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {

                }
            }
    }

    private fun setupObserver() {
        mViewModel?.resultSCbyPinCodeLD?.observe(this@BAnOnboardingActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@BAnOnboardingActivity,
                        responseModel.serverError.toString(),
                        "m"
                    )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success?.statusCode == 200) {
                    if (responseModel.success.data.isNotEmpty()) {
                        pinData(
                            responseModel.success.data[0].district,
                            responseModel.success.data[0].hindiCity,
                            responseModel.success.data[0].stateName,
                            responseModel.success.data[0].hindiState
                        )

                        setSubCity(responseModel.success.data)
                    } else {
                        val abx = AlertBoxDialog(
                            this@BAnOnboardingActivity,
                            responseModel.success.message,
                            "m"
                        )
                        abx.show()

                        binding.eTCity.setText("")
                        binding.eTState.setText("")

                        binding.eTPinCode.setText("")
                    }
                } else {
                    val abx = AlertBoxDialog(
                        this@BAnOnboardingActivity,
                        responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }
            }
        }

        mViewModel?.onBoardBAResponseLD?.observe(this@BAnOnboardingActivity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@BAnOnboardingActivity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success != null) {
                    LoggerMessage.toastPrint("BA OnBoarded Successfully.", this)
                    val happinessCodeBox = HappinessCodeBox(
                        this, getString(R.string.hapinessCodeMsg),
                        getString(R.string.EnterHappinessCode),
                        getString(R.string.resand_sms)
                    )
                    happinessCodeBox.show()
                } else {
                    val abx = AlertBoxDialog(
                        this@BAnOnboardingActivity,
                        "Something went wrong.",
                        "m"
                    )
                    abx.show()
                }
            }
        }

    }

    private fun checkValidation() {
        if (binding.eTContactName.text.isEmpty()) {
            binding.eTContactName.requestFocus()
            binding.eTContactName.error = getString(R.string.PleaseEnterContactName)
        } else if (binding.eTContactNumber.text.isEmpty()) {
            binding.eTContactNumber.requestFocus()
            binding.eTContactNumber.error = getString(R.string.PleaseEnterContactNumber)
        } else if (binding.eTBusinessName.text.isEmpty()) {
            binding.eTBusinessName.requestFocus()
            binding.eTBusinessName.error = getString(R.string.PleaseEnterBusinessName)
        } else if (binding.ETBusinessAddress.text.isEmpty()) {
            binding.ETBusinessAddress.requestFocus()
            binding.ETBusinessAddress.error = getString(R.string.PleaseEnterBusinessAddress)
        } else if (binding.eTPinCode.text.isEmpty()) {
            binding.eTPinCode.requestFocus()
            binding.eTPinCode.error = getString(R.string.PleaseEnterBusinessPincode)
        } else {
            setSubmitDialog()

            val ph: String = PreferenceManager.getPhoneNo(this)

            val request = AddBrokerRequest(
                "I",
                PreferenceManager.getRequestNo(),
                PreferenceManager.getRequestNo(),
                ph,
                binding.eTContactName.text.toString(),
                binding.eTCity.text.toString(),
                "",
                binding.eTBusinessName.text.toString(),
                ph,
                binding.eTState.text.toString(),
                docType,
                imageUrl,
                "Mobile App",
                PreferenceManager.getCurrentDateTime(),
                "Unverified",
                binding.eTPinCode.text.toString()
            )
            mViewModel?.onBoardBusinessAssociate(request)
        }


    }

    private fun setSubCity(data: List<PnData>) {
        //sub city
        tempSubCityList.clear()

        if (subCity.isNullOrEmpty()) {
            tempSubCityList.add(
                PnData(
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Select City",
                    "",
                    "",
                    "",
                    ""
                )
            )
        } else {
            tempSubCityList.add(PnData("", "", "", "", "", "", "", "", subCity, "", "", "", ""))

        }

        data.forEach() { pinData ->
            tempSubCityList.add(pinData)
        }

        tempSubCityList.add(PnData("", "", "", "", "", "", "", "", "Other", "", "", "", ""))
        val subCityAdapter = SubCityAdapter(this@BAnOnboardingActivity, tempSubCityList, "#ffffff")
        binding.spinnerSubCity.adapter = subCityAdapter
    }

    private fun setSubmitDialog() {
        val builder = AlertDialog.Builder(this)
        val binding = VerifyOtpDialogBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.tvVerify.setOnClickListener {
            if (binding.pvHapinessCodeverify.text?.isEmpty() == true) {
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.error = getString(R.string.EnterHappinessCode)
                //Toast.makeText(this, "",Toast.LENGTH_SHORT).show()
            } else if (binding.pvHapinessCodeverify.text?.length!! > 6) {
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.error = "Wrong"
            } else {
                dialog.dismiss()
            }
        }

        /*Glide.with(this)
            .load(R.drawable.ab_apni_chalao_image)
            .into(binding.img)
        binding.msg.text=respnse.message
        binding.message1.text=respnse.message1

        //ok button
        binding.btnOk.setOnClickListener {
            dialog.dismiss()
            finish()
        }*/

        dialog.show()
    }

    private fun cameraListener() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    imageUri = data!!.getStringExtra("result").toString()
                    binding.profileImage?.let {
                        Glide.with(getApplicationContext())
                            .load(data!!.getStringExtra("result")?.toUri())
                            .into(it)
                    }
                    //profileImage?.setRotation(270F)
                    var orFile: File =
                        FileHelp().getFile(this, data!!.getStringExtra("result")?.toUri())!!
                    var newBitmap: Bitmap = FileHelp().fileToBitmap(orFile)


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

    private fun launchCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 0)
        launcher!!.launch(intent)
    }

    private fun getPinData(token: String) {
        val request = PinCodeRequest(
            binding.eTPinCode.text.toString(),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this)
        )
        mViewModel?.getCityStateByPin(token, request)
    }

    private fun pinData(city: String, cityHindi: String, state: String, stateHindi: String) {
        dismissProgressDialog()
        binding.eTCity.setText(city)
        binding.eTState.setText(state)

    }


}