package com.trucksup.field_officer.presenter.view.activity.growth_partner

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityGponboardingStoreproofBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.utils.LoggerMessage
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class GPOnBoardStoreProofActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGponboardingStoreproofBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var imageUri: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gponboarding_storeproof)
        val view = binding.root
        setContentView(view)
        binding.cvCamera.setOnClickListener {
            launchCamera()
        }
        binding.btnPreview.setOnClickListener {
            checkValidation()
        }
        camera()
    }

    private fun camera() {
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
                    var newBitmap: Bitmap = FileHelp().FileToBitmap(orFile)


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

    private fun launchCamera(){
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 1)
        launcher!!.launch(intent)
    }

    private fun checkValidation() {
        if (imageUri.isEmpty() || imageUri == null) {
            LoggerMessage.onSNACK(
                binding.cvCamera,
                resources.getString(R.string.PleaseSelectStorePhoto),
                this
            )
        } else {
            startActivity(Intent(this, GPOnboardingBankActivity::class.java))
        }
        /*else if (binding.ETPanNumberNOB.length() == 10) {
            val s = binding.ETPanNumberNOB.toString() // get your editext value here
            val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")
            val matcher: Matcher = pattern.matcher(s)
            // Check if pattern matches
            if (matcher.matches()) {
                panNumber = binding.ETPanNumberNOB.toString()
            } else {
                //LoggerMessage.tostPrint("Enter Right PAN No", this)
                binding.ETPanNumberNOB?.setError("Enter Right PAN Noss")
                // pan_no?.setText("")
            }
        } else {
            binding.ETPanNumberNOB.requestFocus()
            *//*val customErrorDrawable = resources.getDrawable(com.trucksup.fieldofficer.R.drawable.ic_phone)
            customErrorDrawable.setBounds(
                0,
                0,
                customErrorDrawable.intrinsicWidth,
                customErrorDrawable.intrinsicHeight
            )
            binding.ETPanNumberNOB?.setError("Enter Right PAN No" , customErrorDrawable)*//*
            binding.ETPanNumberNOB?.setError("Enter Right PAN No")
            binding.ETPanNumberNOB?.setText("")
        }*/

    }
}