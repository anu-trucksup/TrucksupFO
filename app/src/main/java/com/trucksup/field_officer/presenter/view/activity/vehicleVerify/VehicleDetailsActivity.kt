package com.logistics.trucksup.activities.vehicleVerify

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.logistics.trucksup.modle.VerifyVehicleResponse
import com.trucksup.field_officer.R
import com.trucksup.field_officer.data.model.GenerateJWTtokenRequest
import com.trucksup.field_officer.data.model.GenerateJWTtokenResponse
import com.trucksup.field_officer.databinding.ActivityVehicleDetailsBinding
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.LoadingUtils
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.JWTtoken
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.MyResponse
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.VehicleVerifyController
import com.trucksup.field_officer.presenter.view.activity.vehicleVerify.model.VerifyVehicleRequest
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class VehicleDetailsActivity : BaseActivity(), JWTtoken, VehicleVerifyController {

    private lateinit var binding: ActivityVehicleDetailsBinding

    var data: VerifyVehicleResponse? = null
    var vehicleNo: String? = null
    val calendar = Calendar.getInstance()
    var shareValue: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_details)

//        data= Gson().fromJson(PreferenceManager.getVehicleDetails(this), VerifyVehicleResponse::class.java)
        var getData = intent.getStringExtra("DATA")
        var getData2 = intent.getStringExtra("VEHICLE_NO")
        var getData3 = intent.getStringExtra("SHARE")

        vehicleNo = getData2
        shareValue = getData3
        data = Gson().fromJson(getData, VerifyVehicleResponse::class.java)

        if (data != null) {
            binding.contentVisibility.visibility = View.VISIBLE
            binding.vehicleImage.visibility = View.VISIBLE
            if (data?.vehicleDetails?.vehicleNumber.isNullOrEmpty()) {
                binding.tvVehicleNo.text = resources.getString(R.string.not_available)
                binding.tvVehicleNumber.text = resources.getString(R.string.not_available)
            } else {
                binding.tvVehicleNo.text = data?.vehicleDetails?.vehicleNumber
                binding.tvVehicleNumber.text = data?.vehicleDetails?.vehicleNumber
            }

//            //newly added
//            if(PreferenceManager.getLanguage(this)=="en")
//            {
//                var st=resources.getString(R.string.date_service)+" "+data?.vehicleDetails?.createdDate+" "+resources.getString(R.string.date_service_end)+" "
//                val ed="T&C."
//                val spanTxt = SpannableStringBuilder(
//                    st
//                )
//                spanTxt.append(ed)
//                spanTxt.setSpan(object : ClickableSpan() {
//                    override fun onClick(widget: View) {
////                        Toast.makeText(this@VehicleDetailsActivity, "Hello", Toast.LENGTH_SHORT).show()
//                    }
//                }, spanTxt.length-ed.length, spanTxt.length-1, 0)
//                binding.tvVehicleDetailVerified.movementMethod=LinkMovementMethod.getInstance()
//                binding.tvVehicleDetailVerified.setText(spanTxt, TextView.BufferType.SPANNABLE)
//                binding.tvVehicleDetailVerified.setLinkTextColor(resources.getColor(R.color.pink2))//"#870583"
//            }
//            else
//            {
//                var st=resources.getString(R.string.date_service)+" "+data?.vehicleDetails?.createdDate+" "+"को सहमति "
//                val ed="शर्तों "
//                val spanTxt = SpannableStringBuilder(
//                    st
//                )
//                spanTxt.append(ed)
//                spanTxt.setSpan(object : ClickableSpan() {
//                    override fun onClick(widget: View) {
//                        Toast.makeText(this@VehicleDetailsActivity, "Hello", Toast.LENGTH_SHORT).show()
//                    }
//                }, spanTxt.length-ed.length, spanTxt.length, 0)
//                spanTxt.append(resources.getString(R.string.date_service_end))
//                binding.tvVehicleDetailVerified.movementMethod=LinkMovementMethod.getInstance()
//                binding.tvVehicleDetailVerified.setText(spanTxt, TextView.BufferType.SPANNABLE)
//                binding.tvVehicleDetailVerified.setLinkTextColor(resources.getColor(R.color.pink2))
////                binding.tvVehicleDetailVerified.text=resources.getString(R.string.date_service)+" "+data?.vehicleDetails?.createdDate+" "+resources.getString(R.string.date_service_end)
//            }

            binding.tvVehicleDetailVerified.text =
                resources.getString(R.string.date_service) + " " + data?.vehicleDetails?.createdDate + " " + resources.getString(
                    R.string.date_service_end
                )

            if (data?.vehicleDetails?.ownerName.isNullOrEmpty()) {
                binding.tvOwnerName.text = resources.getString(R.string.not_available)
            } else {
                binding.tvOwnerName.text = data?.vehicleDetails?.ownerName
            }

            if (data?.vehicleDetails?.chasiNo.isNullOrEmpty()) {
                binding.tvChassicNo.text = resources.getString(R.string.not_available)
            } else {
                binding.tvChassicNo.text = data?.vehicleDetails?.chasiNo
            }

            if (data?.vehicleDetails?.engineNumber.isNullOrEmpty()) {
                binding.tvEngineName.text = resources.getString(R.string.not_available)
            } else {
                binding.tvEngineName.text = data?.vehicleDetails?.engineNumber
            }

            if (data?.vehicleDetails?.manufacturingYear.isNullOrEmpty()) {
                binding.tvManufacturingDate.text = resources.getString(R.string.not_available)
            } else {
                binding.tvManufacturingDate.text = data?.vehicleDetails?.manufacturingYear
//                binding.tvManufacturingDate.setTextColor(resources.getColor(R.color.active_color))
            }

            if (data?.vehicleDetails?.status.isNullOrEmpty()) {
                binding.tvRcStatus.text = resources.getString(R.string.not_available)
            } else {
                binding.tvRcStatus.text = data?.vehicleDetails?.status
//                binding.tvRcStatus.text = data?.vehicleDetails?.status
                if (data?.vehicleDetails?.status?.trim()?.lowercase() == "active") {
                    binding.tvRcStatus.setTextColor(resources.getColor(R.color.active_color))
                } else {
                    binding.tvRcStatus.setTextColor(resources.getColor(R.color.expired_color))
                }
            }

            if (data?.vehicleDetails?.insuranceUpto.isNullOrEmpty()) {
                binding.tvInsUpto.text = resources.getString(R.string.not_available)
            } else {
                if (data?.vehicleDetails?.data_Insurance_Upto_status.isNullOrEmpty()) {
                    binding.tvInsUpto.text = data?.vehicleDetails?.insuranceUpto
                    setColorInsuranceValidUpto()
                } else if (data?.vehicleDetails?.data_Insurance_Upto_status?.lowercase() == "valid") {
                    binding.tvInsUpto.text = data?.vehicleDetails?.insuranceUpto
                    binding.tvInsUpto.setTextColor(resources.getColor(R.color.active_color))
                } else {
                    binding.tvInsUpto.text =
                        data?.vehicleDetails?.insuranceUpto + " (${data?.vehicleDetails?.data_Insurance_Upto_status})"
                    binding.tvInsUpto.setTextColor(resources.getColor(R.color.expired_color))
                }
            }

            if (data?.vehicleDetails?.nationalPermitUpto.isNullOrEmpty()) {
                binding.tvNationalPermit1.text = resources.getString(R.string.not_available)
            } else {
//                binding.tvNationalPermit1.text=data?.vehicleDetails?.nationalPermitUpto
//                setColorPermitValidUpto(binding.tvNationalPermit1)

                if (data?.vehicleDetails?.data_National_Permit_Upto_status.isNullOrEmpty()) {
                    binding.tvNationalPermit1.text = data?.vehicleDetails?.nationalPermitUpto
                    setColorPermitValidUpto(binding.tvNationalPermit1)
                } else if (data?.vehicleDetails?.data_National_Permit_Upto_status?.lowercase() == "valid") {
                    binding.tvNationalPermit1.text = data?.vehicleDetails?.nationalPermitUpto
                    binding.tvNationalPermit1.setTextColor(resources.getColor(R.color.active_color))
                } else {
                    binding.tvNationalPermit1.text =
                        data?.vehicleDetails?.nationalPermitUpto + " (${data?.vehicleDetails?.data_National_Permit_Upto_status})"
                    binding.tvNationalPermit1.setTextColor(resources.getColor(R.color.expired_color))
                }

//                if (data?.vehicleDetails.natio)
            }

//            if (data?.vehicleDetails?.puccNo.isNullOrEmpty())
//            {
//                binding.tvPucNumber.text=resources.getString(R.string.not_available)
//            }
//            else
//            {
//                binding.tvPucNumber.text=data?.vehicleDetails?.puccNo
//            }

            if (data?.vehicleDetails?.Fitness.isNullOrEmpty()) {
                binding.tvFitnessUpto.text = resources.getString(R.string.not_available)
            } else {
//                binding.tvFitnessUpto.text=data?.vehicleDetails?.Fitness
//                setColorFitnessValidUpto()

                if (data?.vehicleDetails?.data_Vehicle_Fit_Upto_status.isNullOrEmpty()) {
                    binding.tvFitnessUpto.text = data?.vehicleDetails?.Fitness
                    setColorFitnessValidUpto()
                } else if (data?.vehicleDetails?.data_Vehicle_Fit_Upto_status?.lowercase() == "valid") {
                    binding.tvFitnessUpto.text = data?.vehicleDetails?.Fitness
                    binding.tvFitnessUpto.setTextColor(resources.getColor(R.color.active_color))
                } else {
                    binding.tvFitnessUpto.text =
                        data?.vehicleDetails?.Fitness + " (${data?.vehicleDetails?.data_Vehicle_Fit_Upto_status})"
                    binding.tvFitnessUpto.setTextColor(resources.getColor(R.color.expired_color))
                }
            }

            if (data?.vehicleDetails?.vehicleRegisteredAt.isNullOrEmpty()) {
                binding.tvRegistrationAut.text = resources.getString(R.string.not_available)
            } else {
                binding.tvRegistrationAut.text = data?.vehicleDetails?.vehicleRegisteredAt
            }

            if (data?.vehicleDetails?.model.isNullOrEmpty()) {
                binding.tvModel.text = resources.getString(R.string.not_available)
            } else {
                binding.tvModel.text = data?.vehicleDetails?.model
            }

            if (data?.vehicleDetails?.registrationDate.isNullOrEmpty()) {
                binding.tvRegistrationDate.text = resources.getString(R.string.not_available)
            } else {
                binding.tvRegistrationDate.text = data?.vehicleDetails?.registrationDate
            }

//            if (data?.vehicleDetails?.permitValidUpto.isNullOrEmpty())
//            {
//                binding.tvPermitValidUpto.text=resources.getString(R.string.not_available)
//            }
//            else
//            {
//                binding.tvPermitValidUpto.text=data?.vehicleDetails?.permitValidUpto
//                setColorPermitValidUpto()
//            }

            if (data?.vehicleDetails?.permitValidUpto.isNullOrEmpty()) {
                binding.tvNationalPermit2.text = resources.getString(R.string.not_available)
            } else {
//                binding.tvNationalPermit2.text=data?.vehicleDetails?.permitValidUpto
//                setColorPermitValidUpto(binding.tvNationalPermit2)

                if (data?.vehicleDetails?.data_Permit_Valid_upto_status.isNullOrEmpty()) {
                    binding.tvNationalPermit2.text = data?.vehicleDetails?.permitValidUpto
                    setColorPermitValidUpto(binding.tvNationalPermit2)
                } else if (data?.vehicleDetails?.data_Permit_Valid_upto_status?.lowercase() == "valid") {
                    binding.tvNationalPermit2.text = data?.vehicleDetails?.permitValidUpto
                    binding.tvNationalPermit2.setTextColor(resources.getColor(R.color.active_color))
                } else {
                    binding.tvNationalPermit2.text =
                        data?.vehicleDetails?.permitValidUpto + " (${data?.vehicleDetails?.data_Permit_Valid_upto_status})"
                    binding.tvNationalPermit2.setTextColor(resources.getColor(R.color.expired_color))
                }
            }

            if (data?.vehicleDetails?.puccUpto.isNullOrEmpty()) {
                binding.tvPucUpto.text = resources.getString(R.string.not_available)
            } else {
//                binding.tvPucUpto.text=data?.vehicleDetails?.puccUpto
//                setColorPucValidUpto()

                if (data?.vehicleDetails?.data_PUCC_upto_status.isNullOrEmpty()) {
                    binding.tvPucUpto.text = data?.vehicleDetails?.puccUpto
                    setColorPucValidUpto()
                } else if (data?.vehicleDetails?.data_PUCC_upto_status?.lowercase() == "valid") {
                    binding.tvPucUpto.text = data?.vehicleDetails?.puccUpto
                    binding.tvPucUpto.setTextColor(resources.getColor(R.color.active_color))
                } else {
                    binding.tvPucUpto.text =
                        data?.vehicleDetails?.puccUpto + " (${data?.vehicleDetails?.data_PUCC_upto_status})"
                    binding.tvPucUpto.setTextColor(resources.getColor(R.color.expired_color))
                }
            }

            if (data?.vehicleDetails?.vehicleTaxUpto.isNullOrEmpty()) {
                binding.tvTaxUpTo.text = resources.getString(R.string.not_available)
            } else {
//                binding.tvTaxUpTo.text=data?.vehicleDetails?.vehicleTaxUpto
//                setColorTaxValidUpto()

                if (data?.vehicleDetails?.data_Vehicle_Tax_Upto_status.isNullOrEmpty()) {
                    binding.tvTaxUpTo.text = data?.vehicleDetails?.vehicleTaxUpto
                    setColorTaxValidUpto()
                } else if (data?.vehicleDetails?.data_Vehicle_Tax_Upto_status?.lowercase() == "valid") {
                    binding.tvTaxUpTo.text = data?.vehicleDetails?.vehicleTaxUpto
                    binding.tvTaxUpTo.setTextColor(resources.getColor(R.color.active_color))
                } else {
                    binding.tvTaxUpTo.text =
                        data?.vehicleDetails?.vehicleTaxUpto + " ${data?.vehicleDetails?.data_Vehicle_Tax_Upto_status}"
                    binding.tvTaxUpTo.setTextColor(resources.getColor(R.color.expired_color))
                }
            }

            if (data?.vehicleDetails?.fuelType.isNullOrEmpty()) {
                binding.tvFuelType.text = resources.getString(R.string.not_available)
            } else {
                binding.tvFuelType.text = data?.vehicleDetails?.fuelType
            }

            if (data?.vehicleDetails?.permanent_address.isNullOrEmpty()) {
                if (data?.vehicleDetails?.present_address.isNullOrEmpty()) {
                    binding.tvAddress.text = resources.getString(R.string.not_available)
                } else {
                    binding.tvAddress.text = data?.vehicleDetails?.present_address
                }
            } else {
                binding.tvAddress.text = data?.vehicleDetails?.permanent_address
            }

            if (data?.vehicleDetails?.body_type.isNullOrEmpty()) {
                binding.tvBodyType.text = resources.getString(R.string.not_available)
            } else {
                binding.tvBodyType.text = data?.vehicleDetails?.body_type
            }

        }
        if (!vehicleNo.isNullOrEmpty()) {
            getToken()
        }

        setListeners()
    }

    private fun setListeners() {
        //ok button
        binding.btnOk.setOnClickListener {
            finish()
        }

        binding.btnShare.setOnClickListener {
//            checkPermission()
            findView(binding.linearVehicleDetail, this)
        }

        //transaction history button
        binding.btnTransactionHis.setOnClickListener {
            /*  val intent = Intent(this, TransactionHistory::class.java)
              startActivity(intent)*/
        }
    }


    fun backScreen(view: View) {
        finish()
    }

    fun findView(view: View, context: Context) {

//        view.isDrawingCacheEnabled = true
//        val bitmap = Bitmap.createBitmap(view.drawingCache)
//        view.isDrawingCacheEnabled = false
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        // Create a canvas using the bitmap
        val canvas = Canvas(bitmap)

        view.draw(canvas)
        if (bitmap != null) {
            var file: File = PreferenceManager.bitmapTofile(bitmap, context)!!

            shareApp(file)
        } else {
            Log.e("File com", "File null ")
        }

    }

    fun shareApp(file: File) {

        var path: Uri? = null
        var nm = PreferenceManager.getUserName(this!!)
        var ch: Char = ' '
        var adhar: String = nm?.replace(ch.toString(), "").toString();
        var n: String = adhar.toUpperCase()
        var code: String = nm
        if (nm.length > 3) {
            code =
                n.subSequence(0, 4).toString() + PreferenceManager.getPhoneNo(this!!).toUpperCase()
        } else {
            code = nm.toString().trim() + PreferenceManager.getPhoneNo(this!!).toUpperCase()
        }
        var shareText: String =
            "To download the app, click on the secure link given below. Download Link: \n https://play.google.com/store/apps/details?id=com.logistics.trucksup&referrer=" + code.trim()

        if (PreferenceManager.getLanguage(this) == "hi") {
            shareText =
                "ऐप डाउनलोड करने के लिए नीचे दिए गए सुरक्षित लिंक पर क्लिक करे: \n https://play.google.com/store/apps/details?id=com.logistics.trucksup&referrer=" + code.trim()
        }

//        var shareText: String = "VEHICLE NUMBER :${binding.tvVehicleNo.text.toString()}"

//        if (PreferenceManager.getLanguage(this) == "hi") {
//            shareText = "वाहन संख्या :${binding.tvVehicleNo.text.toString()}"
//        }


        val imgBitmap: Bitmap =
            FileHelp().FileToBitmap(file)    // BitmapFactory.decodeResource(context.getResources(), R.drawable.whatsapp_trucks_logo)
        path = PreferenceManager.getImageUri(this!!, imgBitmap)


        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_STREAM, path);
            type = "image/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, "TrucksUp")
        startActivity(shareIntent)
    }

//    fun shareApp(file: File)
//    {
//
//        var path: Uri?= null
//        var nm=PreferenceManager.getUserName(this!!)
//        var ch:Char=' '
//        var  adhar:String = nm?.replace(ch.toString(), "").toString();
//        var n:String=adhar.toUpperCase()
//        var code:String  =  n.subSequence(0,4).toString()+PreferenceManager.getPhoneNo(this!!).toUpperCase()
////        var shareText:String="To download the app, click on the secure link given below. Download Link: \n https://play.google.com/store/apps/details?id=com.logistics.trucksup&referrer="+code.trim()
////
////        if (PreferenceManager.getLanguage(this)=="hi")
////        {
////            shareText="ऐप डाउनलोड करने के लिए नीचे दिए गए सुरक्षित लिंक पर क्लिक करे: \n https://play.google.com/store/apps/details?id=com.logistics.trucksup&referrer="+code.trim()
////        }
//
//        var shareText:String="VEHICLE NUMBER :${binding.tvVehicleNo.text.toString()}"
//
//        if (PreferenceManager.getLanguage(this)=="hi")
//        {
//            shareText="वाहन संख्या :${binding.tvVehicleNo.text.toString()}"
//        }
//
//        val imgBitmap: Bitmap = FileHelp().FileToBitmap(file)    // BitmapFactory.decodeResource(context.getResources(), R.drawable.whatsapp_trucks_logo)
//        path = PreferenceManager.getImageUri(this!!, imgBitmap)
//
//
//
//
//        val sendIntent: Intent = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, shareText)
//            putExtra(Intent.EXTRA_STREAM, path);
//            type = "image/*"
//        }
//
//        val shareIntent = Intent.createChooser(sendIntent, "TrucksUp")
//        startActivity(shareIntent)
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1010) {
            if (grantResults.isNotEmpty()) {
                val locationAccepted: Boolean =
                    grantResults[0] === PackageManager.PERMISSION_GRANTED
                // val cameraAccepted:Boolean = grantResults[1] === PackageManager.PERMISSION_GRANTED

                if (locationAccepted == true) {
                    findView(binding.linearVehicleDetail, this)
                } else {

                    divPer()
                }
            } else {
                divPer()
            }

        }
    }

    fun divPer() {
        /*var intent: Intent = Intent(this, DevicePermitioins::class.java)
        startActivity(intent)*/
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("Click", "Click opn location ask  ")
            checkLocationPermission()


        } else {
            findView(binding.linearVehicleDetail, this)
        }

    }

    private fun checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.CAMERA
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.READ_MEDIA_IMAGES,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                }

            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_MEDIA_IMAGES,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1010
                    )
                }

            }
        }

    }

    fun getToken() {
//        progressDailogBox?.show()
        LoadingUtils.showDialog(this, false)
        var request = GenerateJWTtokenRequest(
            username = PreferenceManager.getAccesUserName(this),
            password = PreferenceManager.getAccesPassword(this),
            apiSecreteKey = PreferenceManager.getAccesKey(this),
            userAgent = PreferenceManager.getAccesUserAgaint(this),
            issuer = PreferenceManager.getAccesUserInssur(this)
        )
        // MyResponse().generateJWTtoken(request, this, this)
    }

    override fun onTokenSuccess(response: GenerateJWTtokenResponse) {
        val rnds1 = (0..10).random()
        val rnds2 = (0..10).random()
//            var request = VerifyVehicleRequest(
//                mobileNumber = "8881236353",
//                refrenceNo = 1232.toString(),
//                orderId = "23456",
//                requestDatetime = "2024-10-07T12:24:54.968Z",
//                requestId = "1232",
//                vehicleNumber = "RJ14GL9748"
//            )
        var request = VerifyVehicleRequest(
            mobileNumber = PreferenceManager.getPhoneNo(this),
            rnds1.toString(),
            rnds2.toString(),
            PreferenceManager.getServerDateUtc(""),
            PreferenceManager.getRequestNo(),
            vehicleNo.toString(), true
        )
        //  MyResponse().verifyVehicle(response.accessToken, request, this, this)
    }

    override fun onTokenFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
    }

    override fun onVerifySuccess(response: VerifyVehicleResponse) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        binding.contentVisibility.visibility = View.VISIBLE
        binding.vehicleImage.visibility = View.VISIBLE
        if (response?.vehicleDetails?.vehicleNumber.isNullOrEmpty()) {
            binding.tvVehicleNo.text = resources.getString(R.string.not_available)
            binding.tvVehicleNumber.text = resources.getString(R.string.not_available)
        } else {
            binding.tvVehicleNo.text = response?.vehicleDetails?.vehicleNumber
            binding.tvVehicleNumber.text = response?.vehicleDetails?.vehicleNumber
        }

//        if(PreferenceManager.getLanguage(this)=="en")
//        {
//            var st=resources.getString(R.string.date_service)+" "+response?.vehicleDetails?.createdDate+" "+resources.getString(R.string.date_service_end)+" "
//            val ed="T&C."
//            val spanTxt = SpannableStringBuilder(
//                st
//            )
//            spanTxt.append(ed)
//            spanTxt.setSpan(object : ClickableSpan() {
//                override fun onClick(widget: View) {
////                    Toast.makeText(this@VehicleDetailsActivity, "Hello", Toast.LENGTH_SHORT).show()
//                }
//            }, spanTxt.length-ed.length, spanTxt.length-1, 0)
//            binding.tvVehicleDetailVerified.movementMethod=LinkMovementMethod.getInstance()
//            binding.tvVehicleDetailVerified.setText(spanTxt, TextView.BufferType.SPANNABLE)
//            binding.tvVehicleDetailVerified.setLinkTextColor(resources.getColor(R.color.pink2))//"#870583"
//        }
//        else
//        {
////            var st=resources.getString(R.string.date_service)+" "+response?.vehicleDetails?.createdDate+" "+resources.getString(R.string.date_service_end)+" "
//            var st=resources.getString(R.string.date_service)+" "+response?.vehicleDetails?.createdDate+" "+"को सहमति "
//            val ed="शर्तों "
//            val spanTxt = SpannableStringBuilder(
//                st
//            )
//            spanTxt.append(ed)
//            spanTxt.setSpan(object : ClickableSpan() {
//                override fun onClick(widget: View) {
//                    Toast.makeText(this@VehicleDetailsActivity, "Hello", Toast.LENGTH_SHORT).show()
//                }
//            }, spanTxt.length-ed.length, spanTxt.length, 0)
//            spanTxt.append(resources.getString(R.string.date_service_end))
//            binding.tvVehicleDetailVerified.movementMethod=LinkMovementMethod.getInstance()
//            binding.tvVehicleDetailVerified.setText(spanTxt, TextView.BufferType.SPANNABLE)
//            binding.tvVehicleDetailVerified.setLinkTextColor(resources.getColor(R.color.pink2))//"#870583"
////            binding.tvVehicleDetailVerified.text=resources.getString(R.string.date_service)+" "+response?.vehicleDetails?.createdDate+" "+resources.getString(R.string.date_service_end)
//        }

        binding.tvVehicleDetailVerified.text =
            resources.getString(R.string.date_service) + " " + response?.vehicleDetails?.createdDate + " " + resources.getString(
                R.string.date_service_end
            )

        if (response?.vehicleDetails?.ownerName.isNullOrEmpty()) {
            binding.tvOwnerName.text = resources.getString(R.string.not_available)
        } else {
            binding.tvOwnerName.text = response?.vehicleDetails?.ownerName
        }

        if (response?.vehicleDetails?.chasiNo.isNullOrEmpty()) {
            binding.tvChassicNo.text = resources.getString(R.string.not_available)
        } else {
            binding.tvChassicNo.text = response?.vehicleDetails?.chasiNo
        }

        if (response?.vehicleDetails?.engineNumber.isNullOrEmpty()) {
            binding.tvEngineName.text = resources.getString(R.string.not_available)
        } else {
            binding.tvEngineName.text = response?.vehicleDetails?.engineNumber
        }

        if (response?.vehicleDetails?.manufacturingYear.isNullOrEmpty()) {
            binding.tvManufacturingDate.text = resources.getString(R.string.not_available)
        } else {
            binding.tvManufacturingDate.text = response?.vehicleDetails?.manufacturingYear
//            binding.tvManufacturingDate.setTextColor(resources.getColor(R.color.active_color))
        }

        if (response?.vehicleDetails?.status.isNullOrEmpty()) {
            binding.tvRcStatus.text = resources.getString(R.string.not_available)
        } else {
            binding.tvRcStatus.text = response?.vehicleDetails?.status
            if (response?.vehicleDetails?.status?.trim()?.lowercase() == "active") {
                binding.tvRcStatus.setTextColor(resources.getColor(R.color.active_color))
            } else {
                binding.tvRcStatus.setTextColor(resources.getColor(R.color.expired_color))
            }
        }

        if (response?.vehicleDetails?.insuranceUpto.isNullOrEmpty()) {
            binding.tvInsUpto.text = resources.getString(R.string.not_available)
        } else {
//            binding.tvInsUpto.text=response?.vehicleDetails?.insuranceUpto
//            setColorInsuranceValidUpto()

            if (response?.vehicleDetails?.data_Insurance_Upto_status.isNullOrEmpty()) {
                binding.tvInsUpto.text = response?.vehicleDetails?.insuranceUpto
                setColorInsuranceValidUpto()
            }
            if (response?.vehicleDetails?.data_Insurance_Upto_status?.lowercase() == "valid") {
                binding.tvInsUpto.text = response?.vehicleDetails?.insuranceUpto
                binding.tvInsUpto.setTextColor(resources.getColor(R.color.active_color))
            } else {
                binding.tvInsUpto.text =
                    response?.vehicleDetails?.insuranceUpto + " (${response?.vehicleDetails?.data_Insurance_Upto_status})"
                binding.tvInsUpto.setTextColor(resources.getColor(R.color.expired_color))
            }
        }

        if (response?.vehicleDetails?.nationalPermitUpto.isNullOrEmpty()) {
            binding.tvNationalPermit1.text = resources.getString(R.string.not_available)
        } else {
//            binding.tvNationalPermit1.text=response?.vehicleDetails?.nationalPermitUpto
//            setColorPermitValidUpto(binding.tvNationalPermit1)

            if (response?.vehicleDetails?.data_National_Permit_Upto_status.isNullOrEmpty()) {
                binding.tvNationalPermit1.text = response?.vehicleDetails?.nationalPermitUpto
                setColorPermitValidUpto(binding.tvNationalPermit1)
            } else if (response?.vehicleDetails?.data_National_Permit_Upto_status?.lowercase() == "valid") {
                binding.tvNationalPermit1.text = response?.vehicleDetails?.nationalPermitUpto
                binding.tvNationalPermit1.setTextColor(resources.getColor(R.color.active_color))
            } else {
                binding.tvNationalPermit1.text =
                    response?.vehicleDetails?.nationalPermitUpto + " (${response?.vehicleDetails?.data_National_Permit_Upto_status})"
                binding.tvNationalPermit1.setTextColor(resources.getColor(R.color.expired_color))
            }
        }

//        if (response?.vehicleDetails?.puccNo.isNullOrEmpty())
//        {
//            binding.tvPucNumber.text=resources.getString(R.string.not_available)
//        }
//        else
//        {
//            binding.tvPucNumber.text=response?.vehicleDetails?.puccNo
//        }

        if (response?.vehicleDetails?.Fitness.isNullOrEmpty()) {
            binding.tvFitnessUpto.text = resources.getString(R.string.not_available)
        } else {
//            binding.tvFitnessUpto.text=response?.vehicleDetails?.Fitness
//            setColorFitnessValidUpto()
            if (response?.vehicleDetails?.data_Vehicle_Fit_Upto_status.isNullOrEmpty()) {
                binding.tvFitnessUpto.text = response?.vehicleDetails?.Fitness
                setColorFitnessValidUpto()
            } else if (response?.vehicleDetails?.data_Vehicle_Fit_Upto_status?.lowercase() == "valid") {
                binding.tvFitnessUpto.text = response?.vehicleDetails?.Fitness
                binding.tvFitnessUpto.setTextColor(resources.getColor(R.color.active_color))
            } else {
                binding.tvFitnessUpto.text =
                    response?.vehicleDetails?.Fitness + " (${response?.vehicleDetails?.data_Vehicle_Fit_Upto_status})"
                binding.tvFitnessUpto.setTextColor(resources.getColor(R.color.expired_color))
            }
        }

        if (response?.vehicleDetails?.vehicleRegisteredAt.isNullOrEmpty()) {
            binding.tvRegistrationAut.text = resources.getString(R.string.not_available)
        } else {
            binding.tvRegistrationAut.text = response?.vehicleDetails?.vehicleRegisteredAt
        }

        if (response?.vehicleDetails?.model.isNullOrEmpty()) {
            binding.tvModel.text = resources.getString(R.string.not_available)
        } else {
            binding.tvModel.text = response?.vehicleDetails?.model
        }

        if (response?.vehicleDetails?.registrationDate.isNullOrEmpty()) {
            binding.tvRegistrationDate.text = resources.getString(R.string.not_available)
        } else {
            binding.tvRegistrationDate.text = response?.vehicleDetails?.registrationDate
        }

//        if (response?.vehicleDetails?.permitValidUpto.isNullOrEmpty())
//        {
//            binding.tvPermitValidUpto.text=resources.getString(R.string.not_available)
//        }
//        else
//        {
//            binding.tvPermitValidUpto.text=response?.vehicleDetails?.permitValidUpto
//            setColorPermitValidUpto()
//        }

        if (response?.vehicleDetails?.permitValidUpto.isNullOrEmpty()) {
            binding.tvNationalPermit2.text = resources.getString(R.string.not_available)
        } else {
//            binding.tvNationalPermit2.text=response?.vehicleDetails?.permitValidUpto
//            setColorPermitValidUpto(binding.tvNationalPermit2)
            if (response?.vehicleDetails?.data_Permit_Valid_upto_status.isNullOrEmpty()) {
                binding.tvNationalPermit2.text = response?.vehicleDetails?.permitValidUpto
                setColorPermitValidUpto(binding.tvNationalPermit2)
            } else if (response?.vehicleDetails?.data_Permit_Valid_upto_status?.lowercase() == "valid") {
                binding.tvNationalPermit2.text = response?.vehicleDetails?.permitValidUpto
                binding.tvNationalPermit2.setTextColor(resources.getColor(R.color.active_color))
            } else {
                binding.tvNationalPermit2.text =
                    response?.vehicleDetails?.permitValidUpto + " (${response?.vehicleDetails?.data_Permit_Valid_upto_status})"
                binding.tvNationalPermit2.setTextColor(resources.getColor(R.color.expired_color))
            }
        }

        if (response?.vehicleDetails?.puccUpto.isNullOrEmpty()) {
            binding.tvPucUpto.text = resources.getString(R.string.not_available)
        } else {
//            binding.tvPucUpto.text=response?.vehicleDetails?.puccUpto
//            setColorPucValidUpto()
            if (response?.vehicleDetails?.data_PUCC_upto_status.isNullOrEmpty()) {
                binding.tvPucUpto.text = response?.vehicleDetails?.puccUpto
                setColorPucValidUpto()
            } else if (response?.vehicleDetails?.data_PUCC_upto_status?.lowercase() == "valid") {
                binding.tvPucUpto.text = response?.vehicleDetails?.puccUpto
                binding.tvPucUpto.setTextColor(resources.getColor(R.color.active_color))
            } else {
                binding.tvPucUpto.text =
                    response?.vehicleDetails?.puccUpto + " (${response?.vehicleDetails?.data_PUCC_upto_status})"
                binding.tvPucUpto.setTextColor(resources.getColor(R.color.expired_color))
            }
        }

        if (response?.vehicleDetails?.vehicleTaxUpto.isNullOrEmpty()) {
            binding.tvTaxUpTo.text = resources.getString(R.string.not_available)
        } else {
//            binding.tvTaxUpTo.text=response?.vehicleDetails?.vehicleTaxUpto
//            setColorTaxValidUpto()

            if (response?.vehicleDetails?.data_Vehicle_Tax_Upto_status.isNullOrEmpty()) {
                binding.tvTaxUpTo.text = response?.vehicleDetails?.vehicleTaxUpto
                setColorTaxValidUpto()
            } else if (response?.vehicleDetails?.data_Vehicle_Tax_Upto_status?.lowercase() == "valid") {
                binding.tvTaxUpTo.text = response?.vehicleDetails?.vehicleTaxUpto
                binding.tvTaxUpTo.setTextColor(resources.getColor(R.color.active_color))
            } else {
                binding.tvTaxUpTo.text =
                    response?.vehicleDetails?.vehicleTaxUpto + " ${response?.vehicleDetails?.data_Vehicle_Tax_Upto_status}"
                binding.tvTaxUpTo.setTextColor(resources.getColor(R.color.expired_color))
            }
        }

        if (response?.vehicleDetails?.fuelType.isNullOrEmpty()) {
            binding.tvFuelType.text = resources.getString(R.string.not_available)
        } else {
            binding.tvFuelType.text = response?.vehicleDetails?.fuelType
        }

        if (response?.vehicleDetails?.permanent_address.isNullOrEmpty()) {
            if (response?.vehicleDetails?.present_address.isNullOrEmpty()) {
                binding.tvAddress.text = resources.getString(R.string.not_available)
            } else {
                binding.tvAddress.text = response?.vehicleDetails?.present_address
            }
        } else {
            binding.tvAddress.text = response?.vehicleDetails?.permanent_address
        }

        if (response?.vehicleDetails?.body_type.isNullOrEmpty()) {
            binding.tvBodyType.text = resources.getString(R.string.not_available)
        } else {
            binding.tvBodyType.text = response?.vehicleDetails?.body_type
        }

        if (!shareValue.isNullOrEmpty()) {
            try {
                Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
                    override fun run() {
                        findView(binding.linearVehicleDetail, this@VehicleDetailsActivity)
                    }
                }, 250)
            } catch (e: Exception) {
            }
        }
    }

//    private fun setColorRegistrationValidUpto() {
//        try {
//            val formatter = SimpleDateFormat("dd-MM-yyyy")
//            val date = Date()
//            val dateFormatted = formatter.format(date)
//            if (PreferenceManager?.isNextDateBetween(
//                    PreferenceManager.getDateFormet(
//                        "dd",
//                        "dd-MM-yyyy",
//                        binding..text.toString() + ""
//                    ).toString().toInt(),
//                    PreferenceManager.getDateFormet(
//                        "MM",
//                        "dd-MM-yyyy",
//                        binding.tvRegistrationUpTo.text.toString() + ""
//                    ).toString().toInt(),
//                    PreferenceManager.getDateFormet(
//                        "yyyy",
//                        "dd-MM-yyyy",
//                        binding.tvRegistrationUpTo.text.toString() + ""
//                    ).toString().toInt(),
//                    dateFormatted,
//                    "dd-MM-yyyy"
//                ) == true
//            ) {
//                //next with current date
//                binding.tvRegistrationUpTo.setTextColor(resources.getColor(R.color.active_color))
//            } else {
//                //previous date
//                binding.tvRegistrationUpTo.setTextColor(resources.getColor(R.color.expired_color))
//            }
//        } catch (e: Exception) {
//
//        }
//    }
//

    private fun setColorFitnessValidUpto() {
        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            val dateFormatted = formatter.format(date)
            if (PreferenceManager?.isNextDateBetween(
                    PreferenceManager.getDateFormet(
                        "dd",
                        "dd-MM-yyyy",
                        binding.tvFitnessUpto.text.toString() + ""
                    ).toString().toInt(),
                    PreferenceManager.getDateFormet(
                        "MM",
                        "dd-MM-yyyy",
                        binding.tvFitnessUpto.text.toString() + ""
                    ).toString().toInt(),
                    PreferenceManager.getDateFormet(
                        "yyyy",
                        "dd-MM-yyyy",
                        binding.tvFitnessUpto.text.toString() + ""
                    ).toString().toInt(),
                    dateFormatted,
                    "dd-MM-yyyy"
                ) == true
            ) {
                //next with current date
                binding.tvFitnessUpto.setTextColor(resources.getColor(R.color.active_color))
            } else {
                //previous date
                binding.tvFitnessUpto.setTextColor(resources.getColor(R.color.expired_color))
            }
        } catch (e: Exception) {

        }
    }

    private fun setColorInsuranceValidUpto() {
        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            val dateFormatted = formatter.format(date)
            if (PreferenceManager?.isNextDateBetween(
                    PreferenceManager.getDateFormet(
                        "dd",
                        "dd-MM-yyyy",
                        binding.tvInsUpto.text.toString() + ""
                    ).toString().toInt(),
                    PreferenceManager.getDateFormet(
                        "MM",
                        "dd-MM-yyyy",
                        binding.tvInsUpto.text.toString() + ""
                    ).toString().toInt(),
                    PreferenceManager.getDateFormet(
                        "yyyy",
                        "dd-MM-yyyy",
                        binding.tvInsUpto.text.toString() + ""
                    ).toString().toInt(),
                    dateFormatted,
                    "dd-MM-yyyy"
                ) == true
            ) {
                //next with current date
                binding.tvInsUpto.setTextColor(resources.getColor(R.color.active_color))
            } else {
                //previous date
                binding.tvInsUpto.setTextColor(resources.getColor(R.color.expired_color))
            }
        } catch (e: Exception) {

        }
    }


    //
    private fun setColorPucValidUpto() {
        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            val dateFormatted = formatter.format(date)
            if (PreferenceManager?.isNextDateBetween(
                    PreferenceManager.getDateFormet(
                        "dd",
                        "dd-MM-yyyy",
                        binding.tvPucUpto.text.toString() + ""
                    ).toString().toInt(),
                    PreferenceManager.getDateFormet(
                        "MM",
                        "dd-MM-yyyy",
                        binding.tvPucUpto.text.toString() + ""
                    ).toString().toInt(),
                    PreferenceManager.getDateFormet(
                        "yyyy",
                        "dd-MM-yyyy",
                        binding.tvPucUpto.text.toString() + ""
                    ).toString().toInt(),
                    dateFormatted,
                    "dd-MM-yyyy"
                ) == true
            ) {
                //next with current date
                binding.tvPucUpto.setTextColor(resources.getColor(R.color.active_color))
            } else {
                //previous date
                binding.tvPucUpto.setTextColor(resources.getColor(R.color.expired_color))
            }
        } catch (e: Exception) {

        }
    }

    private fun setColorPermitValidUpto(id: TextView) {
        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            val dateFormatted = formatter.format(date)
            if (PreferenceManager?.isNextDateBetween(
                    PreferenceManager.getDateFormet(
                        "dd",
                        "dd-MM-yyyy",
                        id.text.toString() + ""
                    ).toString().toInt(),
                    PreferenceManager.getDateFormet(
                        "MM",
                        "dd-MM-yyyy",
                        id.text.toString() + ""
                    ).toString().toInt(),
                    PreferenceManager.getDateFormet(
                        "yyyy",
                        "dd-MM-yyyy",
                        id.text.toString() + ""
                    ).toString().toInt(),
                    dateFormatted,
                    "dd-MM-yyyy"
                ) == true
            ) {
                //next with current date
                id.setTextColor(resources.getColor(R.color.active_color))
            } else {
                //previous date
                id.setTextColor(resources.getColor(R.color.expired_color))
            }
        } catch (e: Exception) {

        }
    }

    private fun setColorTaxValidUpto() {
        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            val dateFormatted = formatter.format(date)
            if (PreferenceManager?.isNextDateBetween(
                    PreferenceManager.getDateFormet(
                        "dd",
                        "dd-MM-yyyy",
                        binding.tvTaxUpTo.text.toString() + ""
                    ).toString().toInt(),
                    PreferenceManager.getDateFormet(
                        "MM",
                        "dd-MM-yyyy",
                        binding.tvTaxUpTo.text.toString() + ""
                    ).toString().toInt(),
                    PreferenceManager.getDateFormet(
                        "yyyy",
                        "dd-MM-yyyy",
                        binding.tvTaxUpTo.text.toString() + ""
                    ).toString().toInt(),
                    dateFormatted,
                    "dd-MM-yyyy"
                ) == true
            ) {
                //next with current date
                binding.tvTaxUpTo.setTextColor(resources.getColor(R.color.active_color))
            } else {
                //previous date
                binding.tvTaxUpTo.setTextColor(resources.getColor(R.color.expired_color))
            }
        } catch (e: Exception) {

        }
    }

    override fun onVerifyFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
    }

}