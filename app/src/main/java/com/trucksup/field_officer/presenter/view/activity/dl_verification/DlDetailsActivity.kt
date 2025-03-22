package com.trucksup.field_officer.presenter.view.activity.dl_verification

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityDlDetailsBinding
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.FileHelper
import com.trucksup.field_officer.presenter.utils.PreferenceManager
import java.io.File

class DlDetailsActivity : BaseActivity()/*, JWTtoken, DLVerifyContrllerRes */{

    private lateinit var binding: ActivityDlDetailsBinding
    private var dob: String? = null
    private var dlNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(getResources().getConfiguration(), 1.0f);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dl_details)

        val getData = intent.getStringExtra("DATA")
        val getDlNo = intent.getStringExtra("DL_NO")
        val getDob = intent.getStringExtra("DOB")

        dob = getDob
        dlNumber = getDlNo

      /*  val data = Gson().fromJson(getData, DLVerifyResponse::class.java)
        if (data != null) {
            binding.main.visibility = View.VISIBLE
            showData(data)
        } else {
            getToken()
        }*/

        setListener()
    }

    private fun setListener() {
        //back button
        binding.btnBack.setOnClickListener {
            finish()
        }

        //share button
        binding.btnShare.setOnClickListener {
            findView(binding.shareLayout, this)
        }

        //transaction history button
        binding.btnTransactionHis.setOnClickListener {
            /* val intent = Intent(this, TransactionHistory::class.java)
             startActivity(intent)*/
        }
    }

    private fun findView(view: View, context: Context) {

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

    private fun shareApp(file: File) {

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
        val imgBitmap: Bitmap = FileHelper().FileToBitmap(file)    // BitmapFactory.decodeResource(context.getResources(), R.drawable.whatsapp_trucks_logo)
        path = PreferenceManager.getImageUri(this, imgBitmap)


        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_STREAM, path);
            type = "image/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, "TrucksUp")
        startActivity(shareIntent)
    }


    /*fun getToken() {
//        progressDailogBox?.show()
        LoadingUtils.showDialog(this, false)
        var request = GenerateJWTtokenRequest(
            username = PreferenceManager.getAccesUserName(this),
            password = PreferenceManager.getAccesPassword(this),
            apiSecreteKey = PreferenceManager.getAccesKey(this),
            userAgent = PreferenceManager.getAccesUserAgaint(this),
            issuer = PreferenceManager.getAccesUserInssur(this)
        )
        MyResponse().generateJWTtoken(request, this, this)
    }

    fun showData(d: DLVerifyResponse) {
        if (d.dlDetails.licenseNumber.isNullOrEmpty()) {
            binding.tvDlNumber.text = resources.getString(R.string.not_available)
            binding.tvLicenseNo.text = resources.getString(R.string.not_available)
        } else {
            binding.tvDlNumber.text = d.dlDetails.licenseNumber
            binding.tvLicenseNo.text = d.dlDetails.licenseNumber
        }

        if (d.dlDetails.doe.isNullOrEmpty()) {
            binding.tvDoeDob.text = resources.getString(R.string.not_available)
        } else {
            binding.tvDoeDob.text = d.dlDetails.doe
        }

        if (d.dlDetails.name.isNullOrEmpty()) {
            binding.tvName.text = resources.getString(R.string.not_available)
        } else {
            binding.tvName.text = d.dlDetails.name
        }

        if (d.dlDetails.vehicleClasses.isNullOrEmpty()) {
            binding.tvAuthToDrive.text = resources.getString(R.string.not_available)
        } else {
            binding.tvAuthToDrive.text = d.dlDetails.vehicleClasses
        }

        if (d.dlDetails.olaName.isNullOrEmpty()) {
            binding.tvIssuedAuth.text = resources.getString(R.string.not_available)
        } else {
            binding.tvIssuedAuth.text = d.dlDetails.olaName
        }

        if (d.dlDetails.dob.isNullOrEmpty()) {
            binding.tvDob.text = resources.getString(R.string.not_available)
        } else {
            binding.tvDob.text = d.dlDetails.dob
        }

        if (d.dlDetails.doe.isNullOrEmpty()) {
            binding.tvDoe.text = resources.getString(R.string.not_available)
        } else {
            binding.tvDoe.text = d.dlDetails.doe
        }

        if (d.dlDetails.doi.isNullOrEmpty()) {
            binding.tvDoi.text = resources.getString(R.string.not_available)
        } else {
            binding.tvDoi.text = d.dlDetails.doi
        }

        if (d.dlDetails.fatherOrHusbandName.isNullOrEmpty()) {
            binding.tvFatherHusName.text = resources.getString(R.string.not_available)
        } else {
            binding.tvFatherHusName.text = d.dlDetails.fatherOrHusbandName
        }

        if (d.dlDetails.state.isNullOrEmpty()) {
            binding.tvState.text = resources.getString(R.string.not_available)
        } else {
            binding.tvState.text = d.dlDetails.state
        }

        if (d.dlDetails.permanentAddress.isNullOrEmpty()) {
            if (d.dlDetails.temporaryAddress.isNullOrEmpty()) {
                binding.tvAddress.text = resources.getString(R.string.not_available)
            } else {
                binding.tvAddress.text = d.dlDetails.temporaryAddress
            }
        } else {
            binding.tvAddress.text = d.dlDetails.permanentAddress
        }

        if (d.dlDetails.profileImage.isNullOrEmpty()) {
            try {
                Glide
                    .with(this)
                    .load(R.drawable.vc_user_icon)
                    .centerCrop()
                    .placeholder(R.drawable.vc_user_icon)
                    .into(binding.imgProfile)
            } catch (e: Exception) {

            }
        } else {
            try {
//                Glide
//                    .with(this)
//                    .load(d.dlDetails.profileImage)
//                    .centerCrop()
//                    .placeholder(R.drawable.vc_user_icon)
//                    .into(binding.imgProfile)
                val imageBytes = Base64.decode(d.dlDetails.profileImage, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                binding.imgProfile.setImageBitmap(decodedImage)
            } catch (e: Exception) {

            }
        }

        if (d.dlDetails.expiryDate.trim().lowercase() == "valid") {
            binding.tvExStatus.visibility = View.VISIBLE
            binding.imgAttention.visibility = View.GONE
            binding.tvExStatus.text = resources.getString(R.string.dl_valid)
            binding.tvExStatus.background = resources.getDrawable(R.drawable.status_background2)

//            binding.titleDoeDob.text=resources.getString(R.string.date_of_expiry)
//            if (d.dlDetails.doe.isNullOrEmpty())
//            {
//                binding.tvDoeDob.text=resources.getString(R.string.not_available)
//            }
//            else
//            {
//                binding.tvDoeDob.text=d.dlDetails.doe
//            }

//            binding.line1.visibility=View.GONE
//            binding.doeLay.visibility=View.GONE
//
//            binding.line2.visibility=View.VISIBLE
//            binding.dobLay.visibility=View.VISIBLE
        } else if (d.dlDetails.expiryDate.trim().lowercase() == "expired") {
            binding.tvExStatus.visibility = View.VISIBLE
            binding.imgAttention.visibility = View.GONE
            binding.tvExStatus.text = resources.getString(R.string.dl_expired)
            binding.tvExStatus.background = resources.getDrawable(R.drawable.background_5)

//            binding.titleDoeDob.text=resources.getString(R.string.dob)
//            if (d.dlDetails.dob.isNullOrEmpty())
//            {
//                binding.tvDoeDob.text=resources.getString(R.string.not_available)
//            }
//            else
//            {
//                binding.tvDoeDob.text=d.dlDetails.dob
//            }
//
//            binding.line1.visibility=View.VISIBLE
//            binding.doeLay.visibility=View.VISIBLE
//
//            binding.line2.visibility=View.GONE
//            binding.dobLay.visibility=View.GONE
        } else {
            binding.tvExStatus.visibility = View.GONE
            binding.imgAttention.visibility = View.VISIBLE
//            binding.titleDoeDob.text=resources.getString(R.string.dob)
//            if (d.dlDetails.dob.isNullOrEmpty())
//            {
//                binding.tvDoeDob.text=resources.getString(R.string.not_available)
//            }
//            else
//            {
//                binding.tvDoeDob.text=d.dlDetails.dob
//            }
//
//            binding.line1.visibility=View.VISIBLE
//            binding.doeLay.visibility=View.VISIBLE
//
//            binding.line2.visibility=View.GONE
//            binding.dobLay.visibility=View.GONE
        }
    }

    override fun onTokenSuccess(response: GenerateJWTtokenResponse) {
        var request = DLVerifyRequest(
            requestId = PreferenceManager.getRequestNo(),
            requestDatetime = PreferenceManager.getServerDateUtc(""),
            requestedBy = PreferenceManager.getPhoneNo(this),
            mobileNumber = PreferenceManager.getPhoneNo(this),
            dob = dob!!,
            idNumber = dlNumber!!,
            isPreview = true
        )
        MyResponse().dlVerify(response.accessToken, request, this, this)
    }

    override fun onTokenFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        binding.main.visibility = View.VISIBLE
    }

    override fun onVerifyDetailsSuccess(response: DLVerifyResponse) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        binding.main.visibility = View.VISIBLE
        showData(response)
    }

    override fun onVerifyDetailsFailure(msg: String) {
//        progressDailogBox?.dismiss()
        LoadingUtils.hideDialog()
        binding.main.visibility = View.VISIBLE
    }*/

}