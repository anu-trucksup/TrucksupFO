package com.trucksup.field_officer.presenter.view.activity.truckSupplier

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.logistics.trucksup.activities.preferre.modle.TrucksDetail
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.databinding.AddNewTruckLayoutBinding
import com.trucksup.field_officer.presenter.common.AlertBoxDialog
import com.trucksup.field_officer.presenter.common.JWTtoken
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.add_truck.AddTruckInterface
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadData
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterPayload
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadFilterRequest
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.AddLoadTyre
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.model.RcResponse
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.LoadItemManager
import com.trucksup.field_officer.presenter.view.activity.truckSupplier.vml.TSOnboard2ViewModel

class AddTruckDialog(
    var context: AppCompatActivity,
    private val lifecycleOwner: LifecycleOwner,
    var vehicleNo: String, var result: RcResponse?,
    val type: String
) : Dialog(context), LoadItemManager, JWTtoken {

    private var idLoad: Int = 0
    private lateinit var vehicleBinding: AddNewTruckLayoutBinding
    private val tsOnboard2ViewModel: TSOnboard2ViewModel =
        ViewModelProvider(context)[TSOnboard2ViewModel::class.java]

    private var bodyTypeList = ArrayList<AddLoadTyre>()
    private var tyreTypeList = ArrayList<AddLoadTyre>()
    private var length = ArrayList<AddLoadTyre>()
    private var materialWeight = ArrayList<AddLoadTyre>()

    private var total_tyer_value: String = ""
    private var vehicle_Size_value: String = ""
    private var body_type_value: String = ""
    private var capacity_value: String = ""
    private var typeofSourcing: String = ""
    private var loadType: Int = 1

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        vehicleBinding = AddNewTruckLayoutBinding.inflate(layoutInflater)
        setContentView(vehicleBinding.root)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        this.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        );
        this.window?.setLayout(
            ActionBar.LayoutParams.FILL_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )

        //generateToken()
        getFilter(idLoad, loadType)
        this.setCancelable(true)

        initialize()
        setObserver()
    }

    private fun setObserver() {
        tsOnboard2ViewModel.resultLoadResponseLD.observe(lifecycleOwner) { responseModel ->
            // update your dialog UI based on LiveData value

            if (responseModel.serverError != null) {
                LoadingUtils.hideDialog()

                val abx = AlertBoxDialog(
                    context, responseModel.serverError.toString(),
                    "m"
                )
                abx.show()
            } else {
                LoadingUtils.hideDialog()

                if (responseModel.success != null) {
                    if (responseModel.success.isSuccess == true) {

                        if (responseModel.success.data != null) {
                            filtterData(responseModel.success.data)
                        } else {
                            filtterError(responseModel.success.message.toString())
                        }
                    } else {
                        filtterError(responseModel.success.message.toString())
                    }
                } else {
                    val abx = AlertBoxDialog(context, "Something server error", "m")
                    abx.show()
                }
            }


        }
    }

    @SuppressLint("NewApi")
    fun initialize() {

        this.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        if (type == "add") {
            vehicleBinding.vehicalNo.text = vehicleNo
            vehicleBinding.buttonCancle.visibility = View.GONE
            vehicleBinding.butLayout.visibility = View.VISIBLE

        } else {
            vehicleBinding.vehicalNo.text = vehicleNo
            vehicleBinding.buttonCancle.visibility = View.VISIBLE
            vehicleBinding.butLayout.visibility = View.GONE

        }

        vehicleBinding.buttonCancle.setOnClickListener {
            this.dismiss()
        }
        vehicleBinding.btnCancel.setOnClickListener {
            this.dismiss()
        }
        vehicleBinding.mainLay.setOnClickListener {
            // this.dismiss()
        }

        // Listener for Checkbox One
        vehicleBinding.checkboxOwn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vehicleBinding.checkboxAttach.isChecked = false
            }
        }

        // Listener for Checkbox Two
        vehicleBinding.checkboxAttach.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vehicleBinding.checkboxOwn.isChecked = false
            }
        }

        vehicleBinding.btnAddTruck.setOnClickListener {

            if (vehicleBinding.checkboxOwn.isChecked) {
                typeofSourcing = "Owned Vehicle"
            } else {
                typeofSourcing = "Attached Vehicle"
            }

            if (validationAddTruck()) {
                val vehicleDetail = TrucksDetail(
                    vehicleNo = vehicleBinding.vehicalNo.text.toString(),
                    vehicleStatus = "",
                    mobileNo = PreferenceManager.getPhoneNo(context),
                    ownerName = result?.ownername.toString(),
                    bodytype = body_type_value,
                    tyre = total_tyer_value, capacity = capacity_value,
                    vehicleSize = vehicle_Size_value, typeofSourcing = typeofSourcing,
                    "",
                    ""

                )


                val addTruckListener = context as AddTruckInterface
                addTruckListener.setTruckDetails(vehicleDetail)

                dismiss()
            }
        }

        vehicleBinding.bodyType.setOnClickListener {
            if (validationBodyType()) {
                val allPick = LoadItemsBoxDialog(
                    "Body type",
                    context.resources.getString(R.string.body_type),
                    bodyTypeList, context,
                    this
                )
                allPick.show()
            }
        }

        vehicleBinding.vehicleSize.setOnClickListener {
            if (validationvehicleSize()) {

                if (length.size > 0) {
                    val allPick = LoadItemsBoxDialog(
                        "Vehicle Size",
                        context.resources.getString(R.string.Vehicle_Size),
                        length,
                        context, this
                    )
                    allPick.show()
                } else {
                    val fl: String = context.resources.getString(R.string.select_tyreType)
                    LoggerMessage.onSNACK(vehicleBinding.bodyType, fl, context)
                }
            }

        }

        vehicleBinding.tyre.setOnClickListener {
            if (validateTyre()) {
                val allPick = LoadItemsBoxDialog(
                    "Tyre",
                    context.resources.getString(R.string.tyre_),
                    tyreTypeList,
                    context, this
                )
                allPick.show()

            }
        }

        vehicleBinding.capicity.setOnClickListener {
            if (isTyreTypeSelected()) {
                if (materialWeight.size > 0) {
                    val allPick = LoadItemsBoxDialog(
                        "Capacity",
                        context.resources.getString(R.string.Capacity),
                        materialWeight,
                        context, this
                    )
                    allPick.show()
                } else {
                    val fl: String = context.resources.getString(R.string.select_tyreType)
                    LoggerMessage.onSNACK(vehicleBinding.bodyType, fl, context)
                }
            } else {

                if (materialWeight.size > 0) {
                    val allPick = LoadItemsBoxDialog(
                        "Capacity",
                        context.resources.getString(R.string.Capacity),
                        materialWeight,
                        context, this
                    )
                    allPick.show()
                }
            }
        }

    }

    override fun loadItoms(
        type: String,
        valueEnglish: String,
        valueHindi: String,
        unit: String,
        id: Int
    ) {

        if (type == "Capacity") {

            capacity_value = valueEnglish

            if (PreferenceManager.getLanguage(context) == "en") {
                vehicleBinding.capicity.text = valueEnglish
            } else {
                vehicleBinding.capicity.text = valueHindi
            }

        }
        if (type == "Body type") {
            body_type_value = valueEnglish
            if (PreferenceManager.getLanguage(context) == "en") {
                vehicleBinding.bodyType.text = valueEnglish
            } else {
                vehicleBinding.bodyType.text = valueHindi
            }


        }

        if (type == "Vehicle Size") {
            vehicle_Size_value = valueEnglish
            if (PreferenceManager.getLanguage(context) == "en") {
                vehicleBinding.vehicleSize.text = valueEnglish
            } else {
                vehicleBinding.vehicleSize.text = valueHindi
            }
        }

        if (type == "Tyre") {
            val string = valueEnglish

            val result = string.filter { it.isDigit() }

            total_tyer_value = result.toString()

            if (PreferenceManager.getLanguage(context) == "en") {
                vehicleBinding.tyre.text = valueEnglish
            } else {
                vehicleBinding.tyre.text = valueHindi
            }

            idLoad = id
            getFilter(id, loadType)
            // generateToken()
        }
    }

    override fun filtterData(data: AddLoadData) {

        LoadingUtils.hideDialog()
        if (data != null) {
            if (data.bodyType != null) {
                bodyTypeList = data.bodyType as ArrayList<AddLoadTyre>
            }

            if (data.tyre != null) {
                tyreTypeList = data.tyre as ArrayList<AddLoadTyre>
            }
            if (data.materialWeight != null) {
                vehicleBinding.capicity.text = ""
                vehicleBinding.vehicleSize.text = ""
                materialWeight = data.materialWeight as ArrayList<AddLoadTyre>
            }
            if (data.length != null) {
                length = data.length as ArrayList<AddLoadTyre>
            }
        }

    }

    override fun filtterError(error: String) {
        LoadingUtils.hideDialog()
        LoggerMessage.onSNACK(vehicleBinding.bodyType, error, context)
    }

    private fun isTyreTypeSelected(): Boolean {
        if (TextUtils.isEmpty(vehicleBinding.tyre.text)) {
            val fl: String = context.resources.getString(R.string.select_tyreType)
            LoggerMessage.onSNACK(vehicleBinding.bodyType, fl, context)
            return false
        }

        return true
    }

    private fun validationvehicleSize(): Boolean {

        if (TextUtils.isEmpty(vehicleBinding.vehicalNo.text)) {

            var fl: String = context.resources.getString(R.string.enterCommercialVehical)
            LoggerMessage.onSNACK(vehicleBinding.vehicalNo, fl, context)
            return false
        }

        if (TextUtils.isEmpty(vehicleBinding.bodyType?.text)) {
            var fl: String = context.resources.getString(R.string.select_bodyType)
            LoggerMessage.onSNACK(vehicleBinding.bodyType!!, fl, context)
            return false
        }

        if (TextUtils.isEmpty(vehicleBinding.tyre?.text)) {
            var fl: String = context.resources.getString(R.string.select_tyreType)
            LoggerMessage.onSNACK(vehicleBinding.tyre!!, fl, context)
            return false
        }

        if (TextUtils.isEmpty(vehicleBinding.capicity?.text)) {
            var fl: String = context.resources.getString(R.string.please_select_capacity)
            LoggerMessage.onSNACK(vehicleBinding.capicity!!, fl, context)
            return false
        }

        return true
    }

    private fun validationAddTruck(): Boolean {

        if (!vehicleBinding.checkboxOwn.isChecked && !vehicleBinding.checkboxAttach.isChecked) {

            val fl: String = context.resources.getString(R.string.enterTypeOfSource)
            LoggerMessage.onSNACK(vehicleBinding.checkboxOwn, fl, context)
            return false
        }

        if (TextUtils.isEmpty(vehicleBinding.vehicalNo.text)) {

            val fl: String = context.resources.getString(R.string.enterCommercialVehical)
            LoggerMessage.onSNACK(vehicleBinding.vehicalNo, fl, context)
            return false
        }


        if (TextUtils.isEmpty(vehicleBinding.bodyType.text)) {
            val fl: String = context.resources.getString(R.string.select_bodyType)
            LoggerMessage.onSNACK(vehicleBinding.bodyType, fl, context)
            return false
        }

        if (TextUtils.isEmpty(vehicleBinding.tyre.text)) {
            val fl: String = context.resources.getString(R.string.select_tyreType)
            LoggerMessage.onSNACK(vehicleBinding.tyre, fl, context)
            return false
        }


        if (TextUtils.isEmpty(vehicleBinding.capicity.text)) {
            val fl: String = context.resources.getString(R.string.please_select_capacity)
            LoggerMessage.onSNACK(vehicleBinding.capicity, fl, context)
            return false
        }

        if (TextUtils.isEmpty(vehicleBinding.vehicleSize.text)) {
            val fl: String = context.resources.getString(R.string.select_vehiclesize)
            LoggerMessage.onSNACK(vehicleBinding.vehicleSize, fl, context)
            return false
        }
        return true
    }

    private fun validationBodyType(): Boolean {

        if (TextUtils.isEmpty(vehicleBinding.vehicalNo.text)) {

            val fl: String = context.resources.getString(R.string.enterCommercialVehical)
            LoggerMessage.onSNACK(vehicleBinding.vehicalNo, fl, context)
            return false
        }

        return true
    }

    private fun validateTyre(): Boolean {
        if (TextUtils.isEmpty(vehicleBinding.vehicalNo.text)) {

            val fl: String = context.resources.getString(R.string.enterCommercialVehical)
            LoggerMessage.onSNACK(vehicleBinding.vehicalNo, fl, context)
            return false
        }


        if (TextUtils.isEmpty(vehicleBinding.bodyType.text)) {
            val fl: String = context.resources.getString(R.string.select_bodyType)
            LoggerMessage.onSNACK(vehicleBinding.bodyType, fl, context)
            return false
        }

        return true
    }

    /* private fun generateToken() {
         LoadingUtils.showDialog(context, false)
         val request = GenerateJWTtokenRequest(
             apiSecreteKey = PreferenceManager.getAccessKey(context),
             password = PreferenceManager.getAccesPassword(context),
             userAgent = PreferenceManager.getAccesUserAgaint(context),
             username = PreferenceManager.getAccesUserName(context),
             issuer = PreferenceManager.getAccesUserInssur(context)
         )
         tokenViewModel.generateJWToken(request, this, context)
         Log.e(" volley main url ", PreferenceManager.getServerUrl(context))
         // getFilter(id, loadType, "Bearer " + data?.accessToken.toString())
     }*/

    private fun getFilter(id: Int, loadType: Int) {

        val payload = AddLoadFilterPayload(loadType, id, "Find")
        val request = AddLoadFilterRequest(
            PreferenceManager.getPhoneNo(context),
            payload,
            PreferenceManager.getServerDateUtc(),
            PreferenceManager.getRequestNo()
        )
        tsOnboard2ViewModel.getLoadFilter(request, this, context)

    }

    override fun onTokenSuccess(response: GenerateJWTtokenResponse) {
        if (response.accessToken.isNotEmpty()) {
            //  getFilter(idLoad, loadType, "Bearer " + response.accessToken.toString())
        }

    }

    override fun onTokenFailure(msg: String) {
        TODO("Not yet implemented")
    }

}