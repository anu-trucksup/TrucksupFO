package com.trucksup.field_officer.presenter.view.activity.growthPartner

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGpPersonalDetailUpdateBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.regex.Pattern

class GPPersonalDetailUpdateActivity : BaseActivity(), View.OnClickListener {
    private var launcher: ActivityResultLauncher<Intent>? = null
    private lateinit var binding: ActivityGpPersonalDetailUpdateBinding
    private var imageUri:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gp_personal_detail_update)
        val view = binding.root
        setContentView(view)
        setOnClicks()
        camera()
    }

    private fun launchCamera(){
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 0)
        launcher!!.launch(intent)
    }
    fun camera() {
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
                    val MEDIA_PATH = Environment.getExternalStorageDirectory().absolutePath + "/" + pt + "/"

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
    fun CheckValidation() {
        if (imageUri.isEmpty() || imageUri == null) {
            LoggerMessage.onSNACK(
                binding.cvCamera,
                resources.getString(R.string.PleaseSelectStorePhoto),
                this
            )
        }else if (binding.ETSalesCode.text.isEmpty()) {
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
        } else if (binding.ETGPBusinessType.text.isEmpty()) {
            binding.ETGPBusinessType.requestFocus()
            binding.ETGPBusinessType.setError(getString(R.string.PleaseBusinessType))
        }else if (binding.ETGpPincode.text.isEmpty()) {
            binding.ETGpPincode.requestFocus()
            binding.ETGpPincode.setError(getString(R.string.PleaseEnterBusinessPincode))
        }
        else if (binding.ETGpPincode.text.length < 6) {
            binding.ETGpPincode.requestFocus()
            binding.ETGpPincode.setError(getString(R.string.PleaseEnterRightPincode))
        }else if (binding.ETGpBusinessAddress.text.isEmpty()) {
            binding.ETGpBusinessAddress.requestFocus()
            binding.ETGpBusinessAddress.setError(getString(R.string.PleaseBusinessAddress))
        } else {
            startActivity(Intent(this, GPOnboardingKYCActivity::class.java))
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
            Toast.makeText(this, "HIBB",Toast.LENGTH_SHORT).show()
            phone.length > 6 && phone.length <= 10
        } else {
            Toast.makeText(this, "HIAA",Toast.LENGTH_SHORT).show()
            false
        }
    }

}