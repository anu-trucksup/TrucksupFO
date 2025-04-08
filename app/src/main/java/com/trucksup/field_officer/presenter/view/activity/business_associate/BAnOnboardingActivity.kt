package com.trucksup.field_officer.presenter.view.activity.business_associate

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.trucksup.field_officer.R
import com.trucksup.field_officer.databinding.ActivityBaNewOnboardingBinding
import com.trucksup.field_officer.databinding.VerifyOtpDialogBinding
import com.trucksup.field_officer.presenter.common.CameraActivity
import com.trucksup.field_officer.presenter.common.FileHelp
import com.trucksup.field_officer.presenter.common.parent.BaseActivity
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class BAnOnboardingActivity : BaseActivity() {
    private lateinit var binding: ActivityBaNewOnboardingBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var imageUri:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaNewOnboardingBinding.inflate(layoutInflater)
        adjustFontScale(resources.configuration, 1.0f);
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener(){
            checkValidation()
        }

        binding.ivBack.setOnClickListener(){
            onBackPressed()
        }

        binding.cvCamera.setOnClickListener{
            launcher
        }

        camera()
    }

    private fun checkValidation() {
        if (binding.ETAccountHolderName.text.isEmpty()) {
            binding.ETAccountHolderName.requestFocus()
            binding.ETAccountHolderName.error = getString(R.string.PleaseEnterContactName)
        } else if (binding.ETAccountHolderNumber.text.isEmpty()) {
            binding.ETAccountHolderNumber.requestFocus()
            binding.ETAccountHolderNumber.error = getString(R.string.PleaseEnterContactNumber)
        } else if (binding.ETBusinessName.text.isEmpty()) {
            binding.ETBusinessName.requestFocus()
            binding.ETBusinessName.error = getString(R.string.PleaseEnterBusinessName)
        } else if (binding.ETBusinessAddress.text.isEmpty()) {
            binding.ETBusinessAddress.requestFocus()
            binding.ETBusinessAddress.error = getString(R.string.PleaseEnterBusinessAddress)
        } else if (binding.ETPincode.text.isEmpty()) {
            binding.ETPincode.requestFocus()
            binding.ETPincode.error = getString(R.string.PleaseEnterBusinessPincode)
        }else{
            setSubmitDialog()
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

    private fun setSubmitDialog() {
        val builder = AlertDialog.Builder(this)
        val binding = VerifyOtpDialogBinding.inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        val dialog: AlertDialog = builder.create()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.tvVerify.setOnClickListener{
            if(binding.pvHapinessCodeverify.text?.isEmpty() == true){
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.error = getString(R.string.EnterHappinessCode)
                //Toast.makeText(this, "",Toast.LENGTH_SHORT).show()
            }else if(binding.pvHapinessCodeverify.text?.length!! > 6){
                binding.pvHapinessCodeverify.requestFocus()
                binding.pvHapinessCodeverify.error = "Wrong"
            }else{
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
                    var newBitmap: Bitmap = FileHelp().FileToBitmap(orFile)


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

    private fun launchCamera(){
        val intent = Intent(this, CameraActivity::class.java)
        intent.putExtra("flipCamera", true)
        intent.putExtra("cameraOpen", 0)
        launcher!!.launch(intent)
    }

}