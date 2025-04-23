package com.trucksup.field_officer.presenter.view.activity.truckSupplier

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.logistics.trucksup.activities.preferre.modle.PrefferLanRequest
import com.logistics.trucksup.activities.preferre.modle.Preflane
import com.logistics.trucksup.activities.preferre.modle.TrucksDetail
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.PinCodeRequest
import com.trucksup.field_officer.data.model.PnData
import com.trucksup.field_officer.databinding.ActivityTsonboardStep2Binding
import com.trucksup.field_officer.databinding.PreferredLaneDialogBinding
import com.trucksup.field_officer.databinding.VerifyOtpDialogBinding
import com.trucksup.field_officer.presenter.cityPicker.CityPicker
import com.trucksup.field_officer.presenter.cityPicker.CityStateDialog
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.dialog.HappinessCodeBox
import com.trucksup.field_officer.presenter.common.image_picker.TrucksFOImageController
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.add_truck.AddTruckInterface
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.TSOnboard2ViewModel
import com.trucksup.field_officer.presenter.view.adapter.TSTrucksDetailsAdapter
import com.trucksup.field_officer.presenter.view.adapter.TrucksDetailsAdap
import com.trucksup.field_officer.presenter.view.adapter.PreferredLaneAdap
import com.trucksup.field_officer.presenter.view.adapter.SubCityAdapter
import com.trucksup.field_officer.presenter.view.adapter.TruckPreferredLaneAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class TSOnBoardStep2Activity : BaseActivity(), PreferredLaneAdap.ControllerListener,
    TrucksDetailsAdap.ControllerListener, TSTrucksDetailsAdapter.ControllerListener,
    TruckPreferredLaneAdapter.ControllerListener, AddTruckInterface, CityPicker,
    TrucksFOImageController {

    private var isfromCity: Boolean = true
    private lateinit var binding: ActivityTsonboardStep2Binding
    private var preferredLaneList = ArrayList<Preflane>()
    private var trucksDetailsList = ArrayList<TrucksDetail>()

    private var mViewModel: TSOnboard2ViewModel? = null

    private var launcher: ActivityResultLauncher<Intent>? = null
    private var imageUri: String = ""
    private var profileImgKey: String? = ""
    private var profileImgUrl: String? = ""

    var subCityValue: String = ""
    var tempSubCityList = ArrayList<PnData>()
    private var subCity: String = ""
    var isOtherSubCity: String = "N"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTsonboardStep2Binding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f)
        setContentView(binding.root)
        mViewModel = ViewModelProvider(this)[TSOnboard2ViewModel::class.java]

        setListener()
        setupObserver()
        cameraListener()
    }

    private fun setupObserver() {
        mViewModel?.resultSCbyPinCodeLD?.observe(this@TSOnBoardStep2Activity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx =
                    AlertBoxDialog(
                        this@TSOnBoardStep2Activity,
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
                            this@TSOnBoardStep2Activity,
                            responseModel.success.message,
                            "m"
                        )
                        abx.show()

                        binding.eTcity.setText("")
                        binding.eTState.setText("")

                        binding.eTPincode.setText("")
                    }
                } else {
                    val abx = AlertBoxDialog(
                        this@TSOnBoardStep2Activity,
                        responseModel.success?.message.toString(),
                        "m"
                    )
                    abx.show()
                }
            }
        }

        mViewModel?.resultVerifyTruckLD?.observe(this@TSOnBoardStep2Activity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@TSOnBoardStep2Activity,
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
                        mViewModel?.getRcDetails(PreferenceManager.getAuthTokenOld(), request)
                    } else {
                        val abx = AlertBoxDialog(
                            this@TSOnBoardStep2Activity,
                            responseModel.success.message,
                            "m"
                        )
                        abx.show()
                    }
                } else {
                    val abx = AlertBoxDialog(
                        this@TSOnBoardStep2Activity,
                        "No data found",
                        "m"
                    )
                    abx.show()
                }
            }
        }

        mViewModel?.resultRcResponseLD?.observe(this@TSOnBoardStep2Activity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@TSOnBoardStep2Activity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success != null) {
                    if (responseModel.success.vehicleno.isNullOrEmpty()) {
                        val abx = AlertBoxDialog(
                            this@TSOnBoardStep2Activity,
                            resources.getString(R.string.valid_truckno),
                            "m"
                        )
                        abx.show()
                    } else {

                        val vehicleDialog = AddTruckDialog(
                            this@TSOnBoardStep2Activity, this,
                            binding.vehicalNo.text.toString(),
                            responseModel.success,
                            "add"
                        )

                        vehicleDialog.show()
                    }
                } else {
                    val abx = AlertBoxDialog(
                        this@TSOnBoardStep2Activity,
                        "No data found",
                        "m"
                    )
                    abx.show()
                }
            }
        }

        mViewModel?.onBoardTsResponseLD?.observe(this@TSOnBoardStep2Activity) { responseModel ->                     // login function observe
            if (responseModel.serverError != null) {
                dismissProgressDialog()

                val abx = AlertBoxDialog(
                    this@TSOnBoardStep2Activity,
                    responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                dismissProgressDialog()

                if (responseModel.success != null) {
                    LoggerMessage.toastPrint("TS OnBoarded Successfully.", this)
                    val happinessCodeBox = HappinessCodeBox(
                        this, getString(R.string.hapinessCodeMsg),
                        getString(R.string.EnterHappinessCode),
                        getString(R.string.resand_sms)
                    )
                    happinessCodeBox.show()
                } else {
                    val abx = AlertBoxDialog(
                        this@TSOnBoardStep2Activity,
                        "Something went wrong.",
                        "m"
                    )
                    abx.show()
                }
            }
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
        val subCityAdapter = SubCityAdapter(this@TSOnBoardStep2Activity, tempSubCityList, "#ffffff")
        binding.spinnerSubCity?.adapter = subCityAdapter
    }

    private fun setListener() {

        binding.btnAdd.setOnClickListener {
            checkValidation()
        }

        binding.cvCamera.setOnClickListener {
            launchCamera()
        }

        binding.eTPincode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.eTPincode.text.length == 6) {
                    showProgressDialog(this@TSOnBoardStep2Activity, false)
                    getPinData(PreferenceManager.getAuthToken())

                } else {
                    binding.eTcity.setText("")
                    binding.eTState.setText("")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
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
                    val tv: TextView = findViewById(view.id)
                    tv.setBackgroundColor(Color.parseColor("#00007fff"))
                    tv.setTextColor(resources.getColor(R.color.secondary_text))
                    if (!tempSubCityList.isNullOrEmpty()) {
                        if (!tempSubCityList[position].officeName.isNullOrEmpty()) {
                            subCityValue = tempSubCityList[position].officeName.toString()
                            isOtherSubCity = "N"

                            if (subCityValue == "Select City") {

                                tv.setTextColor(Color.parseColor("#c0c0c0"))
                            } else {
                                tv.setTextColor(resources.getColor(R.color.secondary_text))

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


        //back button
        binding.ivBack.setOnClickListener {
            if (binding.llStep2.visibility == View.VISIBLE) {
                binding.llStep2.visibility = View.INVISIBLE
                binding.llStep1.visibility = View.VISIBLE
            } else {
                onBackPressed()
            }

        }

        //add preferred lane
        binding.btnPreferredLane.setOnClickListener {
            //addPreferredLane(this)
            if (!TextUtils.isEmpty(binding.etFromCity.text.trim()) && !TextUtils.isEmpty(binding.etToCity.text.trim())) {
                preferredLaneList.add(
                    Preflane(
                        binding.etFromCity.getText().toString(),
                        binding.etToCity.getText().toString()
                    )
                )
                setRvPreferredLane()
            }
        }

        //add truck details
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

        //cancel button
        binding.btnCancel.setOnClickListener {
            finish()
        }

        //cancel button
        binding.btnCancel1.setOnClickListener {
            finish()
        }

        //submit button
        binding.btnSubmit.setOnClickListener {

            if (trucksDetailsList.isEmpty() || trucksDetailsList.size == 0) {
                LoggerMessage.onSNACK(
                    binding.rvTrucksDetails,
                    resources.getString(R.string.add_one_truck_msg),
                    this
                )
                return@setOnClickListener
            }

            if (preferredLaneList.isEmpty() || preferredLaneList.size == 0) {

                LoggerMessage.onSNACK(
                    binding.rvPreferredLane,
                    "Please Enter Preferred Lane",
                    this
                )

                return@setOnClickListener
            }


            showProgressDialog(this, false)
            val requests = PrefferLanRequest(
                binding.eTState.text.toString() + "",
                binding.eTState.text.toString() + "",
                binding.eTcity.text.toString() + "",
                PreferenceManager.getPhoneNo(this),
                "", "",
                PreferenceManager.getPhoneNo(this),
                binding.eTBusinessName.text.toString(),
                binding.eTPincode.text.toString() + "",
                preflane = preferredLaneList,
                1,
                PreferenceManager.getPhoneNo(this),
                PreferenceManager.getServerDateUtc(),
                trucksDetails = trucksDetailsList,
                ""
            )
            mViewModel?.onBoardTruckSupplier(requests)

        }


        binding.etFromCity.setOnClickListener {
            isfromCity = true
            val cityDialog = CityStateDialog(this, this, "cs", binding.etFromCity, "M", true)
            cityDialog.show()
            binding.vehicalNo.clearFocus()
        }

        binding.etToCity.setOnClickListener {
            isfromCity = false
            val cityDialog = CityStateDialog(this, this, "cs", binding.etToCity, "M", true)
            cityDialog.show()
            binding.vehicalNo.clearFocus()
        }
    }


    private fun verifyTruck() {
        mViewModel?.verifyTruckDetails(
            PreferenceManager.getAuthTokenOld(),
            binding.vehicalNo.text.toString(),
            PreferenceManager.getPhoneNo(this)
        )

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
                Preflane(
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
            layoutManager =
                LinearLayoutManager(this@TSOnBoardStep2Activity, RecyclerView.VERTICAL, false)
            adapter = TruckPreferredLaneAdapter(
                this@TSOnBoardStep2Activity,
                preferredLaneList,
                this@TSOnBoardStep2Activity
            )
            hasFixedSize()
        }
    }

    private fun setRvTruckDetails() {
        binding.rvTrucksDetails.apply {
            layoutManager =
                LinearLayoutManager(this@TSOnBoardStep2Activity, RecyclerView.VERTICAL, false)
            adapter = TSTrucksDetailsAdapter(
                this@TSOnBoardStep2Activity,
                trucksDetailsList,
                this@TSOnBoardStep2Activity
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

    private fun cameraListener() {
        launcher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data

                try {
                    imageUri = data?.getStringExtra("result").toString()
                    /* binding.profileImage.let {
                         Glide.with(applicationContext)
                             .load(data?.getStringExtra("result")?.toUri())
                             .into(it)
                     }*/

                    val orFile: File = FileHelp().getFile(this, result.data?.data)!!

                    uploadImage(orFile, "")

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    fun uploadImage(file: File, token: String) {
        LoadingUtils.showDialog(this, false)

        mViewModel?.uploadProfileImage(
            PreferenceManager.getAuthToken(),
            "image",
            PreferenceManager.prepareFilePartTrucksHum(file, "imageFile"),
            PreferenceManager.prepareFilePartTrucksHum(file, "watermarkFile"),
            this
        )

    }

    private fun launchCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 0)
        launcher?.launch(intent)
    }

    private fun checkValidation() {
        if (binding.eTContactName.text.isEmpty()) {
            binding.eTContactName.requestFocus()
            binding.eTContactName.error = getString(R.string.PleaseEnterContactName)
        } else if (imageUri.isNullOrEmpty()) {
            LoggerMessage.onSNACK(
                binding.cvCamera,
                resources.getString(R.string.PleaseSelectStorePhoto),
                this
            )
        } else if (binding.eTContactNumber.text.isEmpty()) {
            binding.eTContactNumber.requestFocus()
            binding.eTContactNumber.error = getString(R.string.PleaseEnterContactNumber)
        } else if (binding.eTContactNumber.text.length < 10) {
            binding.eTContactNumber.requestFocus()
            binding.eTContactNumber.error = getString(R.string.enter_right_number_v)
        } else if (binding.eTBusinessName.text.isEmpty()) {
            binding.eTBusinessName.requestFocus()
            binding.eTBusinessName.error = getString(R.string.PleaseEnterBusinessName)
        } else if (binding.eTPincode.text.isEmpty()) {
            binding.eTPincode.requestFocus()
            binding.eTPincode.error = getString(R.string.PleaseEnterBusinessPincode)
        } else if (binding.eTPincode.text.length < 6) {
            binding.eTPincode.requestFocus()
            binding.eTPincode.error = getString(R.string.PleaseEnterRightPincode)
        } else {
            binding.llStep1.visibility = View.GONE
            binding.llStep2.visibility = View.VISIBLE
        }
    }

    private fun getPinData(token: String) {
        val request = PinCodeRequest(
            binding.eTPincode.text.toString(),
            PreferenceManager.getRequestNo(),
            PreferenceManager.getPhoneNo(this)
        )
        mViewModel?.getCityStateByPin(token, request)
    }

    private fun pinData(city: String, cityHindi: String, state: String, stateHindi: String) {
        dismissProgressDialog()

        binding.eTcity.setText(city)
        binding.eTState.setText(state)

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
                binding.pvHapinessCodeverify.setError(getString(R.string.EnterHappinessCode))
                //Toast.makeText(this, "",Toast.LENGTH_SHORT).show()
            } else if (binding.pvHapinessCodeverify.text?.length!! > 6) {
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.setError("Wrong")
            } else {
                dialog.dismiss()
            }
        }

        //  timer_progress?.setProgressWithAnimation(65f, 1000)
        object : CountDownTimer(60000, 1000) {
            override fun onTick(msUntilFinished: Long) {
                // displayText.setText("remaining sec: " + msUntilFinished / 1000)
                var tm: Long = msUntilFinished % 1000
                binding.timecounter?.text = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(msUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(msUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    msUntilFinished
                                )
                            )
                )

            }

            override fun onFinish() {
                //goback = true
                binding.timecounter?.visibility = View.GONE
                binding.txtHinttimecounter?.visibility = View.GONE
                binding.resendVerificationCodeTxt?.visibility = View.VISIBLE
                //cancle?.visibility = View.VISIBLE
            }
        }.start()

        dialog.show()
    }


    override fun getImage(value: String, url: String) {
        LoadingUtils.hideDialog()
        profileImgKey = value
        profileImgUrl = url
        try {
            Glide.with(this)
                .load(url)
                .into(binding.profileImage)
        } catch (e: Exception) {
        }

    }

    override fun dataSubmitted(message: String) {}

    override fun imageError(error: String) {
        LoadingUtils.hideDialog()
        LoggerMessage.onSNACK(binding.profileImage, error, this)
    }

}
